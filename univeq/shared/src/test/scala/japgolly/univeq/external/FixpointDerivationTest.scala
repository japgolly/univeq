package japgolly.univeq.external
// In external package so that univeq._ isn't imported

import japgolly.univeq.UnivEq
import japgolly.univeq.UnivEq.{derive, deriveDebug}
import utest.compileError

object FixpointDerivationTest {
  type Fail = utest.CompileError

  final case class FixCC[F[_]](unfix: F[FixCC[F]])
  object FixCC {
    def ok[F[_]](implicit ev: UnivEq[F[Unit]]): UnivEq[FixCC[F]] = derive[FixCC[F]]
    def ko[F[_]]: Fail = compileError("derive[FixCC[F]]")
  }

  object FixModule {
    import japgolly.microlibs.recursion._

    sealed trait AstAB[+A, +B]
    object AstAB {
      case object CO extends AstAB[Nothing, Nothing]
      final case class C0(i: Int) extends AstAB[Nothing, Nothing]
      final case class CA[+A](a: A) extends AstAB[A, Nothing]
      final case class CB[+B](b: B) extends AstAB[Nothing, B]
    }

    object Ok {
      type AstF[+F] = AstAB[F, Int]
      type Ast = Fix[AstF]
      implicit def univEqAst: UnivEq[Ast] = UnivEq.deriveFix[Fix, AstF]
    }

    object Ko {
      class X
      type AstF[+F] = AstAB[F, X]
      type Ast = Fix[AstF]
      compileError("UnivEq.deriveFix[Fix, AstF]")
    }
  }
}