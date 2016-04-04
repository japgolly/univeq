package japgolly.univeq.macros

object MacroUtils {
  sealed trait FindSubClasses
  case object DirectOnly extends FindSubClasses
  case object LeavesOnly extends FindSubClasses
  case object Everything extends FindSubClasses
}

abstract class MacroUtils {
  val c: scala.reflect.macros.blackbox.Context
  import c.universe._
  import MacroUtils.FindSubClasses

  @inline final def DirectOnly = MacroUtils.DirectOnly
  @inline final def LeavesOnly = MacroUtils.LeavesOnly
  @inline final def Everything = MacroUtils.Everything

  final def sep = ("_" * 120) + "\n"

  final def fail(msg: String): Nothing =
    c.abort(c.enclosingPosition, msg)

  final def warn(msg: String): Unit =
    c.warning(c.enclosingPosition, msg)

  final def concreteWeakTypeOf[T: c.WeakTypeTag]: Type = {
    val t = weakTypeOf[T]
    ensureConcrete(t)
    t
  }

  final def ensureConcrete(t: Type): Unit = {
    val sym = t.typeSymbol.asClass
    if (sym.isAbstract)
      fail(s"ensureConcrete: [${sym.name}] is abstract which is not allowed.")
    if (sym.isTrait)
      fail(s"ensureConcrete: [${sym.name}] is a trait which is not allowed.")
    if (sym.isSynthetic)
      fail(s"ensureConcrete: [${sym.name}] is synthetic which is not allowed.")
  }

  final def primaryConstructorParams(t: Type): List[Symbol] =
    t.decls
      .collectFirst { case m: MethodSymbol if m.isPrimaryConstructor => m }
      .getOrElse(fail("Unable to discern primary constructor."))
      .paramLists
      .headOption
      .getOrElse(fail("Primary constructor missing paramList."))

  type NameAndType = (TermName, Type)
  final def nameAndType(T: Type, s: Symbol): NameAndType = {
    def paramType(name: TermName): Type =
      T.decl(name).typeSignatureIn(T) match {
        case NullaryMethodType(t) => t
        case t                    => t
      }

    val a = s.asTerm.name
    val A = paramType(a)
    (a, A)
  }

  final def ensureValidAdtBase(tpe: Type): ClassSymbol = {
    tpe.typeConstructor // https://issues.scala-lang.org/browse/SI-7755
    val sym = tpe.typeSymbol.asClass

    if (!sym.isSealed)
      fail(s"${sym.name} must be sealed.")

    if (!(sym.isAbstract || sym.isTrait))
      fail(s"${sym.name} must be abstract or a trait.")

    if (sym.knownDirectSubclasses.isEmpty)
      fail(s"${sym.name} does not have any sub-classes. This may happen due to a limitation of scalac (SI-7046).")

    sym
  }

  final def crawlADT[A](tpe: Type, f: ClassSymbol => Option[A], giveUp: ClassSymbol => Vector[A]): Vector[A] = {
    def go(t: Type, as: Vector[A]): Vector[A] = {
      val tb = ensureValidAdtBase(t)
      tb.knownDirectSubclasses.foldLeft(as) { (q, sub) =>
        val subClass = sub.asClass
        val subType = sub.asType.toType

        f(subClass) match {
          case Some(a) =>
            q :+ a
          case None =>
            if (subClass.isAbstract || subClass.isTrait)
              go(subType, q)
            else
              q ++ giveUp(subClass)
        }
      }
    }
    go(tpe, Vector.empty)
  }

  /**
   * Constraints:
   * - Type must be sealed.
   * - Type must be abstract or a trait.
   */
  final def findConcreteTypes(tpe: Type, f: FindSubClasses): Set[ClassSymbol] = {
     val sym = ensureValidAdtBase(tpe)

    def findSubClasses(p: ClassSymbol): Set[ClassSymbol] = {
      p.knownDirectSubclasses.flatMap { sub =>
        val subClass = sub.asClass
        if (subClass.isTrait)
          findSubClasses(subClass)
        else f match {
          case MacroUtils.DirectOnly => Set(subClass)
          case MacroUtils.Everything => Set(subClass) ++ findSubClasses(subClass)
          case MacroUtils.LeavesOnly =>
            val s = findSubClasses(subClass)
            if (s.isEmpty)
              Set(subClass)
            else
              s
        }
      }
    }

    findSubClasses(sym)
  }

  final def findConcreteTypesNE(tpe: Type, f: FindSubClasses): Set[ClassSymbol] = {
    val r = findConcreteTypes(tpe, f)
    if (r.isEmpty)
      fail(s"Unable to find concrete types for ${tpe.typeSymbol.name}.")
    r
  }

  final def findConcreteAdtTypes(tpe: Type, f: FindSubClasses): Set[Type] =
    findConcreteTypes(tpe, f) map (determineAdtType(tpe, _))

  final def findConcreteAdtTypesNE(tpe: Type, f: FindSubClasses): Set[Type] =
    findConcreteTypesNE(tpe, f) map (determineAdtType(tpe, _))

  /**
   * findConcreteTypes will spit out type constructors. This will turn them into types.
   *
   * @param T The ADT base trait.
   * @param t The subclass.
   */
  final def determineAdtType(T: Type, t: ClassSymbol): Type = {
    val t2 =
      if (t.typeParams.isEmpty)
        t.toType
      else if (t.isCaseClass)
        caseClassTypeCtorToType(T, t)
      else
        t.toType
    require(t2 <:< T, s"$t2 is not a subtype of $T")
    t2
  }

  /**
   * Turns a case class type constructor into a type.
   *
   * Eg. caseClassTypeCtorToType(Option[Int], Some[_]) → Some[Int]
   *
   * Actually this doesn't work with type variance :(
   */
  private def caseClassTypeCtorToType(baseTrait: Type, caseclass: ClassSymbol): Type = {
    val companion = caseclass.companion
    val apply = companion.typeSignature.member(TermName("apply"))
    if (apply == NoSymbol)
      fail(s"Don't know how to turn $caseclass into a real type of $baseTrait; it's generic and its companion has no `apply` method.")

    val matchArgs = apply.asMethod.paramLists.flatten.map { arg => pq"_" }
    val name = TermName(c.freshName("x"))
    c.typecheck(q"""(??? : $baseTrait) match {case $name@$companion(..$matchArgs) => $name }""").tpe
  }

  final def tryInferImplicit(t: Type): Option[Tree] =
    c.inferImplicitValue(t, silent = true) match {
      case EmptyTree => None
      case i         => Some(i)
    }

  final def needInferImplicit(t: Type): Tree =
    tryInferImplicit(t) getOrElse fail(s"Implicit not found: $t")
}
