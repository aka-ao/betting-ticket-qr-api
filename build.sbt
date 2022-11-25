ThisBuild / organizationName := "sample-akka-http"
ThisBuild / version := "0.1"

ThisBuild / scalaVersion := "2.13.10"
val AkkaVersion = "2.7.0"
val AkkaHttpVersion = "10.4.0"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion,
  "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
  "com.typesafe.akka" %% "akka-http" % AkkaHttpVersion,
  "org.wvlet.airframe" %% "airframe" % "22.11.0",
  "com.typesafe.akka" %% "akka-persistence-typed" % AkkaVersion,
  "com.typesafe.akka" %% "akka-serialization-jackson" % AkkaVersion,
  "com.typesafe.akka" %% "akka-persistence-cassandra" % "1.1.0",
  "org.slf4j" % "slf4j-simple" % "2.0.3",
  "com.auth0" % "java-jwt" % "4.2.1",
  "com.auth0" % "jwks-rsa" % "0.21.2"
)
