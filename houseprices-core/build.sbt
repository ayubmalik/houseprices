name := """houseprices-core"""

libraryDependencies ++= {
  val elasticSearchVersion = "1.7.3"
  val json4sVersion        = "3.3.0"
  val scalaTestVersion     = "2.2.4"

  Seq(
    "org.apache.httpcomponents" %  "httpclient"    % "4.5.1",
    "org.elasticsearch"         %  "elasticsearch" % elasticSearchVersion,
    "org.json4s"                %% "json4s-native" % json4sVersion,
    "org.scalatest"             %% "scalatest"     % scalaTestVersion % "test"
  )
}


