import sbt._
import sbt.Keys._
import org.portablescala.sbtplatformdeps.PlatformDepsPlugin.autoImport._

object Dependencies {

  object Ver {

    // Exported
    val cats       = "2.6.1"
    val scala2     = "2.13.10"
    val scala3     = "3.0.1"
    val scalaJsDom = "2.0.0"

    // Internal
    val utest      = "0.7.10"
  }

  object Dep {
    val cats          = Def.setting("org.typelevel"  %%% "cats-core"      % Ver.cats)
    val scalaCompiler = Def.setting("org.scala-lang"   % "scala-compiler" % scalaVersion.value)
    val scalaJsDom    = Def.setting("org.scala-js"   %%% "scalajs-dom"    % Ver.scalaJsDom)
    val utest         = Def.setting("com.lihaoyi"    %%% "utest"          % Ver.utest)
  }
}
