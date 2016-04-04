package japgolly.univeq

import java.{lang => jl}
import scala.collection.immutable.ListSet

/**
 * Universal equality.
 */
final class UnivEq[-A] private[univeq](val equal: (A, A) => Boolean) extends AnyVal

object UnivEq {

  @inline def apply[A](implicit proof: UnivEq[A]): UnivEq[A] =
    proof

  def const[A](equality: Boolean): UnivEq[A] =
    new UnivEq((_, _) => equality)

  def always[A]: UnivEq[A] =
    const(true)

  def never[A]: UnivEq[A] =
    const(false)

  val UnivEqAnyRef: UnivEq[AnyRef] =
    new UnivEq(_ == _)

  @inline def force[A <: AnyRef]: UnivEq[A] =
    UnivEqAnyRef

  // Primitives
  implicit val UnivEqUnit   : UnivEq[Unit   ] = always
  implicit val UnivEqChar   : UnivEq[Char   ] = new UnivEq(_ == _)
  implicit val UnivEqLong   : UnivEq[Long   ] = new UnivEq(_ == _)
  implicit val UnivEqInt    : UnivEq[Int    ] = new UnivEq(_ == _)
  implicit val UnivEqShort  : UnivEq[Short  ] = new UnivEq(_ == _)
  implicit val UnivEqBoolean: UnivEq[Boolean] = new UnivEq(_ == _)
  implicit val UnivEqByte   : UnivEq[Byte   ] = new UnivEq(_ == _)

  // Scala
  @inline implicit def univEqString                       : UnivEq[String    ] = force
  @inline implicit def univEqClass  [A]                   : UnivEq[Class  [A]] = force
  @inline implicit def univEqOption [A: UnivEq]           : UnivEq[Option [A]] = force
  @inline implicit def univEqSet    [A: UnivEq]           : UnivEq[Set    [A]] = force
  @inline implicit def univEqList   [A: UnivEq]           : UnivEq[List   [A]] = force
  @inline implicit def univEqListSet[A: UnivEq]           : UnivEq[ListSet[A]] = force
  @inline implicit def univEqStream [A: UnivEq]           : UnivEq[Stream [A]] = force
  @inline implicit def univEqVector [A: UnivEq]           : UnivEq[Vector [A]] = force
  @inline implicit def univEqMap    [K: UnivEq, V: UnivEq]: UnivEq[Map[K, V] ] = force

  // Tuples
  @inline implicit def univEqTuple2[A:UnivEq, B:UnivEq]: UnivEq[(A,B)] = force
  @inline implicit def univEqTuple3[A:UnivEq, B:UnivEq, C:UnivEq]: UnivEq[(A,B,C)] = force
  @inline implicit def univEqTuple4[A:UnivEq, B:UnivEq, C:UnivEq, D:UnivEq]: UnivEq[(A,B,C,D)] = force
  @inline implicit def univEqTuple5[A:UnivEq, B:UnivEq, C:UnivEq, D:UnivEq, E:UnivEq]: UnivEq[(A,B,C,D,E)] = force
  @inline implicit def univEqTuple6[A:UnivEq, B:UnivEq, C:UnivEq, D:UnivEq, E:UnivEq, F:UnivEq]: UnivEq[(A,B,C,D,E,F)] = force
  @inline implicit def univEqTuple7[A:UnivEq, B:UnivEq, C:UnivEq, D:UnivEq, E:UnivEq, F:UnivEq, G:UnivEq]: UnivEq[(A,B,C,D,E,F,G)] = force
  @inline implicit def univEqTuple8[A:UnivEq, B:UnivEq, C:UnivEq, D:UnivEq, E:UnivEq, F:UnivEq, G:UnivEq, H:UnivEq]: UnivEq[(A,B,C,D,E,F,G,H)] = force
  @inline implicit def univEqTuple9[A:UnivEq, B:UnivEq, C:UnivEq, D:UnivEq, E:UnivEq, F:UnivEq, G:UnivEq, H:UnivEq, I:UnivEq]: UnivEq[(A,B,C,D,E,F,G,H,I)] = force

