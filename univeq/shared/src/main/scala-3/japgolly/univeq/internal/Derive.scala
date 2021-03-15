package japgolly.univeq.internal

import japgolly.univeq.UnivEq
import scala.collection.mutable
import scala.deriving.*
import scala.quoted.*

object Derive:

  inline def apply[A]: UnivEq[A] =
    ${ macroImpl[A](false) }

  inline def debug[A]: UnivEq[A] =
    ${ macroImpl[A](true) }

  private def macroImpl[A](debug: Boolean)(using Quotes, Type[A]): Expr[UnivEq[A]] =
    def log(msg: => Any) = if debug then println(msg)
    log("="*120)
    log(s"Beginning derivation of UnivEq[${Type.show[A]}]")
    val seen = mutable.Set.empty[Type[_]]
    var failed = false
    var causes = List.empty[Type[_]]

    def go[B: Type]: Unit =
      val B = Type.of[B]
      log("  - Checking: " + Type.show[B])

      // Check 1: Already processed?
      if seen.contains(B) then return
      seen.add(B)

      // Check 2: Does UnivEq[B] exist?
      if Expr.summon[UnivEq[B]].isDefined then
        log("      ok: found given")
        return

      // Check 3: Is tuple? Check field-by-field
      B match
        case '[h *: t] =>
          log(s"    Splitting tuple...")
          // log(s"    Splitting tuple: ${Type.show[h]} *: ${Type.show[t]}")
          go[h]
          go[t]
          return
        case '[EmptyTuple] =>
          // log(s"  - EmptyTuple")
          return
        case _ =>
          // log(s"  - Not a tuple: ${Type.show[B]}")

      // Check 4: Can we derive via a Mirror?
      Expr.summon[Mirror.Of[B]] match
        case Some('{ $m: Mirror.ProductOf[B] { type MirroredElemTypes = types } }) =>
          log("    Product-type mirror found. Checking fields...")
          go[types]
          return
        case Some('{ $m: Mirror.SumOf[B] { type MirroredElemTypes = types } }) =>
          log("    Sum-type mirror found. Checking cases...")
          go[types]
          return
        case _ =>

      // Give up
      log("      DERIVATION FAILED")
      failed = true
      if B != Type.of[A] then causes ::= B

    end go

    go(Type.of[A])
    // log("Seen:")
    // log(seen.toList.map(t => Type.show(using t)).sorted.map("  - " + _).mkString("\n"))

    if failed then
      var msg = s"Failed to derive UnivEq[${Type.show[A]}]."
      if causes.nonEmpty then
        val causeList = causes.map(t => Type.show(using t)).sorted.map("  - " + _).mkString("\n")
        msg += "\n\nDerivation failed due to the lack of UnivEq instances for the following:\n" + causeList
      msg += "\n\n"
      quotes.reflect.report.throwError(msg)
    else
      log(s"Successfully validated UnivEq[${Type.show[A]}]")
      '{ UnivEq.force[A] }
