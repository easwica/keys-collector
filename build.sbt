name := "key-collector-project"

version := "1.0"

scalaVersion := "2.13.4"

val AkkaVersion = "2.6.10"
val AkkaHttpVersion = "10.2.2"
libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
  "com.typesafe.akka" %% "akka-http" % AkkaHttpVersion
)