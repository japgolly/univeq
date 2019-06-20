package japgolly.microlibs

package object recursion {

  val Fix: FixModule = FixImpl
  type Fix[F[_]] = Fix.Fix[F]

}
