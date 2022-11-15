name := "sample-akka-http"

version := "0.1"

scalaVersion := "2.12.4"
val AkkaVersion = "2.7.0"
val AkkaHttpVersion = "10.4.0"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion,
  "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
  "com.typesafe.akka" %% "akka-http" % AkkaHttpVersion,
  "org.wvlet.airframe" %% "airframe" % "19.5.0",
  "com.github.blemale" %% "scaffeine" % "3.1.0" % "compile",
  "org.slf4j" % "slf4j-simple" % "2.0.3"
)
