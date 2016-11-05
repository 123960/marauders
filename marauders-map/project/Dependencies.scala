import sbt._

object Version {
  val spark               = "1.5.2"
  val hadoop              = "2.6.0"
  val slf4j               = "1.7.6"
  val logback             = "1.1.1"
  val scalaTest           = "2.1.7"
  val mockito             = "1.9.5"
  val akka                = "2.3.14"
  val reactiveMongo       = "0.11.7"
  val sparkStreamingFlume = "1.5.1"
  val scalaTime           = "2.6.0"
  val akkaClusterV        = "2.4.0"
}

object Library {
  // workaround until 2.11 version for Spark Streaming's available
  val sparkStreaming         = "org.apache.spark"  %% "spark-streaming"            % Version.spark
  val akkaActor              = "com.typesafe.akka" %% "akka-actor"                 % Version.akka
  val akkaTestKit            = "com.typesafe.akka" %% "akka-testkit"               % Version.akka
  val hadoopClient           = "org.apache.hadoop" %  "hadoop-client"              % Version.hadoop
  val slf4jApi               = "org.slf4j"         %  "slf4j-api"                  % Version.slf4j
  val logbackClassic         = "ch.qos.logback"    %  "logback-classic"            % Version.logback
  val scalaTest              = "org.scalatest"     %% "scalatest"                  % Version.scalaTest
  val mockitoAll             = "org.mockito"       %  "mockito-all"                % Version.mockito
  val reactiveMongoAll       = "org.reactivemongo" %% "reactiveMongo"              % Version.reactiveMongo
  val sparkStreamingFlumeAll = "org.apache.spark"  %  "spark-streaming-flume_2.10" % Version.sparkStreamingFlume
  val nsScalaTime            = "com.github.nscala-time" %% "nscala-time"           % Version.scalaTime
  val akkaCluster            = "com.typesafe.akka" %% "akka-cluster"               % Version.akkaClusterV
}

object Dependencies {

  import Library._

  val sparkAkkaHadoop = Seq(
    sparkStreaming,
    akkaActor,
    akkaTestKit,
    hadoopClient,
    reactiveMongoAll,
    sparkStreamingFlumeAll,
    logbackClassic % "test",
    scalaTest % "test",
    mockitoAll % "test",
    nsScalaTime
  )
}
