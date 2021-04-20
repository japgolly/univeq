import sbt._
import sbt.Keys._
import com.jsuereth.sbtpgp.PgpKeys
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
    "-new-syntax",
    "-Yexplicit-nulls",
    "-Yindent-colons",
  )

  val commonSettings = ConfigureBoth(
    _.settings(
      scalaVersion                  := Ver.Scala3,
      crossScalaVersions            := Seq(Ver.Scala212, Ver.Scala213, Ver.Scala3),
      scalacOptions                ++= scalacCommonFlags,
      scalacOptions                ++= byScalaVersion {
                                         case (2, _) => scalac2Flags
                                         case (3, _) => scalac3Flags
                                       }.value,
      Test / scalacOptions         --= Seq("-new-syntax", "-Yexplicit-nulls", "-Ywarn-dead-code"),
      testFrameworks                := Nil,
      incOptions                    := incOptions.value,
      updateOptions                 := updateOptions.value.withCachedResolution(true),
      releasePublishArtifactsAction := PgpKeys.publishSigned.value,
      releaseTagComment             := s"v${(ThisBuild / version).value}",
      releaseVcsSign                := true))

  def definesMacros: Project => Project =
    _.settings(
      scalacOptions       ++= (if (scalaVersion.value startsWith "3") Nil else Seq("-language:experimental.macros")),
      libraryDependencies ++= (if (scalaVersion.value startsWith "3") Nil else Seq(Dep.ScalaCompiler.value % Provided)),
    )

  def utestSettings = ConfigureBoth(
    _.settings(
      libraryDependencies += Dep.MTest.value % Test,
      testFrameworks      += new TestFramework("utest.runner.Framework")))
    .jsConfigure(
      _.settings(Test / jsEnv := new JSDOMNodeJSEnv))

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
    .jvmSettings(
      Test / fork        := true,
      Test / javaOptions += ("-Dclasses.dir=" + (Test / classDirectory).value.absolutePath),
      Test / unmanagedResourceDirectories ++= {
        val base = (Test / resourceDirectory).value.absolutePath
        CrossVersion.partialVersion(scalaVersion.value) match {
          case Some((2, n))  => Seq(file(base + "-2." + n))
          case Some((3, _))  => Seq(file(base + "-3"))
          case _             => Nil
        }
      }
    )
    .jsSettings(
      libraryDependencies += Dep.ScalaJsDom.value)

  lazy val scalazJVM = scalaz.jvm
  lazy val scalazJS  = scalaz.js
  lazy val scalaz = crossProject(JSPlatform, JVMPlatform)
    .in(file("univeq-scalaz"))
    .configureCross(commonSettings, crossProjectScalaDirs, publicationSettings)
    .dependsOn(univEq)
    .configureCross(utestSettings)
    .settings(
      moduleName          := "univeq-scalaz",
      libraryDependencies += Dep.Scalaz.value)

  lazy val catsJVM = cats.jvm
  lazy val catsJS  = cats.js
  lazy val cats = crossProject(JSPlatform, JVMPlatform)
    .in(file("univeq-cats"))
    .configureCross(commonSettings, crossProjectScalaDirs, publicationSettings)
    .dependsOn(univEq)
    .configureCross(utestSettings)
    .settings(
      moduleName          := "univeq-cats",
      libraryDependencies += Dep.Cats.value)
}
