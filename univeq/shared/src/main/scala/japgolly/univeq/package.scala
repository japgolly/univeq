package japgolly

package object univeq {

  final class Ops[A](private val a: A) extends AnyVal {
    @inline def ==*[B >: A : UnivEq](b: B): Boolean =
      a == b

    @inline def !=*[B >: A : UnivEq](b: B): Boolean =
      a != b
  }

}
