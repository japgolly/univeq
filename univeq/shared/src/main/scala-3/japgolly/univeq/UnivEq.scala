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
    a == b

object UnivEq extends PlatformUnivEq:

  private val singleton: UnivEq[Any] =
    new UnivEq[Any]

  def force[A]: UnivEq[A] =
    singleton.asInstanceOf[UnivEq[A]]

  inline def apply[A](using proof: UnivEq[A]): UnivEq[A] =
    proof

  // TODO Rename all UnivEq.{[uU]nivEqXxx => xxx} givens

  // Primitives & core
  inline given univEqNothing: UnivEq[Nothing] = force
  inline given UnivEqBoolean: UnivEq[Boolean] = force
  inline given UnivEqByte   : UnivEq[Byte   ] = force
  inline given UnivEqChar   : UnivEq[Char   ] = force
  inline given UnivEqDouble : UnivEq[Double ] = force
  inline given UnivEqFloat  : UnivEq[Float  ] = force
  inline given UnivEqInt    : UnivEq[Int    ] = force
  inline given UnivEqLong   : UnivEq[Long   ] = force
  inline given UnivEqShort  : UnivEq[Short  ] = force
  inline given UnivEqUnit   : UnivEq[Unit   ] = force
  inline given univEqString : UnivEq[String ] = force

  // Java
  inline given univEqClass  [A]            : UnivEq[Class  [A]] = force
  inline given univEqClass_                : UnivEq[Class  [_]] = force
  inline given univEqJDouble               : UnivEq[jl.Double ] = force
  inline given univEqJFloat                : UnivEq[jl.Float  ] = force
  inline given univEqJInteger              : UnivEq[jl.Integer] = force
  inline given univEqJLong                 : UnivEq[jl.Long   ] = force
  inline given univEqJBoolean              : UnivEq[jl.Boolean] = force
  inline given univEqJByte                 : UnivEq[jl.Byte   ] = force
  inline given univEqJShort                : UnivEq[jl.Short  ] = force
  inline given univEqJEnum[A <: jl.Enum[A]]: UnivEq[A         ] = force
  inline given univEqUUID                  : UnivEq[ju.UUID   ] = force

  // java.time
  inline given univEqJavaTimeDuration      : UnivEq[jt.Duration      ] = force
  inline given univEqJavaTimeInstant       : UnivEq[jt.Instant       ] = force
  inline given univEqJavaTimeLocalDate     : UnivEq[jt.LocalDate     ] = force
  inline given univEqJavaTimeLocalDateTime : UnivEq[jt.LocalDateTime ] = force
  inline given univEqJavaTimeLocalTime     : UnivEq[jt.LocalTime     ] = force
  inline given univEqJavaTimeMonthDay      : UnivEq[jt.MonthDay      ] = force
  inline given univEqJavaTimeOffsetDateTime: UnivEq[jt.OffsetDateTime] = force
  inline given univEqJavaTimeOffsetTime    : UnivEq[jt.OffsetTime    ] = force
  inline given univEqJavaTimePeriod        : UnivEq[jt.Period        ] = force
  inline given univEqJavaTimeYear          : UnivEq[jt.Year          ] = force
  inline given univEqJavaTimeYearMonth     : UnivEq[jt.YearMonth     ] = force
  inline given univEqJavaTimeZonedDateTime : UnivEq[jt.ZonedDateTime ] = force
  inline given univEqJavaTimeZoneId        : UnivEq[jt.ZoneId        ] = force
  inline given univEqJavaTimeZoneOffset    : UnivEq[jt.ZoneOffset    ] = force

  // Tuples
  inline given univEqEmptyTuple                               : UnivEq[EmptyTuple] = force
  inline given univEqTuple     [H: UnivEq, T <: Tuple: UnivEq]: UnivEq[H *: T    ] = force

  // ===================================================================================================================
  // Derivation

  inline def derived     [A]: UnivEq[A] = internal.Derive[A]
  inline def derivedDebug[A]: UnivEq[A] = internal.Derive.debug[A]

  @deprecated("Use UnivEq.derived", "1.4.0")
  inline def derive[A]: UnivEq[A] = derived[A]

  @deprecated("Use UnivEq.derivedDebug", "1.4.0")
  inline def deriveDebug[A]: UnivEq[A] = derivedDebug[A]

  @deprecated("No longer required in Scala 3. Use UnivEq.derived", "1.4.0")
  inline def deriveEmpty[A]: UnivEq[A] = derived[A]

  @deprecated("No longer required in Scala 3. Use UnivEq.derivedDebug", "1.4.0")
  inline def deriveEmptyDebug[A]: UnivEq[A] = derivedDebug[A]

//   inline def deriveFix     [Fix[_[_]], F[_]]: UnivEq[Fix[F]] = macro macros.UnivEqMacros.deriveFixQuiet[Fix, F]
//   inline def deriveFixDebug[Fix[_[_]], F[_]]: UnivEq[Fix[F]] = macro macros.UnivEqMacros.deriveFixDebug[Fix, F]

  object AutoDerive:
    inline given autoDeriveUnivEq[A <: AnyRef]: UnivEq[A] =
      derived[A]
