package japgolly.univeq

import java.{lang => jl}
import java.{time => jt}
import scala.collection.{immutable => sci}
import scala.concurrent.{duration => sd}

/**
 * Universal equality.
 */
trait UnivEq[@specialized A] {
  @inline final def univEq(a: A, b: A): Boolean =
    a == b
}

object UnivEq {

  @inline def apply[A](implicit proof: UnivEq[A]): UnivEq[A] =
    proof

  final val UnivEqAnyRef: UnivEq[AnyRef] =
    new UnivEq[AnyRef] {}

  def force[A]: UnivEq[A] =
    UnivEqAnyRef.asInstanceOf[UnivEq[A]]

  // Primitives & core
  implicit val univEqUnit   : UnivEq[Unit   ] = new UnivEq[Unit   ]{}
  implicit val univEqChar   : UnivEq[Char   ] = new UnivEq[Char   ]{}
  implicit val univEqShort  : UnivEq[Short  ] = new UnivEq[Short  ]{}
  implicit val univEqByte   : UnivEq[Byte   ] = new UnivEq[Byte   ]{}
  implicit val univEqLong   : UnivEq[Long   ] = new UnivEq[Long   ]{}
  implicit val univEqInt    : UnivEq[Int    ] = new UnivEq[Int    ]{}
  implicit def univEqFloat  : UnivEq[Float  ] = new UnivEq[Float  ]{}
  implicit val univEqDouble : UnivEq[Double ] = new UnivEq[Double ]{}
  implicit val univEqBoolean: UnivEq[Boolean] = new UnivEq[Boolean]{}
  @inline implicit def univEqString: UnivEq[String] = force

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

  // Scala Predef
  @inline implicit def univEqBigDecimal                        : UnivEq[BigDecimal  ] = force
  @inline implicit def univEqBigInt                            : UnivEq[BigInt      ] = force
  @inline implicit def univEqEither    [A: UnivEq, B: UnivEq]  : UnivEq[Either[A, B]] = force
  @inline implicit def univEqEitherL   [A: UnivEq, B        ]  : UnivEq[Left  [A, B]] = force
  @inline implicit def univEqEitherR   [A        , B: UnivEq]  : UnivEq[Right [A, B]] = force
  @inline implicit def univEqEnum      [A <: Enumeration#Value]: UnivEq[A]            = force
  @inline implicit def univEqList      [A: UnivEq]             : UnivEq[List     [A]] = force
  @inline implicit def univEqMap       [A: UnivEq, B: UnivEq]  : UnivEq[Map   [A, B]] = force
  @inline implicit def univEqNone                              : UnivEq[None.type   ] = force
  @inline implicit def univEqOption    [A: UnivEq]             : UnivEq[Option   [A]] = force
  @inline implicit def univEqRange                             : UnivEq[Range       ] = force
  @inline implicit def univEqSet       [A: UnivEq]             : UnivEq[Set      [A]] = force
  @inline implicit def univEqSome      [A: UnivEq]             : UnivEq[Some     [A]] = force
  @inline implicit def univEqStream    [A: UnivEq]             : UnivEq[Stream   [A]] = force
  @inline implicit def univEqVector    [A: UnivEq]             : UnivEq[Vector   [A]] = force

  // scala.collection.immutable
  @inline implicit def univEqSciBitSet   [A: UnivEq           ]: UnivEq[sci.BitSet         ] = force
  @inline implicit def univEqSciHashMap  [A: UnivEq, B: UnivEq]: UnivEq[sci.HashMap  [A, B]] = force
  @inline implicit def univEqSciHashSet  [A: UnivEq           ]: UnivEq[sci.HashSet  [A]   ] = force
  @inline implicit def univEqSciIntMap   [A: UnivEq           ]: UnivEq[sci.IntMap   [A]   ] = force
  @inline implicit def univEqSciListMap  [A: UnivEq, B: UnivEq]: UnivEq[sci.ListMap  [A, B]] = force
  @inline implicit def univEqSciListSet  [A: UnivEq           ]: UnivEq[sci.ListSet  [A]   ] = force
  @inline implicit def univEqSciLongMap  [A: UnivEq           ]: UnivEq[sci.LongMap  [A]   ] = force
  @inline implicit def univEqSciSortedMap[A: UnivEq, B: UnivEq]: UnivEq[sci.SortedMap[A, B]] = force
  @inline implicit def univEqSciSortedSet[A: UnivEq           ]: UnivEq[sci.SortedSet[A]   ] = force
  @inline implicit def univEqSciTreeMap  [A: UnivEq, B: UnivEq]: UnivEq[sci.TreeMap  [A, B]] = force
  @inline implicit def univEqSciTreeSet  [A: UnivEq           ]: UnivEq[sci.TreeSet  [A]   ] = force

  // scala.concurrent.duration
  @inline implicit def univEqSDuration      : UnivEq[sd.Duration      ] = force
  @inline implicit def univEqSFiniteDuration: UnivEq[sd.FiniteDuration] = force

  // Java
  @inline implicit def univEqClass  [A]            : UnivEq[Class  [A]] = force
  @inline implicit def univEqClass_                : UnivEq[Class  [_]] = force
  @inline implicit def univEqJDouble               : UnivEq[jl.Double ] = force
  @inline implicit def univEqJFloat                : UnivEq[jl.Float  ] = force
  @inline implicit def univEqJInteger              : UnivEq[jl.Integer] = force
  @inline implicit def univEqJLong                 : UnivEq[jl.Long   ] = force
  @inline implicit def univEqJBoolean              : UnivEq[jl.Boolean] = force
  @inline implicit def univEqJByte                 : UnivEq[jl.Byte   ] = force
  @inline implicit def univEqJShort                : UnivEq[jl.Short  ] = force
  @inline implicit def univEqJEnum[A <: jl.Enum[A]]: UnivEq[A         ] = force

  // java.time
  @inline implicit def univEqJavaTimeDuration      : UnivEq[jt.Duration      ] = force
  @inline implicit def univEqJavaTimeInstant       : UnivEq[jt.Instant       ] = force
  @inline implicit def univEqJavaTimeLocalDate     : UnivEq[jt.LocalDate     ] = force
  @inline implicit def univEqJavaTimeLocalDateTime : UnivEq[jt.LocalDateTime ] = force
  @inline implicit def univEqJavaTimeLocalTime     : UnivEq[jt.LocalTime     ] = force
  @inline implicit def univEqJavaTimeMonthDay      : UnivEq[jt.MonthDay      ] = force
  @inline implicit def univEqJavaTimeOffsetDateTime: UnivEq[jt.OffsetDateTime] = force
  @inline implicit def univEqJavaTimeOffsetTime    : UnivEq[jt.OffsetTime    ] = force
  @inline implicit def univEqJavaTimePeriod        : UnivEq[jt.Period        ] = force
  @inline implicit def univEqJavaTimeYear          : UnivEq[jt.Year          ] = force
  @inline implicit def univEqJavaTimeYearMonth     : UnivEq[jt.YearMonth     ] = force
  @inline implicit def univEqJavaTimeZonedDateTime : UnivEq[jt.ZonedDateTime ] = force
  @inline implicit def univEqJavaTimeZoneId        : UnivEq[jt.ZoneId        ] = force
  @inline implicit def univEqJavaTimeZoneOffset    : UnivEq[jt.ZoneOffset    ] = force

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
