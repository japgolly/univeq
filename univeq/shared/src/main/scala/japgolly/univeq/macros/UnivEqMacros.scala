package japgolly.univeq.macros

import scala.reflect.macros.blackbox.Context
import japgolly.univeq.UnivEq

final class UnivEqMacros(val c: Context) extends MacroUtils {
  import c.universe._

  val UnivEq = c.typeOf[UnivEq[_]]
  def Unit   = c.typeOf[Unit]

  def deriveFixQuiet[Fix[_[_]], F[_]]                (implicit Fix: c.WeakTypeTag[Fix[F]], F: c.WeakTypeTag[F[Unit]]): c.Expr[UnivEq[Fix[F]]] = deriveFix(false)
  def deriveFixDebug[Fix[_[_]], F[_]]                (implicit Fix: c.WeakTypeTag[Fix[F]], F: c.WeakTypeTag[F[Unit]]): c.Expr[UnivEq[Fix[F]]] = deriveFix(true )
  def deriveFix     [Fix[_[_]], F[_]](debug: Boolean)(implicit Fix: c.WeakTypeTag[Fix[F]], F: c.WeakTypeTag[F[Unit]]): c.Expr[UnivEq[Fix[F]]] = {
    val fixF  = appliedType(Fix.tpe, F.tpe)
    val fUnit = appliedType(F.tpe, Unit)

    if (debug) {
      println(sep)
      println(s"Deriving UnivEq[$fixF]")
      println(s"     via UnivEq[$fUnit]")
    }

    attemptProof(fixF, debug) {
      val u = appliedType(UnivEq, fUnit)
      tryInferImplicit(u) match {
        case Some(i) => onFound(debug, fUnit, i)
        case None    => ensureUnivEq(fUnit, debug, auto = true, allow = _ => false)
      }
    }
  }

  // ===================================================================================================================

  def deriveAutoQuiet[T: c.WeakTypeTag]: c.Expr[UnivEq[T]] = derive(false, true)
  def deriveAutoDebug[T: c.WeakTypeTag]: c.Expr[UnivEq[T]] = derive(true , true)
  def deriveQuiet    [T: c.WeakTypeTag]: c.Expr[UnivEq[T]] = derive(false, false)
  def deriveDebug    [T: c.WeakTypeTag]: c.Expr[UnivEq[T]] = derive(true , false)

  def derive[T: c.WeakTypeTag](debug: Boolean, auto: Boolean): c.Expr[UnivEq[T]] = {
    val T = weakTypeOf[T]

    if (debug) {
      println(sep)
      println(s"Deriving UnivEq[$T]")
    }

    attemptProof(T, debug) {
      // Macros ignore implicit params in the enclosing method :(
      // Manually scan each declared implicit and maintain a whitelist.
      val whitelist = findUnivEqAmongstImplicitArgs

      if (debug && whitelist.nonEmpty)
        println("Whitelist: " + whitelist)

      ensureUnivEq(T, debug, auto,
        s => whitelist.exists(w => (s <:< w) || (s.toString == w.toString)))
    }
  }

  def findUnivEqAmongstImplicitArgs: Set[Type] = {
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
      onFound(debug, t, p)

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
        if (allow(pt))
          found(pt, "implicit arg")
        else {

          // pt = F[T[F]]
          val isFixpointOfSelf =
            T.typeArgs.contains(pt.typeConstructor) && pt.typeArgs == List(T)

          if (isFixpointOfSelf) {
            // F[Unit] is proof
            val fUnit = appliedType(pt.typeConstructor, Unit)
            val u = appliedType(UnivEq, fUnit)
            val i = needInferImplicit(u)
            found(pt, i)
            found(pt, "<univeq:fix>")

          } else {
            val u = appliedType(UnivEq, pt)
            val i = needInferImplicit(u)
            found(pt, i)
          }
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

  // ===================================================================================================================

  private def attemptProof[T](T: Type, debug: Boolean)(prove: => Any): c.Expr[UnivEq[T]] = {
    try {

      prove

      if (debug) println("Ok.")

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

  def onFound(debug: Boolean, t: Any, p: Any): Unit =
    if (debug) {
      printf("%-80s = %s\n", t.toString, p.toString())
    }
}