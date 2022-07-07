addSbtPlugin("ch.epfl.scala"      % "sbt-scalafix"             % "0.9.31")
addSbtPlugin("com.github.sbt"     % "sbt-pgp"                  % "2.1.2")
addSbtPlugin("com.github.sbt"     % "sbt-release"              % "1.1.0")
addSbtPlugin("org.portable-scala" % "sbt-scalajs-crossproject" % "1.1.0")
addSbtPlugin("org.scala-js"       % "sbt-scalajs"              % "1.10.1")
addSbtPlugin("org.xerial.sbt"     % "sbt-sonatype"             % "3.9.10")

libraryDependencies += "org.scala-js" %% "scalajs-env-jsdom-nodejs" % "1.1.0"
