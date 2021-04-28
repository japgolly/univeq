package japgolly.univeq

import java.{lang => jl}
import java.{time => jt}
import java.{util => ju}

// TODO Replace with an erased class once Scala 3 supports it

/**
 * Universal equality.
 */
final class UnivEq[A]:
  inline def univEq(a: A, b: A): Boolean =
    a.==*(b)(using this)

object UnivEq extends PlatformUnivEq, ScalaUnivEq:

  private val singleton: UnivEq[Any] =
    new UnivEq[Any]

  def force[A]: UnivEq[A] =
    singleton.asInstanceOf[UnivEq[A]]

  inline def apply[A](using inline proof: UnivEq[A]): UnivEq[A] =
    proof

  // TODO Rename all UnivEq.{[uU]nivEqXxx => xxx} givens

  // Primitives & core
  inline implicit def univEqNothing: UnivEq[Nothing] = force
  inline implicit def UnivEqBoolean: UnivEq[Boolean] = force
  inline implicit def UnivEqByte   : UnivEq[Byte   ] = force
  inline implicit def UnivEqChar   : UnivEq[Char   ] = force
  inline implicit def UnivEqDouble : UnivEq[Double ] = force
  inline implicit def UnivEqFloat  : UnivEq[Float  ] = force
  inline implicit def UnivEqInt    : UnivEq[Int    ] = force
  inline implicit def UnivEqLong   : UnivEq[Long   ] = force
  inline implicit def UnivEqShort  : UnivEq[Short  ] = force
  inline implicit def UnivEqUnit   : UnivEq[Unit   ] = force
  inline implicit def univEqString : UnivEq[String ] = force

  // Tuples
  inline implicit def univEqEmptyTuple                               : UnivEq[EmptyTuple] = force
  inline implicit def univEqTuple     [H: UnivEq, T <: Tuple: UnivEq]: UnivEq[H *: T    ] = force

  // Java
  inline implicit def univEqClass  [A]            : UnivEq[Class  [A]] = force
  inline implicit def univEqClass_                : UnivEq[Class  [_]] = force
  inline implicit def univEqJDouble               : UnivEq[jl.Double ] = force
  inline implicit def univEqJFloat                : UnivEq[jl.Float  ] = force
  inline implicit def univEqJInteger              : UnivEq[jl.Integer] = force
  inline implicit def univEqJLong                 : UnivEq[jl.Long   ] = force
  inline implicit def univEqJBoolean              : UnivEq[jl.Boolean] = force
  inline implicit def univEqJByte                 : UnivEq[jl.Byte   ] = force
  inline implicit def univEqJShort                : UnivEq[jl.Short  ] = force
  inline implicit def univEqJEnum[A <: jl.Enum[A]]: UnivEq[A         ] = force
  inline implicit def univEqUUID                  : UnivEq[ju.UUID   ] = force

  // java.time
  inline implicit def univEqJavaTimeDuration      : UnivEq[jt.Duration      ] = force
  inline implicit def univEqJavaTimeInstant       : UnivEq[jt.Instant       ] = force
  inline implicit def univEqJavaTimeLocalDate     : UnivEq[jt.LocalDate     ] = force
  inline implicit def univEqJavaTimeLocalDateTime : UnivEq[jt.LocalDateTime ] = force
  inline implicit def univEqJavaTimeLocalTime     : UnivEq[jt.LocalTime     ] = force
  inline implicit def univEqJavaTimeMonthDay      : UnivEq[jt.MonthDay      ] = force
  inline implicit def univEqJavaTimeOffsetDateTime: UnivEq[jt.OffsetDateTime] = force
  inline implicit def univEqJavaTimeOffsetTime    : UnivEq[jt.OffsetTime    ] = force
  inline implicit def univEqJavaTimePeriod        : UnivEq[jt.Period        ] = force
  inline implicit def univEqJavaTimeYear          : UnivEq[jt.Year          ] = force
  inline implicit def univEqJavaTimeYearMonth     : UnivEq[jt.YearMonth     ] = force
  inline implicit def univEqJavaTimeZonedDateTime : UnivEq[jt.ZonedDateTime ] = force
  inline implicit def univEqJavaTimeZoneId        : UnivEq[jt.ZoneId        ] = force
  inline implicit def univEqJavaTimeZoneOffset    : UnivEq[jt.ZoneOffset    ] = force

  // ===================================================================================================================
  // Derivation

  inline def derived    [A]: UnivEq[A] = internal.Derivation.derive[A]
  inline def derive     [A]: UnivEq[A] = internal.Derivation.derive[A]
  inline def deriveDebug[A]: UnivEq[A] = internal.Derivation.deriveDebug[A]

  @deprecated("No longer required in Scala 3. Use UnivEq.derived", "1.4.0")
  inline def deriveEmpty[A]: UnivEq[A] = derive[A]

  @deprecated("No longer required in Scala 3. Use UnivEq.derivedDebug", "1.4.0")
  inline def deriveEmptyDebug[A]: UnivEq[A] = deriveDebug[A]

  inline def deriveFix[Fix[_[_]], F[_]]: UnivEq[Fix[F]] =
    def unused = derive[F[Unit]]
    force

  inline def deriveFixDebug[Fix[_[_]], F[_]]: UnivEq[Fix[F]] =
    def unused = deriveDebug[F[Unit]]
    force

  object AutoDerive:
    inline implicit def autoDeriveUnivEq[A <: AnyRef]: UnivEq[A] =
      derived[A]

end UnivEq
