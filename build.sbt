ThisBuild / homepage     := Some(url("https://github.com/japgolly/univeq"))
ThisBuild / licenses     := ("Apache-2.0", url("http://opensource.org/licenses/Apache-2.0")) :: Nil
ThisBuild / organization := "com.github.japgolly.univeq"
ThisBuild / shellPrompt  := ((s: State) => Project.extract(s).currentRef.project + "> ")
ThisBuild / startYear    := Some(2015)

lazy val root      = UnivEqBuild.root
lazy val univEqJVM = UnivEqBuild.univEqJVM
lazy val univEqJS  = UnivEqBuild.univEqJS
lazy val catsJVM   = UnivEqBuild.catsJVM
lazy val catsJS    = UnivEqBuild.catsJS
