name := """houseprices"""

val commonSettings = Seq(
  organization := "ayubmalik",
  version := "1.0",
  scalaVersion := "2.11.7",
  scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8"),
  testOptions += Tests.Argument(TestFrameworks.JUnit, "-v"),
  EclipseKeys.withSource := true
)

lazy val core = project.in(file("houseprices-core"))
  .settings(commonSettings:_*)

lazy val rest = project.in(file("houseprices-rest-api"))
  .settings(commonSettings:_*)

lazy val main = project.in(file("."))
  .aggregate(core)
  .aggregate(rest)
  .settings(commonSettings:_*)

