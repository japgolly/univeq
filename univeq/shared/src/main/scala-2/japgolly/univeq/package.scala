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
}
