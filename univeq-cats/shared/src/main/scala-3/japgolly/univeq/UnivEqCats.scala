package japgolly.univeq

import cats.data.*
import cats.kernel.*
import UnivEq.*

trait UnivEqCats:

  inline given catsEqFromUnivEq[A](using a: => UnivEq[A]): Eq[A] =
    Eq.fromUniversalEquals

  inline given univEqCatsIor  [A, B](using a: => UnivEq[A], b: => UnivEq[B]): UnivEq[A Ior B         ] = force
  inline given univEqCatsChain[A]   (using a: => UnivEq[A])                 : UnivEq[Chain[A]        ] = force
  inline given univEqCatsNec  [A]   (using a: => UnivEq[A])                 : UnivEq[NonEmptyChain[A]] = force
  inline given univEqCatsNel  [A]   (using a: => UnivEq[A])                 : UnivEq[NonEmptyList[A] ] = derive

  // NonEmptyVector doesn't implement

  inline given univEqCatsOneAnd[F[_], A](using fa: => UnivEq[F[A]], a: => UnivEq[A]): UnivEq[OneAnd[F, A]] = derive

  given catsMonoidSet[A](using ev: => UnivEq[A]): Monoid[Set[A]] =
    new Monoid[Set[A]]:
      override def empty = Set.empty
      override def combine(a: Set[A], b: Set[A]) = a | b

object UnivEqCats extends UnivEqCats
