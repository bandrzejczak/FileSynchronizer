package pl.com.bms.fileSynchronizer

import java.io.File
import java.nio.file.Paths

import pl.com.bms.fileSynchronizer.config.Configuration
import pl.com.bms.fileSynchronizer.fileUploader.TimePrinter
import pl.com.bms.fileSynchronizer.fileUploader.ssh.SshFileUploader
import pl.com.bms.fileSynchronizer.watch.FileWatcher


class Application(config: Configuration) {

  val files = config.allFilesToLoad
  val uploaderWithTimePrinter = new TimePrinter(new SshFileUploader(config))


  def upload() = {
    println("File uploading started...")
    files filter {
      file => new File(config.sourceRoot + file).isFile
    } foreach {
      fileToLoad => uploaderWithTimePrinter.uploadFile(fileToLoad)
    }
    println("All files uploaded!")
    this
  }


  def startWatching(): Unit = {
    val watcher = new FileWatcher(uploaderWithTimePrinter, config.sourceRoot)

    files map {
      file => Paths.get(config.sourceRoot+file)
    } foreach {
      path => watcher.addToWatched(path)
    }

    println("Watching...")
    watcher.startWatching() //infinite loop
  }
}

