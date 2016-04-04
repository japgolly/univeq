package japgolly.univeq

import utest._

object RuntimeTest extends TestSuite {

  def assertExists[A: UnivEq]: Unit =
    ()

  def assertId[A](a: A)(implicit u: UnivEq[A]): Unit =
    assert(u.equal(a, a))

  def assertPass[A](as: A*)(implicit u: UnivEq[A]): Unit = {
    as foreach (assertId(_))
    if (as.size > 1) {
      val h = as.head
      as.tail.foreach(t => assert(!u.equal(h, t)))
    }
  }

  def assertFail[A](implicit u: UnivEq[A] = null.asInstanceOf[UnivEq[A]]): Unit =
    assert(u == null)

  case class Wrap[A](a: A)
  implicit def univEqWrap[A: UnivEq]: UnivEq[Wrap[A]] = UnivEq.derive

  override def tests = TestSuite {
    'scala {
      'unit    - assertId(())

      'long    - assertPass[Long   ](4L, 5L)
      'int     - assertPass[Int    ](3, 5)
      'short   - assertPass[Short  ](2, 7)
      'byte    - assertPass[Byte   ](1, 3)

      'char    - assertPass('a', 'H')
      'boolean - assertPass(true, false)
      'string  - assertPass("x", "")

      'option  - assertPass[Option[Int]](Some(2), None, Some(4))
    }

    'derived - assertPass(Wrap(2), Wrap(3))

    'ops {
      compileError("3 ==* false")
      assert(3 ==* 3)
      assert(3 !=* 4)
    }
  }
}