  // Java
  @inline implicit def univEqJInteger              : UnivEq[jl.Integer] = force
  @inline implicit def univEqJLong                 : UnivEq[jl.Long   ] = force
  @inline implicit def univEqJBoolean              : UnivEq[jl.Boolean] = force
  @inline implicit def univEqJByte                 : UnivEq[jl.Byte   ] = force
  @inline implicit def univEqJShort                : UnivEq[jl.Short  ] = force
  @inline implicit def univEqJEnum[A <: jl.Enum[A]]: UnivEq[jl.Enum[A]] = force

}




//  @inline implicit def univEqDisj   [A: UnivEq, B: UnivEq]: UnivEq[A \/ B]          = univEqForce
//  @inline implicit def univEqThese  [A: UnivEq, B: UnivEq]: UnivEq[A \&/ B]         = univEqForce
//  @inline implicit def univEqNel    [A: UnivEq]           : UnivEq[NonEmptyList[A]] = univEqForce
//  @inline implicit def univEqOneAnd[F[_], A](implicit fa: UnivEq[F[A]], a: UnivEq[A]): UnivEq[OneAnd[F, A]] = univEqForce

//  @inline implicit def univEqMultimap[K, L[_], V](implicit ev: UnivEq[Map[K, L[V]]]): UnivEq[Multimap[K, L, V]] = univEqForce

