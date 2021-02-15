addSbtPlugin("org.portable-scala" % "sbt-scalajs-crossproject" % "1.0.0")
addSbtPlugin("org.scala-js"       % "sbt-scalajs"              % "1.3.1")
addSbtPlugin("com.github.sbt"       % "sbt-pgp"                  % "2.1.2")
addSbtPlugin("com.github.gseitz"  % "sbt-release"              % "1.0.13")
addSbtPlugin("org.xerial.sbt"     % "sbt-sonatype"             % "3.9.5")

libraryDependencies += "org.scala-js" %% "scalajs-env-jsdom-nodejs" % "1.1.0"
