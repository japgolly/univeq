addSbtPlugin("ch.epfl.scala"      % "sbt-scalafix"             % "0.14.5")
addSbtPlugin("com.github.sbt"     % "sbt-ci-release"           % "1.11.2")
addSbtPlugin("org.portable-scala" % "sbt-scalajs-crossproject" % "1.3.2")
addSbtPlugin("org.scala-js"       % "sbt-scalajs"              % "1.6.0")

libraryDependencies += "org.scala-js" %% "scalajs-env-jsdom-nodejs" % "1.1.0"
