import JmhKeys._

name := """scala-actor-map-reduce"""

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.6"

jmhSettings

outputTarget in Jmh := target.value / s"scala-${scalaBinaryVersion.value}"

libraryDependencies ++= Seq(
  // Add your own project dependencies in the form:
  // "group" % "artifact" % "version"
  "org.scala-lang" % "scala-actors" % "2.11.6"
)


fork in run := true
