package org.Analyser.Log

import java.io.File
import scala.collection.mutable.ArrayBuffer
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
  private val f5 = "(.+)"

  private val regex = s"$f1 $f2 $f3 $f4 $f5".r

  val space: String="-"
  var tab: Int=0
  var dex: Int=0
  val key: List[String] = List("Exception", "Failure", "Error", "exception", "failure", "error")
  var comStack = new ArrayBuffer[String]

  def readRecord() {
    val conf = new SparkConf().setAppName("Simple Application").setMaster("local")
    val sc = new SparkContext(conf)
    print("Enter file Location : ")
    var con = Console.readLine
    con="test.txt"
    print(con)
    val fileTemp = new File(con)
    if (fileTemp.exists()) {

      //check for format
      val record = sc.textFile(con).map(str =>
        regex.findFirstMatchIn(str).map(m => LogRecord(m.group(1), m.group(2), m.group(3), m.group(4), m.group(5))) match
        {
          case Some(l) => {
            Success(l)
          }
          case None => Failure(new Exception(s"Non matching input: $str"))
        })

      val cFormat = record.filter(_.isSuccess) //correct format
      val wFormat = record.filter(_.isFailure) //wrong format

      wFormat.collect().foreach(println)

      val par = cFormat.collect {
        case Success(l@LogRecord(t, p, c, a, m)) => {
          parser(t,p,c,a,m)
          Nil
        }
      }
      par.collect()
    }
    else
    {
      println("Invalid or non existing file path")
    }
  }

  def parser(t: String, p: String, c: String, a: String, m: String): Unit=
  {
    dex=comStack.lastIndexOf(c)
    var error: Boolean=false
    if(a.contentEquals("->"))
    {
      comStack += c
      for (a <- 1 to tab)
      {print(space)}
      tab+=1
      print(c)
    }
    else if(a.contentEquals("--"))
    {
      //nothing
      for (a <- 1 to tab)
      {print(space)}
      print(c)
    }
    else if(a.contentEquals("<-"))
    {
      if(dex != -1)
      {
        comStack.remove(dex)
        tab-=1
        for (a <- 1 to tab)
        {print(space)}
        print(c)
      }
      else
      {
        error=true
      }
    }
    if(key.exists(m.contains)||(error==true))
    {
      //build component stack
      print(" -> Error")
    }
    print("\n")
    // to test the contents of the stack on every iteration

     //  comStack.foreach(print)
      // print("\n")


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
