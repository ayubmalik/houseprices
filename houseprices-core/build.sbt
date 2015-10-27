name := """houseprices-core"""

libraryDependencies ++= {
  val elasticSearchVersion = "1.7.3"
  val parboiledVersion     = "2.1.0"
  val akkaVersion          = "2.4.0"
  val json4sVersion        = "3.3.0"
  val scalaTestVersion     = "2.2.4"

  Seq(
    "org.elasticsearch" %  "elasticsearch"                        % elasticSearchVersion,
    "org.json4s"        %% "json4s-native"                        % json4sVersion,
    "org.parboiled"     %% "parboiled"                            % parboiledVersion,
    "com.typesafe.akka" %% "akka-actor"                           % akkaVersion,
    "org.scalatest"     %% "scalatest"                            % scalaTestVersion % "test",
    "com.typesafe.akka" %% "akka-testkit"                         % akkaVersion % "test"
  )
}


