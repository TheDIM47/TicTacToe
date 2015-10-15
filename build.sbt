name := "TicTacToe"

version := "1.0"

scalaVersion := "2.11.7"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

val scalacOptions = Seq("-Xlint", "-unchecked", "-deprecation", "-feature", "-Yrangepos")

libraryDependencies ++= Seq(
    "org.scala-lang.modules" %% "scala-async" % "0.9.2" withSources() withJavadoc
  , "com.typesafe.play" %% "play-specs2" % "2.4.3" % "test" withSources() withJavadoc
)

