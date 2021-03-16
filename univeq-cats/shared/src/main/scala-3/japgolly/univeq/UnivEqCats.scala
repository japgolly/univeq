package japgolly.univeq

import cats.data.*
import cats.kernel.*
import UnivEq.*

trait UnivEqCats:

  inline given catsEqFromUnivEq[A](using erased UnivEq[A]): Eq[A] =
    Eq.fromUniversalEquals

  inline given univEqCatsIor  [A, B](using erased UnivEq[A], UnivEq[B]): UnivEq[A Ior B         ] = force
  inline given univEqCatsChain[A]   (using erased UnivEq[A])           : UnivEq[Chain[A]        ] = force
  inline given univEqCatsNec  [A]   (using erased UnivEq[A])           : UnivEq[NonEmptyChain[A]] = force
  inline given univEqCatsNel  [A]   (using erased UnivEq[A])           : UnivEq[NonEmptyList[A] ] = derive

  // NonEmptyVector doesn't implement

  inline given univEqCatsOneAnd[F[_], A](using erased fa: UnivEq[F[A]], a: UnivEq[A]): UnivEq[OneAnd[F, A]] = derive

  given catsMonoidSet[A: UnivEq]: Monoid[Set[A]] =
    new Monoid[Set[A]]:
      override def empty = Set.empty
      override def combine(a: Set[A], b: Set[A]) = a | b

object UnivEqCats extends UnivEqCats
