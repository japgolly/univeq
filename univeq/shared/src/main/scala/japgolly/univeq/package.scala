package japgolly

package object univeq {

  final class UnivEqOps[A](private val a: A) extends AnyVal {
    @inline def ==*[B >: A : UnivEq](b: B): Boolean =
      a == b

    @inline def !=*[B >: A : UnivEq](b: B): Boolean =
      a != b
  }

  @inline implicit def UnivEqOps[A](a: A): UnivEqOps[A] =
    new UnivEqOps(a)

  // ============================================================================

  trait UnivEqExports {
    @inline implicit def UnivEqOps[A](a: A): UnivEqOps[A] =
      new UnivEqOps(a)

    type UnivEq[A] = japgolly.univeq.UnivEq[A]
    val UnivEq = japgolly.univeq.UnivEq
  }
}