//
//  def  derive[A]: UnivEq[A] = macro MacroImpl.quietDerive[A]
//  def _derive[A]: UnivEq[A] = macro MacroImpl.debugDerive[A]
//  def  deriveAuto[A]: UnivEq[A] = macro MacroImpl.quietDeriveAuto[A]
//  def _deriveAuto[A]: UnivEq[A] = macro MacroImpl.debugDeriveAuto[A]
//
//  object Implicits extends UnivEqImplicits
//
//  final class Ops[A](private val a: A) extends AnyVal {
//    @inline def ==*[B >: A : UnivEq](b: B): Boolean =
//      a == b
//
//    @inline def !=*[B >: A : UnivEq](b: B): Boolean =
//      a != b
//  }
//
//  // -------------------------------------------------------------------------------------------------------------------
//
////  def withOrder[A](o: Order[A]): Order[A] with UnivEq[A] =
////    new Order[A] with UnivEq[A] {
////      override def order(a: A, b: A) = o.order(a, b)
////    }
////
////  def withArbitraryOrder[A](values: Iterable[A]): Order[A] with UnivEq[A] = {
////    val fixedOrder = values.zipWithIndex.toMap
////    new Order[A] with UnivEq[A] {
////      @inline private[this] def int(s: A) = fixedOrder(s)
////      override def order(a: A, b: A) = Order[Int].order(int(a), int(b))
////    }
////  }
////
////  def setMonoid[A: UnivEq]: Monoid[Set[A]] =
////    new Monoid[Set[A]] {
////      override def zero = Set.empty
////      override def append(a: Set[A], b: => Set[A]) = a | b
////    }
//
//  // Copied from Shapeless
//  trait =:!=[A, B]
//  def _unexpected : Nothing = sys.error("Unexpected invocation")
//  implicit def _neq[A, B] : A =:!= B = null.asInstanceOf[A =:!= B] //new =:!=[A, B] {}
//  implicit def _neqAmbig1[A] : A =:!= A = _unexpected
//  implicit def _neqAmbig2[A] : A =:!= A = _unexpected
//
//  @inline def emptyMap        [K: UnivEq, V]         = Map.empty[K, V]
//  @inline def emptySet        [A: UnivEq]            = Set.empty[A]
//  @inline def emptySetMultimap[K: UnivEq, V: UnivEq] = Multimap.empty[K, Set, V]
//  @inline def emptyMultimap   [K: UnivEq, L[_] : MultiValues, V](implicit ev: L[V] =:!= Set[V]) = Multimap.empty[K, L, V]
//
//  @inline def emptyMutableSet[A: UnivEq] = collection.mutable.Set.empty[A]
//  @inline def setBuilder     [A: UnivEq] = Set.newBuilder[A]
//
//  @inline def toSet[A: UnivEq](as: TraversableOnce[A]): Set[A] = as.toSet
//
//  // ===================================================================================================================
//
//  import scala.reflect.macros.blackbox.Context
//
//  object AutoDerive {
//    implicit def autoDeriveUnivEq[A]: UnivEq[A] = macro UnivEq.MacroImpl.quietDerive[A]
//  }
//
//  class MacroImpl(val c: Context) extends MacroUtils {
//    import c.universe._
//
//    val univEq = c.typeOf[UnivEq[_]]
//
//    def quietDeriveAuto[T: c.WeakTypeTag]: c.Expr[UnivEq[T]] = implDerive(false, true)
//    def debugDeriveAuto[T: c.WeakTypeTag]: c.Expr[UnivEq[T]] = implDerive(true , true)
//    def quietDerive[T: c.WeakTypeTag]: c.Expr[UnivEq[T]] = implDerive(false, false)
//    def debugDerive[T: c.WeakTypeTag]: c.Expr[UnivEq[T]] = implDerive(true , false)
//    def implDerive[T: c.WeakTypeTag](debug: Boolean, auto: Boolean): c.Expr[UnivEq[T]] = {
//      if (debug) println()
//
//      // Fucking macros ignore implicit params in the enclosing method!
//      val eo = c.internal.enclosingOwner
//      val whitelist: Set[Type] =
//        if (eo.isMethod) {
//          val m = eo.asMethod
//          m.paramLists.flatten.collect { case a if a.isImplicit =>
//            a.typeSignature match {
//              case TypeRef(ThisType(_), a, List(inner)) if a.fullName == "japgolly.univeq.UnivEq" => inner
//              case _ => null
//            }
//          }.filter(_ ne null).toSet
//        } else Set.empty
//      if (debug && whitelist.nonEmpty) println("Whitelist: " + whitelist)
//
//      val T = weakTypeOf[T]
//      ensureUnivEq(T, debug, auto,
//        s => whitelist.exists(w => (s <:< w) || (s.toString == w.toString)))
//
//      val impl = q"_root_.japgolly.univeq.UnivEq.force[$T]"
//
//      if (debug) println("\n")// + impl + "\n")
//      c.Expr[UnivEq[T]](impl)
//    }
//
//    def ensureUnivEq(T: Type, debug: Boolean, auto: Boolean, allow: Type => Boolean): Unit = {
//      if (debug) println(s"→ $T")
//      def found(t: Any, p: Any): Unit =
//        if (debug) {
//          printf("%-90s = %s\n", t.toString, p.toString())
//        }
//
//      val t = T.typeSymbol
//      if (t.isType && allow(t.asType.toType))
//        found(t, "implicit arg")
//      else if (t.isClass && t.asClass.isCaseClass) {
//        // Case class
//        ensureConcrete(T)
//        val params = primaryConstructorParams(T)
//        for (p <- params) {
//          val (pn, pt) = nameAndType(T, p)
//          if (debug) println(s"  .$pn: $pt")
//          val u = appliedType(univEq, pt)
//          if (allow(pt))
//            found(pt, "implicit arg")
//          else
//            tryInferImplicit(u) match {
//              case Some(i) => found(pt, i)
//              case None    => fail(s"Implicit not found: $u") //init += q"implicitly[$u]"
//            }
//        }
//      } else
//        // ADT
//        crawlADT(T, p => {
//          val pt = determineAdtType(T, p)
//          if (allow(pt)) {
//            found(pt, "implicit arg")
//            Some(())
//          } else if (p.isModuleClass) {
//            found(pt, "case object")
//            Some(())
//          } else
//            tryInferImplicit(appliedType(univEq, pt)).map(found(pt, _)).orElse {
//              if (auto)
//                Some(ensureUnivEq(pt, debug, auto, allow))
//              else None
//            }
//        }, p => {
//          val pt = p.asType.toType
//          val u = appliedType(univEq, pt)
//          fail(s"Implicit not found: $u")
//          // init += q"implicitly[$u]"
//          // Vector.empty
//        })
//    }
//
//  }
//}