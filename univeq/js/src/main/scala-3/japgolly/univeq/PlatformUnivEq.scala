package japgolly.univeq

import org.scalajs.dom.Element
import scala.scalajs.js

trait PlatformUnivEq {

  inline given univEqJsDomElement[A <: Element]: UnivEq[A]             = UnivEq.force
  inline given univEqJsUndefOr   [A: UnivEq]   : UnivEq[js.UndefOr[A]] = UnivEq.force

}
