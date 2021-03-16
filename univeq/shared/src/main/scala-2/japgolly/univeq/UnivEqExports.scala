package japgolly.univeq

trait UnivEqExports {
  @inline implicit final def UnivEqOps[A](a: A): UnivEqOps[A] =
    new UnivEqOps(a)

  final type UnivEq[A] = japgolly.univeq.UnivEq[A]
  final val UnivEq = japgolly.univeq.UnivEq
}
