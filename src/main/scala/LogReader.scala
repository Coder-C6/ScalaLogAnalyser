package org.Analyser.Log

import java.io.File
import scala.io.Source

object LogReader {
  def readRecord() {
    print("Enter file Location : ")
    val line = Console.readLine
    println("Thanks, you just typed: " + line)
    val fileTemp = new File(line)
    if (fileTemp.exists()) {
      println("Following is the content read:")
      for (record <- Source.fromFile(line).getLines) {
        // Call parser
        LogParser.parseRecord(record)
      }
    }
    else {
      println("Invalid or non existing file path")
    }
  }
}
