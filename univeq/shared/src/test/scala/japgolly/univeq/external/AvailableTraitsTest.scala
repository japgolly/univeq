package japgolly.univeq.external

import utest.compileError

object AvailableTraitsTest {

  object TestE extends japgolly.univeq.UnivEqExports {
    val x: UnivEq[Int] = UnivEq[Int]
    val y = 1 ==* 1
    val z = compileError("1 ==* 'A'")
  }

  object TestP extends japgolly.univeq.PlatformUnivEq

  object TestS extends japgolly.univeq.ScalaUnivEq

}
