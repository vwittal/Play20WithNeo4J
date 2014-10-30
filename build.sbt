name := "PlayNeo4J"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  "net.databinder" %% "dispatch-http" % "0.8.10",
  cache
)


play.Project.playScalaSettings
