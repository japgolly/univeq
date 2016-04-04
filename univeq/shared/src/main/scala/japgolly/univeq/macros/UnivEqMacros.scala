package japgolly.univeq.macros

import scala.reflect.macros.blackbox.Context
import japgolly.univeq.UnivEq

final class UnivEqMacros(val c: Context) extends MacroUtils {
  import c.universe._

  val UnivEq = c.typeOf[UnivEq[_]]

  def deriveAutoQuiet[T <: AnyRef : c.WeakTypeTag]: c.Expr[UnivEq[T]] = derive(false, true)
  def deriveAutoDebug[T <: AnyRef : c.WeakTypeTag]: c.Expr[UnivEq[T]] = derive(true , true)
  def deriveQuiet    [T <: AnyRef : c.WeakTypeTag]: c.Expr[UnivEq[T]] = derive(false, false)
  def deriveDebug    [T <: AnyRef : c.WeakTypeTag]: c.Expr[UnivEq[T]] = derive(true , false)

  def derive[T <: AnyRef : c.WeakTypeTag](debug: Boolean, auto: Boolean): c.Expr[UnivEq[T]] = {
    val T = weakTypeOf[T]

    if (debug) {
      println(sep)
      println(s"Deriving UnivEq[$T]")
    }

    try {

      tryInferImplicit(appliedType(UnivEq, T)) match {
        case Some(_) =>
          if (debug)
            println("Implicit instance already in scope.")

        case None =>
          // Macros ignore implicit params in the enclosing method :(
          // Manually scan each declared implicit and maintain a whitelist.
          val whitelist = findUnivEqAmongstImplicitParams

          if (debug && whitelist.nonEmpty)
            println("Whitelist: " + whitelist)

          ensureUnivEq(T, debug, auto,
            s => whitelist.exists(w => (s <:< w) || (s.toString == w.toString)))
      }

      val impl = q"_root_.japgolly.univeq.UnivEq.force[$T]"

      c.Expr[UnivEq[T]](impl)

    } catch {
      case e: Throwable =>
        if (debug) println(e)
        throw e
    } finally
      if (debug) {
        println(sep)
        println()
      }
  }

  def findUnivEqAmongstImplicitParams: Set[Type] = {
    val eo = c.internal.enclosingOwner
    if (eo.isMethod) {
      val m = eo.asMethod
      m.paramLists.flatten.collect { case a if a.isImplicit =>
        a.typeSignature match {
          case TypeRef(ThisType(_), x, List(inner)) if x.fullName == "japgolly.univeq.UnivEq" =>
            inner
          case _ =>
            null
        }
      }.filter(_ ne null).toSet
    } else
      Set.empty
  }

  def ensureUnivEq(T: Type, debug: Boolean, auto: Boolean, allow: Type => Boolean): Unit = {
    if (debug) println(s"→ $T")

    def found(t: Any, p: Any): Unit =
      if (debug) {
        printf("%-80s = %s\n", t.toString, p.toString())
      }

    val t = T.typeSymbol
    if (t.isType && allow(t.asType.toType))
      found(t, "implicit arg")
    else if (t.isClass && t.asClass.isCaseClass) {
      // Case class
      ensureConcrete(T)
      val params = primaryConstructorParams(T)
      for (p <- params) {
        val (pn, pt) = nameAndType(T, p)
        if (debug) println(s"  .$pn: $pt")
        val u = appliedType(UnivEq, pt)
        if (allow(pt))
          found(pt, "implicit arg")
        else
          tryInferImplicit(u) match {
            case Some(i) => found(pt, i)
            case None    => fail(s"Implicit not found: $u") //init += q"implicitly[$u]"
          }
      }
    } else
      // ADT
      crawlADT(T, p => {
        val pt = determineAdtType(T, p)
        if (allow(pt)) {
          found(pt, "implicit arg")
          Some(())
        } else if (p.isModuleClass) {
          found(pt, "case object")
          Some(())
        } else
          tryInferImplicit(appliedType(UnivEq, pt)).map(found(pt, _)).orElse {
            if (auto)
              Some(ensureUnivEq(pt, debug, auto, allow))
            else
              None
          }
      }, p => {
        val pt = p.asType.toType
        val u = appliedType(UnivEq, pt)
        fail(s"Implicit not found: $u")
        // init += q"implicitly[$u]"
        // Vector.empty
      })

    ()
  }

}