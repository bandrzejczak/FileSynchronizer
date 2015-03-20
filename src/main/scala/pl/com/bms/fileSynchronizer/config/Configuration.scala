package pl.com.bms.fileSynchronizer.config

import spray.json.JsonParser

case class Connection(login: String, password: String, url: String)

case class Configuration(connection: Connection, sourceRoot: String, destinationRoot: String, files: List[String], directories: List[String]){
  def allFilesToLoad = {
    val filesFromDirectories = directories.flatMap(directory => DirectoryToLoad(sourceRoot, directory).filesToLoad())
    files ::: directories ::: filesFromDirectories
  }
}

object Configuration{
  import pl.com.bms.fileSynchronizer.config.MyJsonProtocol._

  def load(configFileName: String = "config.json"): Configuration = {
    val configFile = loadConfigFile(configFileName)
    val config = JsonParser(configFile).convertTo[Configuration]
    config
  }

  private def loadConfigFile(configFileName: String): String = {
    scala.io.Source.fromFile(configFileName).getLines().mkString
  }
  
}
