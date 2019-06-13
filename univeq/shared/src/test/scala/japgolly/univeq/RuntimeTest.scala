package japgolly.univeq

import utest._

object RuntimeTest extends TestSuite {

  def assertExists[A: UnivEq]: Unit =
    ()

  def assertId[A](a: A)(implicit u: UnivEq[A]): Unit =
    assert(u.univEq(a, a))

  def assertPass[A](as: A*)(implicit u: UnivEq[A]): Unit = {
    as foreach (assertId(_))
    if (as.size > 1) {
      val h = as.head
      as.tail.foreach(t => assert(!u.univEq(h, t)))
    }
  }

  def assertFail[A](implicit u: UnivEq[A] = null.asInstanceOf[UnivEq[A]]): Unit =
    assert(u == null)

  case class Wrap[A](a: A)
  implicit def univEqWrap[A: UnivEq]: UnivEq[Wrap[A]] = UnivEq.derive

  case class I(i: Int)

  val clsI = classOf[Int]
  val clsL = classOf[Long]

  object Colours extends Enumeration {
    val Red, Amber, Green = Value
  }

  override def tests = Tests {
    "scala" - {
      "unit"    - assertId(())

      "long"    - assertPass[Long   ](4L, 5L)
      "int"     - assertPass[Int    ](3, 5)
      "short"   - assertPass[Short  ](2, 7)
      "byte"    - assertPass[Byte   ](1, 3)

      "char"    - assertPass('a', 'H')
      "boolean" - assertPass(true, false)
      "string"  - assertPass("x", "")

      "option"  - assertPass[Option[Int]](Some(2), None, Some(4))

      "enum"    - assertPass(Colours.Amber, Colours.Green, Colours.Red)
    }

    "java" - {
      "class" - {
        compileError("clsI ==* clsL")
        assert(clsI == clsI)
        assertId(clsL)
      }

      "class_" - assertPass[Class[_]](clsI, clsL)
    }

    "derived" - assertPass(Wrap(2), Wrap(3))

    "ops" - {
      compileError("3 ==* false")
      assert(3 ==* 3)
      assert(3 !=* 4)
    }

    "force" - {
      def test[A](a: A, b: A): Unit = {
        implicit def f = UnivEq.force[A]
        assert(a ==* a)
        assert(a !=* b)
      }
      "anyref" - test(I(1), I(2))
      "int"    - test(1, 2)
      "bool"   - test(true, false)
    }
  }
}