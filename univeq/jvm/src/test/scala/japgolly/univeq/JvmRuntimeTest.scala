package japgolly.univeq

import utest._

object JvmRuntimeTest extends TestSuite {

  def assertId[A](a: A)(implicit u: UnivEq[A]): Unit =
    assert(u.univEq(a, a))

  def assertPass[A](as: A*)(implicit u: UnivEq[A]): Unit = {
    as foreach (assertId(_))
    if (as.size > 1) {
      val h = as.head
      as.tail.foreach(t => assert(!u.univEq(h, t)))
    }
  }

  override def tests = Tests {
    "enum" - assertPass(JavaEnum.BOB, JavaEnum.LOB, JavaEnum.LAW)
  }
}