name := """event-processor"""

version := "1.0"

scalaVersion := "2.11.6"

resolvers += "Typesafe repository releases" at "http://repo.typesafe.com/typesafe/releases/"

resolvers += "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.4.1",
  "com.typesafe.akka" %% "akka-stream-experimental" % "2.0.2",
  "com.typesafe.akka" %% "akka-http-core-experimental" % "2.0.2",
  "com.typesafe.akka" %% "akka-http-experimental" % "2.0.2",
  "com.typesafe.akka" %% "akka-slf4j" % "2.4.1",
  "ch.qos.logback" % "logback-classic" % "1.1.3",
  "org.json4s" %% "json4s-jackson" % "3.3.0",
  "org.json4s" %% "json4s-mongo" % "3.3.0",
  "org.reactivemongo" %% "reactivemongo" % "0.12.0-SNAPSHOT",
  "de.heikoseeberger" %% "akka-http-json4s" % "1.4.2",
  "com.typesafe" % "config" % "1.3.0",
  "com.sksamuel.elastic4s" %% "elastic4s-core" % "2.1.0",
  "com.typesafe.akka" %% "akka-testkit" % "2.4.1" % "test",
  "com.typesafe.akka" %% "akka-http-testkit-experimental" % "2.0-M2" % "test",
  "com.typesafe.akka" %% "akka-stream-testkit-experimental" % "2.0-M2" % "test",
  "org.scalatest" %% "scalatest" % "2.2.4" % "test")
