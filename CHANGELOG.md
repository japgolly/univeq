# 1.3.0

* Upgrade deps
* Drop Scala.JS 0.6 support

# 1.2.1

* Upgrade deps
* Scala.JS 1.0 support

# 1.2.0

* Upgrade Cats to 2.1.0
* Add Scala.JS 1.0.0-RC2 support

# 1.1.0

* Add Scala 2.13 support
* Drop Scala 2.11 support
* Upgrade Cats to 2.0.0
* Upgrade Scalaz to 7.2.30
* Upgrade Scala.JS to 0.6.31
* Upgrade scalajs-dom to 0.9.8

# 1.0.8

* Add `UnivEq[Nothing]` instance

# 1.0.7

* Add `UnivEq.deriveFix{,Debug}`
* Allow `UnivEq.derive` to derive fixpoint classes

# 1.0.6

* Fix derivation of hierarchies that include abstract middles.
* Upgrade all dependencies.

# 1.0.5

* Fix `UnivEqCats` and `UnivEqScalaz` objects extending the wrong traits
* Upgrade versions
* Use cats-data instead of cats-kernel and add instances for cats-data types

# 1.0.4

Unbreak backwards-compatibility

# 1.0.3

* Add instances for:
  * `BigDecimal`
  * `BigInt`
  * `Double`
  * `Float`
  * `None`
  * `Range`
  * `Some`
  * `java.lang.Double`
  * `java.lang.Float`
  * `java.time.Duration`
  * `java.time.Instant`
  * `java.time.LocalDate`
  * `java.time.LocalDateTime`
  * `java.time.LocalTime`
  * `java.time.MonthDay`
  * `java.time.OffsetDateTime`
  * `java.time.OffsetTime`
  * `java.time.Period`
  * `java.time.Year`
  * `java.time.YearMonth`
  * `java.time.ZonedDateTime`
  * `java.time.ZoneId`
  * `java.time.ZoneOffset`
  * `scala.collection.immutable.BitSet`
  * `scala.collection.immutable.HashMap`
  * `scala.collection.immutable.HashSet`
  * `scala.collection.immutable.IntMap`
  * `scala.collection.immutable.ListMap`
  * `scala.collection.immutable.LongMap`
  * `scala.collection.immutable.SortedMap`
  * `scala.collection.immutable.SortedSet`
  * `scala.collection.immutable.TreeMap`
  * `scala.collection.immutable.TreeSet`
  * `scala.concurrent.duration.Duration`
  * `scala.concurrent.duration.FiniteDuration`
* Add JS instances for:
  * `org.scalajs.dom.Element`
  * `scalajs.js.UndefOr`
* Upgrade all dependencies

# 1.0.2

* Upgrade all dependencies.
* Publish for Scala 2.12.0.
* Add instance for scala.Enumeration.

# 1.0.1

* Add instance for scala.Either
* Upgrade Cats to 0.6.0


# 1.0

Open-sourced.

