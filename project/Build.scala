import sbt._
import sbt.Keys._
import com.jsuereth.sbtpgp.PgpKeys
import dotty.tools.sbtplugin.DottyPlugin.autoImport._
import org.portablescala.sbtplatformdeps.PlatformDepsPlugin.autoImport._
import org.scalajs.jsenv.jsdomnodejs.JSDOMNodeJSEnv
import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._
import sbtcrossproject.CrossPlugin.autoImport._
import sbtrelease.ReleasePlugin.autoImport._
import scalajscrossproject.ScalaJSCrossPlugin.autoImport._

object UnivEqBuild {
  import Dependencies._
  import Lib._

  private val ghProject = "univeq"

  private val publicationSettings =
    Lib.publicationSettings(ghProject)

  def scalacCommonFlags = Seq(
    "-deprecation",
    "-unchecked",
    "-feature",
    "-language:postfixOps",
    "-language:implicitConversions",
    "-language:higherKinds",
    "-language:existentials",
  )

  def scalac2Flags = Seq(
    "-opt:l:inline",
    "-opt-inline-from:scala.**",
    "-opt-inline-from:japgolly.univeq.**",
    "-Ywarn-dead-code",
    "-Ywarn-value-discard",
  )

  def scalac3Flags = Seq(
    "-source", "3.0-migration",
  )

  val commonSettings = ConfigureBoth(
    _.settings(
      organization                  := "com.github.japgolly.univeq",
      homepage                      := Some(url("https://github.com/japgolly/" + ghProject)),
      licenses                      += ("Apache-2.0", url("http://opensource.org/licenses/Apache-2.0")),
      startYear                     := Some(2015),
      // scalaVersion                  := Ver.Scala213,
      scalaVersion                  := Ver.Scala3,
      crossScalaVersions            := Seq(Ver.Scala212, Ver.Scala213, Ver.Scala3),
      scalacOptions                ++= scalacCommonFlags,
      scalacOptions                ++= byScalaVersion {
                                         case (2, _) => scalac2Flags
                                         case (3, _) => scalac3Flags
                                       }.value,
      scalacOptions in Test        --= Seq("-Ywarn-dead-code"),
      shellPrompt in ThisBuild      := ((s: State) => Project.extract(s).currentRef.project + "> "),
      testFrameworks                := Nil,
      incOptions                    := incOptions.value,
      updateOptions                 := updateOptions.value.withCachedResolution(true),
      releasePublishArtifactsAction := PgpKeys.publishSigned.value,
      releaseTagComment             := s"v${(version in ThisBuild).value}",
      releaseVcsSign                := true))

  def definesMacros: Project => Project =
    _.settings(
      scalacOptions       ++= (if (isDotty.value) Nil else Seq("-language:experimental.macros")),
      libraryDependencies ++= (if (isDotty.value) Nil else Seq(Dep.ScalaCompiler.value % Provided)),
    )

  def utestSettings = ConfigureBoth(
    _.settings(
      libraryDependencies += Dep.MTest.value % Test,
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
    .configureCross(commonSettings, crossProjectScalaDirs, publicationSettings, utestSettings)
    .bothConfigure(definesMacros)
    .settings(
      moduleName := "univeq",
      libraryDependencies += Dep.ScalaCollCompat.value)
    .jsSettings(
      libraryDependencies += Dep.ScalaJsDom.value)

  lazy val scalazJVM = scalaz.jvm
  lazy val scalazJS  = scalaz.js
  lazy val scalaz = crossProject(JSPlatform, JVMPlatform)
    .in(file("univeq-scalaz"))
    .configureCross(commonSettings, publicationSettings)
    .dependsOn(univEq)
    .configureCross(utestSettings)
    .settings(
      moduleName          := "univeq-scalaz",
      libraryDependencies += Dep.Scalaz.value)

  lazy val catsJVM = cats.jvm
  lazy val catsJS  = cats.js
  lazy val cats = crossProject(JSPlatform, JVMPlatform)
    .in(file("univeq-cats"))
    .configureCross(commonSettings, publicationSettings)
    .dependsOn(univEq)
    .configureCross(utestSettings)
    .settings(
      moduleName          := "univeq-cats",
      libraryDependencies += Dep.Cats.value)
}
