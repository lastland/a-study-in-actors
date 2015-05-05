import JmhKeys._

name := """akka-actor-map-reduce"""

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.6"

jmhSettings

outputTarget in Jmh := target.value / s"scala-${scalaBinaryVersion.value}"

libraryDependencies ++= Seq(
  // Add your own project dependencies in the form:
  // "group" % "artifact" % "version"
  "com.typesafe.akka" %% "akka-actor" % "2.4-SNAPSHOT"
)

resolvers += "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/"

fork in run := true
