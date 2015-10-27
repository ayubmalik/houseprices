name := """houseprices-core"""

libraryDependencies ++= {
  val elasticSearchVersion = "1.7.3"
  val akkaVersion          = "2.4.0"
  val akkaStreamVersion    = "1.0"
  val scalaTestVersion     = "2.2.4"
  val parboiledVersion     = "2.1.0"

  Seq(
    "org.elasticsearch" %  "elasticsearch"                        % elasticSearchVersion,
    "org.parboiled"     %% "parboiled"                            % parboiledVersion,
    "com.typesafe.akka" %% "akka-actor"                           % akkaVersion,
    "com.typesafe.akka" %% "akka-stream-experimental"             % akkaStreamVersion,
    "com.typesafe.akka" %% "akka-http-core-experimental"          % akkaStreamVersion,
    "com.typesafe.akka" %% "akka-http-spray-json-experimental"    % akkaStreamVersion,
    "org.scalatest"     %% "scalatest"                            % scalaTestVersion % "test",
    "com.typesafe.akka" %% "akka-testkit"                         % akkaVersion % "test"
    
  )
}


