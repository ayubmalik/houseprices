name := """houseprices-core"""


libraryDependencies ++= {

  val elasticSearchVersion = "1.7.5"
  val json4sVersion        = "3.3.0"
  val scalaTestVersion     = "2.2.6"
  val akkaVersion          = "2.4.2"
  val akkaHttpVersion      = "2.4.2"

  Seq(
    "ch.qos.logback"             % "logback-classic"                % "1.1.5",
    "org.apache.httpcomponents"  % "httpclient"                     % "4.5.1",
    "org.elasticsearch"          % "elasticsearch"                  % elasticSearchVersion,
    "org.json4s"                %% "json4s-native"                  % json4sVersion,
    "com.typesafe.akka"         %% "akka-actor"                     % akkaVersion,
    "com.typesafe.akka"         %% "akka-slf4j"                     % akkaVersion,
    "com.typesafe.akka"         %% "akka-http-experimental"         % akkaHttpVersion,
    "com.typesafe.akka"         %% "akka-http-xml-experimental"     % akkaHttpVersion,
    "com.typesafe.akka"         %% "akka-http-spray-json-experimental"     % akkaHttpVersion,
    "org.scalatest"             %% "scalatest"                      % scalaTestVersion % "test",
    "com.typesafe.akka"         %% "akka-testkit"                   % akkaVersion % "test",
    "com.typesafe.akka"         %% "akka-http-testkit-experimental" % "2.4.2-RC3" % "test"
  )
}
