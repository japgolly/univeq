package japgolly.univeq.internal

import japgolly.univeq.UnivEq
import scala.collection.mutable
import scala.deriving.*
import scala.quoted.*

object Derivation:

  inline def derive[A]: UnivEq[A] =
    ${ macroImpl[A](false) }

  inline def deriveDebug[A]: UnivEq[A] =
    ${ macroImpl[A](true) }

  private def macroImpl[A](debug: Boolean)(using Quotes, Type[A]): Expr[UnivEq[A]] =
    def log(msg: => Any) = if debug then println(msg)
    log("="*120)
    log(s"Beginning derivation of UnivEq[${Type.show[A]}]")
    val seen = mutable.Set.empty[Type[?]]
    var failed = false
    var causes = List.empty[Type[?]]

    def go[B: Type](fieldParent: Type[?] | Null): Unit =
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
          go[h](fieldParent)
          go[t](fieldParent)
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
          go[types](B)
          return
        case Some('{ $m: Mirror.SumOf[B] { type MirroredElemTypes = types } }) =>
          log("    Sum-type mirror found. Checking cases...")
          go[types](null)
          return
        case _ =>
          // log(s"    No mirror found for ${Type.show[B]}")

      // Check 5: Is fixpoint of self?
      if fieldParent != null then
        import quotes.reflect._
        val P = TypeRepr.of(using fieldParent).dealias
        val b = TypeRepr.of[B].dealias
        b match
          case AppliedType(f@ TypeRef(NoPrefix(), _), `P` :: Nil) =>
            P match
              case AppliedType(_, parentTypeArgs) if parentTypeArgs.contains(f) =>
                // We've found B[P[B]]
                val fUnit = f.appliedTo(TypeRepr.of[Unit])
                val univeqFUnit = TypeRepr.of[UnivEq].appliedTo(fUnit)
                val UniveqFUnit = univeqFUnit.asType.asInstanceOf[Type[Any]]
                log(s"    Found fixpoint, looking for a UnivEq[${f.name}[Unit]]...")
                log("  - Checking: " + Type.show(using fUnit.asType))
                if Expr.summon(using UniveqFUnit).isDefined then
                  log("      ok: found given")
                  return
              case _ =>
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
