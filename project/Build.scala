import sbt._
import sbt.Keys._
import com.jsuereth.sbtpgp.PgpKeys
import org.portablescala.sbtplatformdeps.PlatformDepsPlugin.autoImport._
import org.scalajs.jsenv.jsdomnodejs.JSDOMNodeJSEnv
import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._
import sbtrelease.ReleasePlugin.autoImport._
import scalajscrossproject.ScalaJSCrossPlugin.autoImport._
import Lib._

object UnivEqBuild {
  import sbtcrossproject.CrossPlugin.autoImport._

  private val ghProject = "univeq"

  private val publicationSettings =
    Lib.publicationSettings(ghProject)

  object Ver {
    val Cats            = "2.4.2"
    val MTest           = "0.7.5"
    val Scala212        = "2.12.11"
    val Scala213        = "2.13.4"
    val Scalaz          = "7.2.30"
    val ScalaCollCompat = "2.3.2"
    val ScalaJsDom      = "1.1.0"
  }

  def scalacFlags = Seq(
    "-deprecation",
    "-unchecked",
    "-feature",
    "-language:postfixOps",
    "-language:implicitConversions",
    "-language:higherKinds",
    "-language:existentials",
    "-opt:l:inline",
    "-opt-inline-from:scala.**",
    "-opt-inline-from:japgolly.univeq.**",
    "-Ywarn-dead-code",
    "-Ywarn-value-discard")

  val commonSettings = ConfigureBoth(
    _.settings(
      organization                  := "com.github.japgolly.univeq",
      homepage                      := Some(url("https://github.com/japgolly/" + ghProject)),
      licenses                      += ("Apache-2.0", url("http://opensource.org/licenses/Apache-2.0")),
      startYear                     := Some(2015),
      scalaVersion                  := Ver.Scala213,
      crossScalaVersions            := Seq(Ver.Scala212, Ver.Scala213),
      scalacOptions                ++= scalacFlags,
      scalacOptions in Test        --= Seq("-Ywarn-dead-code"),
      shellPrompt in ThisBuild      := ((s: State) => Project.extract(s).currentRef.project + "> "),
      incOptions                    := incOptions.value,
      updateOptions                 := updateOptions.value.withCachedResolution(true),
      releasePublishArtifactsAction := PgpKeys.publishSigned.value,
      releaseTagComment             := s"v${(version in ThisBuild).value}",
      releaseVcsSign                := true))

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
      _.settings(jsEnv in Test := new JSDOMNodeJSEnv))

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
    .settings(
      moduleName := "univeq",
      libraryDependencies += "org.scala-lang.modules" %%% "scala-collection-compat" % Ver.ScalaCollCompat)
    .jsSettings(libraryDependencies += "org.scala-js" %%% "scalajs-dom" % Ver.ScalaJsDom)

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
      libraryDependencies += "org.typelevel" %%% "cats-core" % Ver.Cats)
}
