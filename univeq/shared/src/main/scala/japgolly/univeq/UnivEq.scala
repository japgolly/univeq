package japgolly.univeq

import java.{lang => jl}
import scala.collection.immutable.ListSet

/**
 * Universal equality.
 */
final class UnivEq[A] private[univeq](val equal: (A, A) => Boolean) extends AnyVal

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
    UnivEqAnyRef.asInstanceOf[UnivEq[A]]

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

  // Derivation
  @inline def derive              [T <: AnyRef]: UnivEq[T] = macro macros.UnivEqMacros.deriveAutoQuiet[T]
  @inline def deriveVerbose       [T <: AnyRef]: UnivEq[T] = macro macros.UnivEqMacros.deriveAutoDebug[T]
//  @inline def deriveShallow       [A <: AnyRef]: UnivEq[A] = macro macros.UnivEqMacros.deriveQuiet[A]
//  @inline def deriveShallowVerbose[A <: AnyRef]: UnivEq[A] = macro macros.UnivEqMacros.deriveDebug[A]

  object AutoDerive {
    @inline implicit def autoDeriveUnivEq[A <: AnyRef]: UnivEq[A] =
      macro macros.UnivEqMacros.deriveQuiet[A]
  }
}


//  @inline implicit def univEqDisj   [A: UnivEq, B: UnivEq]: UnivEq[A \/ B]          = univEqForce
//  @inline implicit def univEqThese  [A: UnivEq, B: UnivEq]: UnivEq[A \&/ B]         = univEqForce
//  @inline implicit def univEqNel    [A: UnivEq]           : UnivEq[NonEmptyList[A]] = univEqForce
//  @inline implicit def univEqOneAnd[F[_], A](implicit fa: UnivEq[F[A]], a: UnivEq[A]): UnivEq[OneAnd[F, A]] = univEqForce

//  @inline implicit def univEqMultimap[K, L[_], V](implicit ev: UnivEq[Map[K, L[V]]]): UnivEq[Multimap[K, L, V]] = univEqForce

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
