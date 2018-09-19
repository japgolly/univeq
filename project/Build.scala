import sbt._
import sbt.Keys._
import com.typesafe.sbt.pgp.PgpKeys
import org.portablescala.sbtplatformdeps.PlatformDepsPlugin.autoImport._
import org.scalajs.jsenv.jsdomnodejs.JSDOMNodeJSEnv
import org.scalajs.sbtplugin.ScalaJSPlugin
import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport.{crossProject => _, CrossType => _, _}
import sbtcrossproject.CrossPlugin.autoImport._
import sbtrelease.ReleasePlugin.autoImport._
import scalajscrossproject.ScalaJSCrossPlugin.autoImport._
import Lib._

object UnivEqBuild {

  private val ghProject = "univeq"

  private val publicationSettings =
    Lib.publicationSettings(ghProject)

  object Ver {
    final val Cats     = "1.4.0"
    final val MTest    = "0.6.5"
    final val Scala211 = "2.11.12"
    final val Scala212 = "2.12.6"
    final val Scalaz   = "7.2.26"
  }

  def scalacFlags = Seq(
    "-deprecation",
    "-unchecked",
    "-Ywarn-dead-code",
    "-Ywarn-value-discard",
    "-feature",
    "-language:postfixOps",
    "-language:implicitConversions",
    "-language:higherKinds",
    "-language:existentials")

  val commonSettings = ConfigureBoth(
    _.settings(
      organization                  := "com.github.japgolly.univeq",
      homepage                      := Some(url("https://github.com/japgolly/" + ghProject)),
      licenses                      += ("Apache-2.0", url("http://opensource.org/licenses/Apache-2.0")),
      startYear                     := Some(2015),
      scalaVersion                  := Ver.Scala212,
      crossScalaVersions            := Seq(Ver.Scala211, Ver.Scala212),
      scalacOptions                ++= scalacFlags,
      scalacOptions in Test        --= Seq("-Ywarn-dead-code"),
      shellPrompt in ThisBuild      := ((s: State) => Project.extract(s).currentRef.project + "> "),
      triggeredMessage              := Watched.clearWhenTriggered,
      incOptions                    := incOptions.value,
      updateOptions                 := updateOptions.value.withCachedResolution(true),
      releasePublishArtifactsAction := PgpKeys.publishSigned.value,
      releaseTagComment             := s"v${(version in ThisBuild).value}",
      releaseVcsSign                := true)
    .configure(
      addCommandAliases(
        "/"   -> "project root",
        "L"   -> "root/publishLocal",
        "C"   -> "root/clean",
        "T"   -> ";root/clean;root/test",
        "TL"  -> ";T;L",
        "c"   -> "compile",
        "tc"  -> "test:compile",
        "t"   -> "test",
        "to"  -> "test-only",
        "cc"  -> ";clean;compile",
        "ctc" -> ";clean;test:compile",
        "ct"  -> ";clean;test")))

  def definesMacros: Project => Project =
    _.settings(
      scalacOptions += "-language:experimental.macros",
      libraryDependencies ++= Seq(
        // "org.scala-lang" % "scala-reflect" % scalaVersion.value,
        // "org.scala-lang" % "scala-library" % scalaVersion.value,
        "org.scala-lang" % "scala-compiler" % scalaVersion.value % "provided"))

  def utestSettings = ConfigureBoth(
    _.settings(
      libraryDependencies += "com.lihaoyi" %%% "utest" % Ver.MTest % "test",
      testFrameworks      += new TestFramework("utest.runner.Framework")))
    .jsConfigure(
      // Not mandatory; just faster.
      _.settings(jsEnv in Test := PhantomJSEnv().value))

  lazy val root =
    Project("root", file("."))
      .configure(commonSettings.jvm, preventPublication)
      .aggregate(rootJVM, rootJS)

  lazy val rootJVM =
    Project("JVM", file(".rootJVM"))
      .configure(commonSettings.jvm, preventPublication)
      .aggregate(univEqJVM, scalazJVM, catsJVM)

  lazy val rootJS =
    Project("JS", file(".rootJS"))
      .configure(commonSettings.jvm, preventPublication)
      .aggregate(univEqJS , scalazJS , catsJS)

  lazy val univEqJVM = univEq.jvm
  lazy val univEqJS  = univEq.js
  lazy val univEq = crossProject(JSPlatform, JVMPlatform)
    .in(file("univeq"))
    .configureCross(commonSettings, publicationSettings, utestSettings)
    .bothConfigure(definesMacros)
    .settings(moduleName := "univeq")

  lazy val scalazJVM = scalaz.jvm
  lazy val scalazJS  = scalaz.js
  lazy val scalaz = crossProject(JSPlatform, JVMPlatform)
    .in(file("univeq-scalaz"))
    .configureCross(commonSettings, publicationSettings)
    .dependsOn(univEq)
    .configureCross(utestSettings)
    .settings(
      moduleName          := "univeq-scalaz",
      libraryDependencies += "org.scalaz" %%% "scalaz-core" % Ver.Scalaz)

  lazy val catsJVM = cats.jvm
  lazy val catsJS  = cats.js
  lazy val cats = crossProject(JSPlatform, JVMPlatform)
    .in(file("univeq-cats"))
    .configureCross(commonSettings, publicationSettings)
    .dependsOn(univEq)
    .configureCross(utestSettings)
    .settings(
      moduleName          := "univeq-cats",
      libraryDependencies += "org.typelevel" %%% "cats-kernel" % Ver.Cats)
}
