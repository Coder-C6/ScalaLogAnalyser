package org.Analyser.Log

import java.io.File
import scala.util.{Failure, Success}
import org.apache.spark.{SparkConf, SparkContext}

object LogAnalyser {
  def main(args: Array[String]): Unit = {
    //initialize
    readRecord()
  }

  /**
    * A sample record:
    * 1447443388.898657 0000411E:0000295E HYPTRACT: -> Delegator.getSysInfoData
    * <-----f1--------> <------f2-------> <--f3-->  f4 <----------f5----------->
    */

  // field 1 : only digits : some time format
  private val f1 = "([0-9]{10}.[0-9]{6})"

  // field 2 : Process ID
  private val f2 = "([0-9a-fA-F]{8}:[0-9a-fA-F]{8})"

  // field 3 : Component name
  private val f3 = "([a-zA-Z ]+:)"

  // field 4 : '->' or '--' or '<-'
  private val f4 = "(<?[-]-?>?)"

  // field 5 : function message : any number of any character, reluctant
  private val f5 = "(.*?)"

  private val regex = s"$f1 $f2 $f3 $f4 $f5".r

  val key: List[String] = List("Exception", "Failure")


  def readRecord() {
    val conf = new SparkConf().setAppName("Simple Application").setMaster("local")
    val sc = new SparkContext(conf)
    print("Enter file Location : ")
    val con = Console.readLine
    println("Thanks, you just typed: " + con)
    val fileTemp = new File(con)
    if (fileTemp.exists()) {

      //check for format
      val record = sc.textFile(con).map(str =>
        regex.findFirstMatchIn(str).map(m => LogRecord(m.group(1), m.group(2), m.group(3), m.group(4), m.group(5))) match
        {
          case Some(l) => Success(l)
          case None => Failure(new Exception(s"Non matching input: $str"))
        })

      val cFormat = record.filter(_.isSuccess) //correct format
      val wFormat = record.filter(_.isFailure) //wrong format

      record.unpersist(); // remove the rdd from memory

      //collect records with error messages
      val messagesWithError = cFormat.collect {
        case Success(l@LogRecord(_, _, _, _, m)) if m.contains(key) => l
      }
      //collect records with entry point but no exit point
      //.....
      //build component stack
      //......
    }
    else {
      println("Invalid or non existing file path")
    }
  }

  case class LogRecord
  (
    Time: String,
    ProcessID: String,
    Component: String,
    Arrow: String,
    Message: String
  )

}
