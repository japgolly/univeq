package japgolly.univeq

import cats.data._
import cats.kernel._
import japgolly.univeq.UnivEq._

trait UnivEqCats {

  implicit def catsEqFromUnivEq[A: UnivEq]: Eq[A] =
    Eq.fromUniversalEquals

  @inline implicit def univEqCatsIor  [A: UnivEq, B: UnivEq]: UnivEq[A Ior B         ] = force
  @inline implicit def univEqCatsChain[A: UnivEq]           : UnivEq[Chain[A]        ] = force
  @inline implicit def univEqCatsNec  [A: UnivEq]           : UnivEq[NonEmptyChain[A]] = force
  @inline implicit def univEqCatsNel  [A: UnivEq]           : UnivEq[NonEmptyList[A] ] = derive

  // NonEmptyVector doesn't implement

  @inline implicit def univEqCatsOneAnd[F[_], A](implicit fa: UnivEq[F[A]], a: UnivEq[A]): UnivEq[OneAnd[F, A]] = derive

  def catsMonoidSet[A: UnivEq]: Monoid[Set[A]] =
    new Monoid[Set[A]] {
      override def empty = Set.empty
      override def combine(a: Set[A], b: Set[A]) = a | b
    }
}

object UnivEqCats extends UnivEqCats
