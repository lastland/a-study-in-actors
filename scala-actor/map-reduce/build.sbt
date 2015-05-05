lazy val commonSettings = Seq(
  organization := "com.liyaos",
  version := "0.1.0",
  scalaVersion := "2.11.4"
)

lazy val root = (project in file(".")).
  settings(commonSettings: _*).
  settings(
    name := "map reduce actor test",
    exportJars := true,
    libraryDependencies ++= Seq("org.scala-lang" % "scala-actors" % "2.11.6"))
