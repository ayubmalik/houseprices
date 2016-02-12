name := """houseprices-core"""

libraryDependencies ++= {

  val elasticSearchVersion = "1.7.5"
  val json4sVersion        = "3.3.0"
  val scalaTestVersion     = "2.2.6"
  val akkaVersion          = "2.4.1"
  val akkaHttpVersion      = "2.0.3"

  Seq(
    "org.apache.httpcomponents" %  "httpclient"    % "4.5.1",
    "org.elasticsearch"         %  "elasticsearch" % elasticSearchVersion,
    "org.json4s"                %% "json4s-native" % json4sVersion,
    "com.typesafe.akka"         %% "akka-actor"    % akkaVersion,
    "com.typesafe.akka"         %% "akka-http-core-experimental" % akkaHttpVersion,
    "org.scalatest"             %% "scalatest"     % scalaTestVersion % "test",
    "com.typesafe.akka"         %% "akka-testkit"  % akkaVersion % "test"
  )
}
