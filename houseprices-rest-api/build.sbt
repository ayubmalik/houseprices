name := """houseprices-rest-api"""

version := "1.0"

scalaVersion := "2.11.7"

val scalaTestVersion     = "2.2.6"
val akkaVersion          = "2.4.2"

libraryDependencies ++= Seq(
    "ch.qos.logback"             % "logback-classic"                   % "1.1.5",
    "com.typesafe.akka"         %% "akka-actor"                        % akkaVersion,
    "com.typesafe.akka"         %% "akka-slf4j"                        % akkaVersion,
    "com.typesafe.akka"         %% "akka-http-experimental"            % akkaVersion,
    "com.typesafe.akka"         %% "akka-http-spray-json-experimental" % akkaVersion,
    "org.scalatest"             %% "scalatest"                         % scalaTestVersion % "test",
    "com.typesafe.akka"         %% "akka-testkit"                      % akkaVersion % "test",
    "com.typesafe.akka"         %% "akka-http-testkit-experimental"    % "2.4.2-RC3" % "test"
  )

fork in (Test) := true
