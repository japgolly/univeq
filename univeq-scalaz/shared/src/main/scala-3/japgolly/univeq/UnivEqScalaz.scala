package japgolly.univeq

import scalaz.*
import UnivEq.*

trait UnivEqScalaz:

  inline implicit def scalazEqualFromUnivEq[A](using inline ev: UnivEq[A]): Equal[A] =
    Equal.equalA

  inline implicit def univEqScalazDisj [A, B](using inline a: UnivEq[A], inline b: UnivEq[B]): UnivEq[A \/ B         ] = derive
  inline implicit def univEqScalazThese[A, B](using inline a: UnivEq[A], inline b: UnivEq[B]): UnivEq[A \&/ B        ] = force
  inline implicit def univEqScalazNel  [A]   (using inline a: UnivEq[A])                     : UnivEq[NonEmptyList[A]] = force

  inline implicit def univEqScalazOneAnd[F[_], A](using inline fa: UnivEq[F[A]], inline a: UnivEq[A]): UnivEq[OneAnd[F, A]] = derive

  inline implicit def scalazMonoidSet[A](using inline ev: UnivEq[A]): Monoid[Set[A]] =
    _scalazMonoidSet

  private[univeq] def _scalazMonoidSet[A]: Monoid[Set[A]] =
    new Monoid[Set[A]]:
      override def zero = Set.empty
      override def append(a: Set[A], b: => Set[A]) = a | b

object UnivEqScalaz extends UnivEqScalaz
