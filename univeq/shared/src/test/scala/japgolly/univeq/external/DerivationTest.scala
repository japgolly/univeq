package japgolly.univeq.external
// In external package so that univeq._ isn't imported

import utest.{compileError => NO}
import japgolly.univeq.UnivEq
import UnivEq.{derive, deriveDebug}

object DerivationTest {

  // not a case class
  class NopeI(val i: Int)
  NO("derive[NopeI]")

  // case classes, nope args
  case class CC_X (i: NopeI)           ; NO("derive[CC_X ]")
  case class CC_XS(i: NopeI, s: String); NO("derive[CC_XS]")
  case class CC_SX(s: String, i: NopeI); NO("derive[CC_SX]")

  // Case classes, builtin instances
  case object CO                     ; derive[CO.type]
  case class CC0()                   ; derive[CC0]
  case class CC_I (i: Int)           ; implicit def cc_i: UnivEq[CC_I] = derive
  case class CC_IS(i: Int, s: String); derive[CC_IS]
  case class CC_SI(s: String, i: Int); derive[CC_SI]

  // Custom instances
  case class CC_U (u: CC_I)        ; derive[CC_U ]
  case class CC_IU(i: Int, u: CC_I); derive[CC_IU]
  case class CC_UI(u: CC_I, i: Int); derive[CC_UI]

  // Poly, 0 args
  case class CCP[A]()
  derive[CCP[Int]]
  derive[CCP[NopeI]]

  // Poly, 1 arg
  case class CC_A[A](a: A)
  derive[CC_A[Int]]
  NO("derive[CC_A[NopeI]]")

  // ADT - ok
  object ADT1 {
    sealed trait Blah
    case object Blah1 extends Blah
    case class Blah2(i: Int) extends Blah
    derive[Blah]
  }

  // ADT - not ok
  object ADT2 {
    sealed trait Blah
    case object Blah1 extends Blah
    case class Blah2(i: NopeI) extends Blah
    NO("derive[Blah]")
  }

  // TODO Re-enable after https://github.com/lampepfl/dotty/issues/11765
  // object Xxx {
  //   sealed abstract class Elem[+T, +S]
  //   sealed abstract class Flow[+S] extends Elem[Nothing, S]
  //   case class T[+T](t: T)         extends Elem[T, Nothing]
  //   case class S[+S](s: S)         extends Flow[S]
  //   case class I(i: Int)           extends Flow[Nothing]
  //   def univEq[TT: UnivEq, SS: UnivEq]: UnivEq[Elem[TT, SS]] = UnivEq.derive
  // }
}