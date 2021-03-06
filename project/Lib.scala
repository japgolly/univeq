import sbt._
import Keys._
import com.jsuereth.sbtpgp.PgpKeys._
import sbtcrossproject.CrossProject
import sbtcrossproject.CrossPlugin.autoImport._
import scalajscrossproject.ScalaJSCrossPlugin.autoImport._
import xerial.sbt.Sonatype.autoImport._

object Lib {
  type CPE = CrossProject => CrossProject
  type PE = Project => Project

  def byScalaVersion[A](f: PartialFunction[(Long, Long), Seq[A]]): Def.Initialize[Seq[A]] =
    Def.setting(CrossVersion.partialVersion(scalaVersion.value).flatMap(f.lift).getOrElse(Nil))

  private def extraCrossProjectScalaDirs(k: ConfigKey): Def.Initialize[Seq[File]] = Def.setting {
    val srcBase = (k / sourceDirectory).value
    val stage   = srcBase.getName()
    val shared  = srcBase.getParentFile().getParentFile().getParentFile() / "shared" / "src" / stage
    CrossVersion.partialVersion(scalaVersion.value) match {
      case Some((2, _))  => Seq(shared / "scala-2")
      case Some((3, _))  => Seq(shared / "scala-3")
      case _             => Nil
    }
  }

  def crossProjectScalaDirs: CPE =
    _.settings(
      Compile / unmanagedSourceDirectories ++= extraCrossProjectScalaDirs(Compile).value,
      Test / unmanagedSourceDirectories    ++= extraCrossProjectScalaDirs(Test).value,
    )

  class ConfigureBoth(val jvm: PE, val js: PE) {
    def jvmConfigure(f: PE) = new ConfigureBoth(f compose jvm, js)
    def  jsConfigure(f: PE) = new ConfigureBoth(jvm, f compose js)
  }

  def ConfigureBoth(both: PE) = new ConfigureBoth(both, both)

  implicit def _configureBothToCPE(p: ConfigureBoth): CPE =
    _.jvmConfigure(p.jvm).jsConfigure(p.js)

  def addCommandAliases(m: (String, String)*): PE = {
    val s = m.map(p => addCommandAlias(p._1, p._2)).reduce(_ ++ _)
    _.settings(s: _*)
  }

  implicit class CrossProjectExt(val cp: CrossProject) extends AnyVal {
    def bothConfigure(fs: PE*): CrossProject =
      fs.foldLeft(cp)((q, f) =>
        q.jvmConfigure(f).jsConfigure(f))
  }
  implicit def CrossProjectExtB(b: CrossProject.Builder) =
    new CrossProjectExt(b)

  def publicationSettings(ghProject: String) = ConfigureBoth(
    _.settings(
      publishTo := sonatypePublishToBundle.value,
      pomExtra :=
        <scm>
          <connection>scm:git:github.com/japgolly/{ghProject}</connection>
          <developerConnection>scm:git:git@github.com:japgolly/{ghProject}.git</developerConnection>
          <url>github.com:japgolly/{ghProject}.git</url>
        </scm>
        <developers>
          <developer>
            <id>japgolly</id>
            <name>David Barri</name>
          </developer>
        </developers>))
    .jsConfigure(
      sourceMapsToGithub(ghProject))

  def sourceMapsToGithub(ghProject: String): PE =
    p => p.settings(
      scalacOptions ++= {
        val isScala3 = scalaVersion.value startsWith "3"
        val ver      = version.value
        if (isSnapshot.value)
          Nil
        else {
          val a = p.base.toURI.toString.replaceFirst("[^/]+/?$", "")
          val g = s"https://raw.githubusercontent.com/japgolly/$ghProject"
          val flag = if (isScala3) "-scalajs-mapSourceURI" else "-P:scalajs:mapSourceURI"
          s"$flag:$a->$g/v$ver/" :: Nil
        }
      }
    )

  def preventPublication: PE =
    _.settings(
      publish / skip     := true,
      publish            := {},
      publishLocal       := {},
      publishSigned      := {},
      publishLocalSigned := {},
      publishArtifact    := false,
      publishTo          := Some(Resolver.file("Unused transient repository", target.value / "fakepublish")),
      packagedArtifacts  := Map.empty)
    // .disablePlugins(plugins.IvyPlugin)
}
