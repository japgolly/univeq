package japgolly.univeq

import scala.collection.{immutable => sci}
import scala.concurrent.{duration => sd}
import scala.collection._
import UnivEq.force

trait ScalaUnivEq {

  // Scala Predef
  @inline implicit def univEqBigDecimal                        : UnivEq[BigDecimal  ] = force
  @inline implicit def univEqBigInt                            : UnivEq[BigInt      ] = force
  @inline implicit def univEqEither    [A: UnivEq, B: UnivEq]  : UnivEq[Either[A, B]] = force
  @inline implicit def univEqEitherL   [A: UnivEq, B        ]  : UnivEq[Left  [A, B]] = force
  @inline implicit def univEqEitherR   [A        , B: UnivEq]  : UnivEq[Right [A, B]] = force
  @inline implicit def univEqEnum      [A <: Enumeration#Value]: UnivEq[A]            = force
  @inline implicit def univEqLazyList  [A: UnivEq]             : UnivEq[LazyList [A]] = force
  @inline implicit def univEqList      [A: UnivEq]             : UnivEq[List     [A]] = force
  @inline implicit def univEqMap       [A: UnivEq, B: UnivEq]  : UnivEq[Map   [A, B]] = force
  @inline implicit def univEqNone                              : UnivEq[None.type   ] = force
  @inline implicit def univEqOption    [A: UnivEq]             : UnivEq[Option   [A]] = force
  @inline implicit def univEqRange                             : UnivEq[Range       ] = force
  @inline implicit def univEqSeq       [A: UnivEq]             : UnivEq[Seq      [A]] = force
  @inline implicit def univEqSet       [A: UnivEq]             : UnivEq[Set      [A]] = force
  @inline implicit def univEqSome      [A: UnivEq]             : UnivEq[Some     [A]] = force
  @inline implicit def univEqStream    [A: UnivEq]             : UnivEq[Stream   [A]] = force
  @inline implicit def univEqVector    [A: UnivEq]             : UnivEq[Vector   [A]] = force

  // scala.collection.immutable
  @inline implicit def univEqSciArraySeq  [A: UnivEq           ]: UnivEq[sci.ArraySeq  [A]   ] = force
  @inline implicit def univEqSciBitSet    [A: UnivEq           ]: UnivEq[sci.BitSet          ] = force
  @inline implicit def univEqSciHashMap   [A: UnivEq, B: UnivEq]: UnivEq[sci.HashMap   [A, B]] = force
  @inline implicit def univEqSciHashSet   [A: UnivEq           ]: UnivEq[sci.HashSet   [A]   ] = force
  @inline implicit def univEqSciIndexedSeq[A: UnivEq           ]: UnivEq[sci.IndexedSeq[A]   ] = force
  @inline implicit def univEqSciIntMap    [A: UnivEq           ]: UnivEq[sci.IntMap    [A]   ] = force
  @inline implicit def univEqSciLinearSeq [A: UnivEq           ]: UnivEq[sci.LinearSeq [A]   ] = force
  @inline implicit def univEqSciListMap   [A: UnivEq, B: UnivEq]: UnivEq[sci.ListMap   [A, B]] = force
  @inline implicit def univEqSciListSet   [A: UnivEq           ]: UnivEq[sci.ListSet   [A]   ] = force
  @inline implicit def univEqSciLongMap   [A: UnivEq           ]: UnivEq[sci.LongMap   [A]   ] = force
  @inline implicit def univEqSciMap       [A: UnivEq, B: UnivEq]: UnivEq[sci.Map       [A, B]] = force
  @inline implicit def univEqSciQueue     [A: UnivEq           ]: UnivEq[sci.Queue     [A]   ] = force
  @inline implicit def univEqSciSortedMap [A: UnivEq, B: UnivEq]: UnivEq[sci.SortedMap [A, B]] = force
  @inline implicit def univEqSciSortedSet [A: UnivEq           ]: UnivEq[sci.SortedSet [A]   ] = force
  @inline implicit def univEqSciTreeMap   [A: UnivEq, B: UnivEq]: UnivEq[sci.TreeMap   [A, B]] = force
  @inline implicit def univEqSciTreeSeqMap[A: UnivEq, B: UnivEq]: UnivEq[sci.TreeSeqMap[A, B]] = force
  @inline implicit def univEqSciTreeSet   [A: UnivEq           ]: UnivEq[sci.TreeSet   [A]   ] = force
  @inline implicit def univEqSciVectorMap [A: UnivEq, B: UnivEq]: UnivEq[sci.VectorMap [A, B]] = force

  // scala.concurrent.duration
  @inline implicit def univEqSDuration      : UnivEq[sd.Duration      ] = force
  @inline implicit def univEqSFiniteDuration: UnivEq[sd.FiniteDuration] = force

  // ===================================================================================================================

  @inline def emptyMap       [K: UnivEq, V] = Map.empty[K, V]
  @inline def emptySet       [A: UnivEq]    = Set.empty[A]
  @inline def emptyMutableSet[A: UnivEq]    = collection.mutable.Set.empty[A]
  @inline def setBuilder     [A: UnivEq]    = Set.newBuilder[A]

  @inline def toSet[A: UnivEq](as: IterableOnce[A]): Set[A] = as.iterator.toSet
}
