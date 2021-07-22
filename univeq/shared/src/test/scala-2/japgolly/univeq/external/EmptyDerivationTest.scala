package japgolly.univeq.external
// In external package so that univeq._ isn't imported

import japgolly.univeq.UnivEq
import japgolly.univeq.UnivEq.{deriveEmpty, deriveEmptyDebug}
import utest.{compileError => NO}

object EmptyDerivationTest {

  // Traits

  trait T0
  NO("deriveEmpty[T0]") // not sealed

  sealed trait T1
  deriveEmpty[T1]

  sealed trait T2
  case object T2a extends T2
  NO("deriveEmpty[T2]") // T2a is concrete
  NO("deriveEmpty[T2a.type]") // T2a is concrete

  sealed trait T3
  sealed trait T3a extends T3
  deriveEmpty[T3]
  deriveEmpty[T3a]

  sealed trait T4
  class T4a extends T4
  NO("deriveEmpty[T4]") // T4a is concrete
  NO("deriveEmpty[T4a]") // T4a is concrete

  sealed trait T5
  abstract class T5a extends T5
  NO("deriveEmpty[T5]") // T5a is not sealed

  sealed trait T6[A]
  deriveEmpty[T6[Int]]

  // Classes

  class C1
  NO("deriveEmpty[C1]") // not abstract

  abstract class C2
  NO("deriveEmpty[C2]") // not sealed

  sealed abstract class C3
  deriveEmpty[C3]

  sealed abstract class C4
  sealed abstract class C4a extends C4
  deriveEmpty[C4]
  deriveEmpty[C4a]

  sealed abstract class C5[A]
  deriveEmpty[C5[Int]]

  abstract class C6
  case class C6a() extends C6
  NO("deriveEmpty[C6]") // C6a is concrete

}