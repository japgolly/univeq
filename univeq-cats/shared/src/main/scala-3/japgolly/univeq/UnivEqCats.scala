package japgolly.univeq

import cats.data.*
import cats.kernel.*
import UnivEq.*

trait UnivEqCats:

  inline implicit def catsEqFromUnivEq[A](using inline ev: UnivEq[A]): Eq[A] =
    Eq.fromUniversalEquals

  inline implicit def univEqCatsIor  [A, B](using inline a: UnivEq[A], inline b: UnivEq[B]): UnivEq[A Ior B         ] = force
  inline implicit def univEqCatsChain[A]   (using inline a: UnivEq[A])                     : UnivEq[Chain[A]        ] = force
  inline implicit def univEqCatsNec  [A]   (using inline a: UnivEq[A])                     : UnivEq[NonEmptyChain[A]] = force
  inline implicit def univEqCatsNel  [A]   (using inline a: UnivEq[A])                     : UnivEq[NonEmptyList[A] ] = derive

  // NonEmptyVector doesn't implement

  inline implicit def univEqCatsOneAnd[F[_], A](using inline fa: UnivEq[F[A]], inline a: UnivEq[A]): UnivEq[OneAnd[F, A]] = derive

  inline implicit def catsMonoidSet[A](using inline ev: UnivEq[A]): Monoid[Set[A]] =
    _catsMonoidSet

  private[univeq] def _catsMonoidSet[A]: Monoid[Set[A]] =
    new Monoid[Set[A]]:
      override def empty = Set.empty
      override def combine(a: Set[A], b: Set[A]) = a | b

object UnivEqCats extends UnivEqCats
