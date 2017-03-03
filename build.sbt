scalaVersion := "2.12.1"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.4.16",
  "com.typesafe.akka" %% "akka-slf4j" % "2.4.16",

  "com.typesafe.akka" %% "akka-http-core" % "10.0.3",
  "com.typesafe.akka" %% "akka-http" % "10.0.3",

  "com.typesafe.akka" %% "akka-http-spray-json" % "10.0.3",
  "com.typesafe.akka" %% "akka-http-jackson" % "10.0.3",

  "com.typesafe.slick" % "slick_2.12" % "3.2.0",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.2.0",
  "org.postgresql" % "postgresql" % "9.4.1212",

  "ch.qos.logback" % "logback-classic" % "1.2.1",
  "ch.qos.logback" % "logback-core" % "1.2.1",

  "com.typesafe.akka" %% "akka-testkit" % "2.4.16" % "test",
  "com.typesafe.akka" %% "akka-http-testkit" % "10.0.3" % "test",
  "org.scalactic" %% "scalactic" % "3.0.1" % "test",
  "p6spy" % "p6spy" % "2.1.2" % "test",
  "org.scalatest" %% "scalatest" % "3.0.1" % "test",
  "org.scalacheck" %% "scalacheck" % "1.13.4" % "test"
)