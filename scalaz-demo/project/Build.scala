import sbt._
import sbt.Keys._

object ScalazdemoBuild extends Build {

  lazy val scalazdemo = Project(
    id = "scalaz-demo",
    base = file("."),
    settings = Project.defaultSettings ++ Seq(
      name := "scalaz-demo",
      organization := "com.bossalien",
      version := "0.1-SNAPSHOT",
      scalaVersion := "2.11.7"
      // add other settings here
    )
  )
}
