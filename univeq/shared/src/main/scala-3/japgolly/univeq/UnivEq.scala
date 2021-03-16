package japgolly.univeq

import java.{lang => jl}
import java.{time => jt}
import java.{util => ju}
import scala.annotation.nowarn
import scala.collection.{immutable => sci}
import scala.concurrent.{duration => sd}

// TODO Replace with an erased class once Scala 3 supports it

/**
 * Universal equality.
 */
final class UnivEq[A]:
  inline def univEq(a: A, b: A): Boolean =
    a == b

@nowarn("cat=deprecation")
object UnivEq extends PlatformUnivEq:

  private val singleton: UnivEq[Any] =
    new UnivEq[Any]

  def force[A]: UnivEq[A] =
    singleton.asInstanceOf[UnivEq[A]]

  inline def apply[A](using proof: UnivEq[A]): UnivEq[A] =
    proof

  // TODO Rename all UnivEq.{[uU]nivEqXxx => xxx} givens

  // Primitives & core
  inline given univEqNothing: UnivEq[Nothing] = force
  inline given UnivEqBoolean: UnivEq[Boolean] = force
  inline given UnivEqByte   : UnivEq[Byte   ] = force
  inline given UnivEqChar   : UnivEq[Char   ] = force
  inline given UnivEqDouble : UnivEq[Double ] = force
  inline given UnivEqFloat  : UnivEq[Float  ] = force
  inline given UnivEqInt    : UnivEq[Int    ] = force
  inline given UnivEqLong   : UnivEq[Long   ] = force
  inline given UnivEqShort  : UnivEq[Short  ] = force
  inline given UnivEqUnit   : UnivEq[Unit   ] = force
  inline given univEqString : UnivEq[String ] = force

  // Tuples
  inline given univEqEmptyTuple                               : UnivEq[EmptyTuple] = force
  inline given univEqTuple     [H: UnivEq, T <: Tuple: UnivEq]: UnivEq[H *: T    ] = force

  // Scala Predef
  inline given univEqBigDecimal                        : UnivEq[BigDecimal  ] = force
  inline given univEqBigInt                            : UnivEq[BigInt      ] = force
  inline given univEqEither    [A: UnivEq, B: UnivEq]  : UnivEq[Either[A, B]] = force
  inline given univEqEitherL   [A: UnivEq, B        ]  : UnivEq[Left  [A, B]] = force
  inline given univEqEitherR   [A        , B: UnivEq]  : UnivEq[Right [A, B]] = force
  inline given univEqEnum      [A <: Enumeration#Value]: UnivEq[A]            = force
  inline given univEqLazyList  [A: UnivEq]             : UnivEq[LazyList [A]] = force
  inline given univEqList      [A: UnivEq]             : UnivEq[List     [A]] = force
  inline given univEqMap       [A: UnivEq, B: UnivEq]  : UnivEq[Map   [A, B]] = force
  inline given univEqNone                              : UnivEq[None.type   ] = force
  inline given univEqOption    [A: UnivEq]             : UnivEq[Option   [A]] = force
  inline given univEqRange                             : UnivEq[Range       ] = force
  inline given univEqSeq       [A: UnivEq]             : UnivEq[Seq      [A]] = force
  inline given univEqSet       [A: UnivEq]             : UnivEq[Set      [A]] = force
  inline given univEqSome      [A: UnivEq]             : UnivEq[Some     [A]] = force
  inline given univEqStream    [A: UnivEq]             : UnivEq[Stream   [A]] = force
  inline given univEqVector    [A: UnivEq]             : UnivEq[Vector   [A]] = force

  // scala.collection.immutable
  inline given univEqSciArraySeq  [A: UnivEq           ]: UnivEq[sci.ArraySeq  [A]   ] = force
  inline given univEqSciBitSet                          : UnivEq[sci.BitSet          ] = force
  inline given univEqSciHashMap   [A: UnivEq, B: UnivEq]: UnivEq[sci.HashMap   [A, B]] = force
  inline given univEqSciHashSet   [A: UnivEq           ]: UnivEq[sci.HashSet   [A]   ] = force
  inline given univEqSciIndexedSeq[A: UnivEq           ]: UnivEq[sci.IndexedSeq[A]   ] = force
  inline given univEqSciIntMap    [A: UnivEq           ]: UnivEq[sci.IntMap    [A]   ] = force
  inline given univEqSciLinearSeq [A: UnivEq           ]: UnivEq[sci.LinearSeq [A]   ] = force
  inline given univEqSciListMap   [A: UnivEq, B: UnivEq]: UnivEq[sci.ListMap   [A, B]] = force
  inline given univEqSciListSet   [A: UnivEq           ]: UnivEq[sci.ListSet   [A]   ] = force
  inline given univEqSciLongMap   [A: UnivEq           ]: UnivEq[sci.LongMap   [A]   ] = force
  inline given univEqSciQueue     [A: UnivEq           ]: UnivEq[sci.Queue     [A]   ] = force
  inline given univEqSciSortedMap [A: UnivEq, B: UnivEq]: UnivEq[sci.SortedMap [A, B]] = force
  inline given univEqSciSortedSet [A: UnivEq           ]: UnivEq[sci.SortedSet [A]   ] = force
  inline given univEqSciTreeMap   [A: UnivEq, B: UnivEq]: UnivEq[sci.TreeMap   [A, B]] = force
  inline given univEqSciTreeSeqMap[A: UnivEq, B: UnivEq]: UnivEq[sci.TreeSeqMap[A, B]] = force
  inline given univEqSciTreeSet   [A: UnivEq           ]: UnivEq[sci.TreeSet   [A]   ] = force
  inline given univEqSciVectorMap [A: UnivEq, B: UnivEq]: UnivEq[sci.VectorMap [A, B]] = force

  // scala.concurrent.duration
  inline given univEqSDuration      : UnivEq[sd.Duration      ] = force
  inline given univEqSFiniteDuration: UnivEq[sd.FiniteDuration] = force

  // Java
  inline given univEqClass  [A]            : UnivEq[Class  [A]] = force
  inline given univEqClass_                : UnivEq[Class  [_]] = force
  inline given univEqJDouble               : UnivEq[jl.Double ] = force
  inline given univEqJFloat                : UnivEq[jl.Float  ] = force
  inline given univEqJInteger              : UnivEq[jl.Integer] = force
  inline given univEqJLong                 : UnivEq[jl.Long   ] = force
  inline given univEqJBoolean              : UnivEq[jl.Boolean] = force
  inline given univEqJByte                 : UnivEq[jl.Byte   ] = force
  inline given univEqJShort                : UnivEq[jl.Short  ] = force
  inline given univEqJEnum[A <: jl.Enum[A]]: UnivEq[A         ] = force
  inline given univEqUUID                  : UnivEq[ju.UUID   ] = force

  // java.time
  inline given univEqJavaTimeDuration      : UnivEq[jt.Duration      ] = force
  inline given univEqJavaTimeInstant       : UnivEq[jt.Instant       ] = force
  inline given univEqJavaTimeLocalDate     : UnivEq[jt.LocalDate     ] = force
  inline given univEqJavaTimeLocalDateTime : UnivEq[jt.LocalDateTime ] = force
  inline given univEqJavaTimeLocalTime     : UnivEq[jt.LocalTime     ] = force
  inline given univEqJavaTimeMonthDay      : UnivEq[jt.MonthDay      ] = force
  inline given univEqJavaTimeOffsetDateTime: UnivEq[jt.OffsetDateTime] = force
  inline given univEqJavaTimeOffsetTime    : UnivEq[jt.OffsetTime    ] = force
  inline given univEqJavaTimePeriod        : UnivEq[jt.Period        ] = force
  inline given univEqJavaTimeYear          : UnivEq[jt.Year          ] = force
  inline given univEqJavaTimeYearMonth     : UnivEq[jt.YearMonth     ] = force
  inline given univEqJavaTimeZonedDateTime : UnivEq[jt.ZonedDateTime ] = force
  inline given univEqJavaTimeZoneId        : UnivEq[jt.ZoneId        ] = force
  inline given univEqJavaTimeZoneOffset    : UnivEq[jt.ZoneOffset    ] = force

  // ===================================================================================================================
  // Derivation

  inline def derived    [A]: UnivEq[A] = internal.Derivation.derive[A]
  inline def derive     [A]: UnivEq[A] = internal.Derivation.derive[A]
  inline def deriveDebug[A]: UnivEq[A] = internal.Derivation.deriveDebug[A]

  @deprecated("No longer required in Scala 3. Use UnivEq.derived", "1.4.0")
  inline def deriveEmpty[A]: UnivEq[A] = derive[A]

  @deprecated("No longer required in Scala 3. Use UnivEq.derivedDebug", "1.4.0")
  inline def deriveEmptyDebug[A]: UnivEq[A] = deriveDebug[A]

  inline def deriveFix[Fix[_[_]], F[_]]: UnivEq[Fix[F]] =
    erased val _ = derive[F[Unit]]
    force

  inline def deriveFixDebug[Fix[_[_]], F[_]]: UnivEq[Fix[F]] =
    erased val _ = deriveDebug[F[Unit]]
    force

  object AutoDerive:
    inline given autoDeriveUnivEq[A <: AnyRef]: UnivEq[A] =
      derived[A]

  // ===================================================================================================================
  // Safe constructors

  inline def emptyMap       [K: UnivEq, V] = Map.empty[K, V]
  inline def emptySet       [A: UnivEq]    = Set.empty[A]
  inline def emptyMutableSet[A: UnivEq]    = collection.mutable.Set.empty[A]
  inline def setBuilder     [A: UnivEq]    = Set.newBuilder[A]

  inline def toSet[A: UnivEq](as: IterableOnce[A]): Set[A] =
    as.iterator.toSet

end UnivEq