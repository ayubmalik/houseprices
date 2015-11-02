name := """houseprices-rest-api"""

libraryDependencies ++= {
  val akkaVersion          = "2.4.0"
  val akkaStreamVersion    = "1.0"
  val scalaTestVersion     = "2.2.4"

  Seq(
    "com.typesafe.akka" %% "akka-actor"                           % akkaVersion,
    "com.typesafe.akka" %% "akka-http-core-experimental"          % akkaStreamVersion,
    "com.typesafe.akka" %% "akka-http-experimental"               % akkaStreamVersion,
    "org.scalatest"     %% "scalatest"                            % scalaTestVersion % "test",
    "com.typesafe.akka" %% "akka-testkit"                         % akkaVersion % "test",
    "com.typesafe.akka" %% "akka-http-testkit-experimental"       % akkaStreamVersion
  )
}


