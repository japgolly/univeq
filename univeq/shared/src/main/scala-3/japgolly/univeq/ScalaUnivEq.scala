package japgolly.univeq

import scala.annotation.nowarn
import scala.collection.{immutable => sci}
import scala.concurrent.{duration => sd}
import UnivEq.force

@nowarn("cat=deprecation")
trait ScalaUnivEq:

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

  // ===================================================================================================================

  inline def emptyMap       [K: UnivEq, V] = Map.empty[K, V]
  inline def emptySet       [A: UnivEq]    = Set.empty[A]
  inline def emptyMutableSet[A: UnivEq]    = collection.mutable.Set.empty[A]
  inline def setBuilder     [A: UnivEq]    = Set.newBuilder[A]

  inline def toSet[A: UnivEq](as: IterableOnce[A]): Set[A] =
    as.iterator.toSet
