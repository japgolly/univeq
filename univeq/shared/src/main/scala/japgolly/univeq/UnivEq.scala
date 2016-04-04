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

  final val UnivEqAnyRef: UnivEq[AnyRef] =
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
          implicit val univEqString                       : UnivEq[String    ] = force
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
  @inline implicit def univEqTuple10[A:UnivEq, B:UnivEq, C:UnivEq, D:UnivEq, E:UnivEq, F:UnivEq, G:UnivEq, H:UnivEq, I:UnivEq, J:UnivEq]: UnivEq[(A,B,C,D,E,F,G,H,I,J)] = force
  @inline implicit def univEqTuple11[A:UnivEq, B:UnivEq, C:UnivEq, D:UnivEq, E:UnivEq, F:UnivEq, G:UnivEq, H:UnivEq, I:UnivEq, J:UnivEq, K:UnivEq]: UnivEq[(A,B,C,D,E,F,G,H,I,J,K)] = force
  @inline implicit def univEqTuple12[A:UnivEq, B:UnivEq, C:UnivEq, D:UnivEq, E:UnivEq, F:UnivEq, G:UnivEq, H:UnivEq, I:UnivEq, J:UnivEq, K:UnivEq, L:UnivEq]: UnivEq[(A,B,C,D,E,F,G,H,I,J,K,L)] = force
  @inline implicit def univEqTuple13[A:UnivEq, B:UnivEq, C:UnivEq, D:UnivEq, E:UnivEq, F:UnivEq, G:UnivEq, H:UnivEq, I:UnivEq, J:UnivEq, K:UnivEq, L:UnivEq, M:UnivEq]: UnivEq[(A,B,C,D,E,F,G,H,I,J,K,L,M)] = force
  @inline implicit def univEqTuple14[A:UnivEq, B:UnivEq, C:UnivEq, D:UnivEq, E:UnivEq, F:UnivEq, G:UnivEq, H:UnivEq, I:UnivEq, J:UnivEq, K:UnivEq, L:UnivEq, M:UnivEq, N:UnivEq]: UnivEq[(A,B,C,D,E,F,G,H,I,J,K,L,M,N)] = force
  @inline implicit def univEqTuple15[A:UnivEq, B:UnivEq, C:UnivEq, D:UnivEq, E:UnivEq, F:UnivEq, G:UnivEq, H:UnivEq, I:UnivEq, J:UnivEq, K:UnivEq, L:UnivEq, M:UnivEq, N:UnivEq, O:UnivEq]: UnivEq[(A,B,C,D,E,F,G,H,I,J,K,L,M,N,O)] = force
  @inline implicit def univEqTuple16[A:UnivEq, B:UnivEq, C:UnivEq, D:UnivEq, E:UnivEq, F:UnivEq, G:UnivEq, H:UnivEq, I:UnivEq, J:UnivEq, K:UnivEq, L:UnivEq, M:UnivEq, N:UnivEq, O:UnivEq, P:UnivEq]: UnivEq[(A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P)] = force
  @inline implicit def univEqTuple17[A:UnivEq, B:UnivEq, C:UnivEq, D:UnivEq, E:UnivEq, F:UnivEq, G:UnivEq, H:UnivEq, I:UnivEq, J:UnivEq, K:UnivEq, L:UnivEq, M:UnivEq, N:UnivEq, O:UnivEq, P:UnivEq, Q:UnivEq]: UnivEq[(A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q)] = force
  @inline implicit def univEqTuple18[A:UnivEq, B:UnivEq, C:UnivEq, D:UnivEq, E:UnivEq, F:UnivEq, G:UnivEq, H:UnivEq, I:UnivEq, J:UnivEq, K:UnivEq, L:UnivEq, M:UnivEq, N:UnivEq, O:UnivEq, P:UnivEq, Q:UnivEq, R:UnivEq]: UnivEq[(A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R)] = force
  @inline implicit def univEqTuple19[A:UnivEq, B:UnivEq, C:UnivEq, D:UnivEq, E:UnivEq, F:UnivEq, G:UnivEq, H:UnivEq, I:UnivEq, J:UnivEq, K:UnivEq, L:UnivEq, M:UnivEq, N:UnivEq, O:UnivEq, P:UnivEq, Q:UnivEq, R:UnivEq, S:UnivEq]: UnivEq[(A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S)] = force
  @inline implicit def univEqTuple20[A:UnivEq, B:UnivEq, C:UnivEq, D:UnivEq, E:UnivEq, F:UnivEq, G:UnivEq, H:UnivEq, I:UnivEq, J:UnivEq, K:UnivEq, L:UnivEq, M:UnivEq, N:UnivEq, O:UnivEq, P:UnivEq, Q:UnivEq, R:UnivEq, S:UnivEq, T:UnivEq]: UnivEq[(A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T)] = force
  @inline implicit def univEqTuple21[A:UnivEq, B:UnivEq, C:UnivEq, D:UnivEq, E:UnivEq, F:UnivEq, G:UnivEq, H:UnivEq, I:UnivEq, J:UnivEq, K:UnivEq, L:UnivEq, M:UnivEq, N:UnivEq, O:UnivEq, P:UnivEq, Q:UnivEq, R:UnivEq, S:UnivEq, T:UnivEq, U:UnivEq]: UnivEq[(A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U)] = force
  @inline implicit def univEqTuple22[A:UnivEq, B:UnivEq, C:UnivEq, D:UnivEq, E:UnivEq, F:UnivEq, G:UnivEq, H:UnivEq, I:UnivEq, J:UnivEq, K:UnivEq, L:UnivEq, M:UnivEq, N:UnivEq, O:UnivEq, P:UnivEq, Q:UnivEq, R:UnivEq, S:UnivEq, T:UnivEq, U:UnivEq, V:UnivEq]: UnivEq[(A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V)] = force

  // Java
  @inline implicit def univEqJInteger              : UnivEq[jl.Integer] = force
  @inline implicit def univEqJLong                 : UnivEq[jl.Long   ] = force
  @inline implicit def univEqJBoolean              : UnivEq[jl.Boolean] = force
  @inline implicit def univEqJByte                 : UnivEq[jl.Byte   ] = force
  @inline implicit def univEqJShort                : UnivEq[jl.Short  ] = force
  @inline implicit def univEqJEnum[A <: jl.Enum[A]]: UnivEq[jl.Enum[A]] = force

  // Derivation
  @inline def derive            [A <: AnyRef]: UnivEq[A] = macro macros.UnivEqMacros.deriveAutoQuiet[A]
  @inline def deriveDebug       [A <: AnyRef]: UnivEq[A] = macro macros.UnivEqMacros.deriveAutoDebug[A]
//@inline def deriveShallow     [A <: AnyRef]: UnivEq[A] = macro macros.UnivEqMacros.deriveQuiet[A]
//@inline def deriveShallowDebug[A <: AnyRef]: UnivEq[A] = macro macros.UnivEqMacros.deriveDebug[A]

  object AutoDerive {
    @inline implicit def autoDeriveUnivEq[A <: AnyRef]: UnivEq[A] =
      macro macros.UnivEqMacros.deriveQuiet[A]
  }

  // ===================================================================================================================

  @inline def emptyMap       [K: UnivEq, V] = Map.empty[K, V]
  @inline def emptySet       [A: UnivEq]    = Set.empty[A]
  @inline def emptyMutableSet[A: UnivEq]    = collection.mutable.Set.empty[A]
  @inline def setBuilder     [A: UnivEq]    = Set.newBuilder[A]

  @inline def toSet[A: UnivEq](as: TraversableOnce[A]): Set[A] = as.toSet
}
