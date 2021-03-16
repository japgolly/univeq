package japgolly.univeq

import scalaz.*
import UnivEq.*

trait UnivEqScalaz:

  inline given scalazEqualFromUnivEq[A](using erased UnivEq[A]): Equal[A] =
    Equal.equalA

  inline given univEqScalazDisj [A, B](using erased UnivEq[A], UnivEq[B]): UnivEq[A \/ B         ] = derive
  inline given univEqScalazThese[A, B](using erased UnivEq[A], UnivEq[B]): UnivEq[A \&/ B        ] = force
  inline given univEqScalazNel  [A]   (using erased UnivEq[A])           : UnivEq[NonEmptyList[A]] = force

  inline given univEqScalazOneAnd[F[_], A](using erased fa: UnivEq[F[A]], a: UnivEq[A]): UnivEq[OneAnd[F, A]] = derive

  given scalazMonoidSet[A](using erased UnivEq[A]): Monoid[Set[A]] =
    new Monoid[Set[A]]:
      override def zero = Set.empty
      override def append(a: Set[A], b: => Set[A]) = a | b

object UnivEqScalaz extends UnivEqScalaz
