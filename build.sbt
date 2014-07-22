seq(com.github.retronym.SbtOneJar.oneJarSettings: _*)

name := "ast2uml"

version := "0.1-SNAPSHOT"

scalaVersion := "2.10.4"

organization := "com.github.caiiiycuk.ast2uml"

resolvers ++= Seq(
	"snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
  "releases" at "https://oss.sonatype.org/content/repositories/releases")

scalacOptions ++= Seq("-unchecked", "-deprecation")

libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.0" % "test"

mainClass in oneJar := Some("com.github.caiiiycuk.ast2uml.Ast2Uml")
