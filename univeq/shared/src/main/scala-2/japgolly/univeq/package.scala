package japgolly

package object univeq {

  final class UnivEqOps[A](private val a: A) extends AnyVal {

    @inline def ==*[B >: A : UnivEq](b: B): Boolean =
      macro japgolly.univeq.macros.UnivEqMacros.noBoxEq[B]

    @inline def !=*[B >: A : UnivEq](b: B): Boolean =
      macro japgolly.univeq.macros.UnivEqMacros.noBoxNe[B]
  }

  @inline implicit def UnivEqOps[A](a: A): UnivEqOps[A] =
    new UnivEqOps(a)
}
