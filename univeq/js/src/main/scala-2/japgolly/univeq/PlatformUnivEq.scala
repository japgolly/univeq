package japgolly.univeq

import org.scalajs.dom.Element
import scala.scalajs.js

trait PlatformUnivEq {

  @inline final implicit def univEqJsDomElement[A <: Element]: UnivEq[A]             = UnivEq.force
  @inline final implicit def univEqJsUndefOr   [A: UnivEq]   : UnivEq[js.UndefOr[A]] = UnivEq.force

}
