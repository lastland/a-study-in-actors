lazy val commonSettings = Seq(
  organization := "com.liyaos",
  version := "0.1.0",
  scalaVersion := "2.11.6"
)

lazy val root = (project in file(".")).
  settings(commonSettings: _*).
  settings(
    name := "map reduce future",
    exportJars := true)
