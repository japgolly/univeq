package japgolly.univeq.external
// In external package so that univeq._ isn't imported

import japgolly.univeq.UnivEq

object StdlibTest {

  UnivEq[BigDecimal]
  UnivEq[BigInt]
  UnivEq[Either[Int, Int]]
  UnivEq[Left[Int, Int]]
  UnivEq[Right[Int, Int]]
  UnivEq[Int]
  UnivEq[List[Int]]
  UnivEq[Map[Int, Int]]
  UnivEq[None.type]
  UnivEq[Option[Int]]
  UnivEq[Range]
  UnivEq[Seq[Int]]
  UnivEq[Set[Int]]
  UnivEq[Some[Int]]
  UnivEq[Stream[Int]]
  UnivEq[Vector[Int]]

  UnivEq[scala.collection.immutable.BitSet]
  UnivEq[scala.collection.immutable.HashMap[Int, Int]]
  UnivEq[scala.collection.immutable.HashSet[Int]]
  UnivEq[scala.collection.immutable.IndexedSeq[Int]]
  UnivEq[scala.collection.immutable.IntMap[Int]]
  UnivEq[scala.collection.immutable.LinearSeq[Int]]
  UnivEq[scala.collection.immutable.ListMap[Int, Int]]
  UnivEq[scala.collection.immutable.ListSet[Int]]
  UnivEq[scala.collection.immutable.LongMap[Int]]
  UnivEq[scala.collection.immutable.Queue[Int]]
  UnivEq[scala.collection.immutable.SortedMap[Int, Int]]
  UnivEq[scala.collection.immutable.SortedSet[Int]]
  UnivEq[scala.collection.immutable.TreeMap[Int, Int]]
  UnivEq[scala.collection.immutable.TreeSet[Int]]

  UnivEq[scala.concurrent.duration.Duration]
  UnivEq[scala.concurrent.duration.FiniteDuration]
}