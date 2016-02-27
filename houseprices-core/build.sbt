name := """houseprices-core"""

version := "1.0"

scalaVersion := "2.11.7"

fork in (Test) := true

val scalaTestVersion     = "2.2.6"
val akkaVersion          = "2.4.2"
val elasticSearchVersion = "2.2.0"

libraryDependencies ++= Seq(
  "net.java.dev.jna"           % "jna"                               % "4.2.1",
  "ch.qos.logback"             % "logback-classic"                   % "1.1.5",
  "org.elasticsearch"          % "elasticsearch"                     % elasticSearchVersion,
  "com.typesafe.akka"         %% "akka-actor"                        % akkaVersion,
  "com.typesafe.akka"         %% "akka-slf4j"                        % akkaVersion,
  "com.typesafe.akka"         %% "akka-http-experimental"            % akkaVersion,
  "com.typesafe.akka"         %% "akka-http-xml-experimental"        % akkaVersion,
  "com.typesafe.akka"         %% "akka-http-spray-json-experimental" % akkaVersion,
  "org.scalatest"             %% "scalatest"                         % scalaTestVersion % "test",
  "com.typesafe.akka"         %% "akka-testkit"                      % akkaVersion % "test",
  "com.typesafe.akka"         %% "akka-http-testkit-experimental"    % "2.4.2-RC3" % "test",
  "org.mockito"                %  "mockito-core"                     % "2.0.43-beta"
)
