package japgolly.univeq

import cats._
import cats.data._
import UnivEq._

trait UnivEqCats {

  implicit def catsEqFromUnivEq[A: UnivEq]: Eq[A] =
    Eq.fromUniversalEquals

  @inline implicit def univEqXor[A: UnivEq, B: UnivEq]: UnivEq[A Xor B] = force
  @inline implicit def univEqIor[A: UnivEq, B: UnivEq]: UnivEq[A Ior B] = force

  @inline implicit def univEqOneAnd[F[_], A](implicit fa: UnivEq[F[A]], a: UnivEq[A]): UnivEq[OneAnd[F, A]] = derive
}

object UnivEqCats extends UnivEqExports
