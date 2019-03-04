
name := "simple-imgur-reupload"

version := "0.1"

scalaVersion := "2.12.8"

lazy val `simple-imgur-reupload` = (project in file(".")).enablePlugins(PlayScala)

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"

resolvers += "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/"

scalaVersion := "2.12.2"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.5.17",
  jdbc,
  ehcache,
  ws,
  specs2 % Test,
  guice
)