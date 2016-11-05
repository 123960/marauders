import com.github.retronym.SbtOneJar._

name := """marauders-map"""

scalaVersion := "2.10.4"

libraryDependencies ++= Dependencies.sparkAkkaHadoop

oneJarSettings

fork in run := true