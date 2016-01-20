package org.Analyser.Log

import java.util.regex.Pattern
import java.util.regex.Matcher

/**
  * A sample record:
  * 1447443388.898657 0000411E:0000295E HYPTRACT: -> Delegator.getSysInfoData
  * <-----f1--------> <------f2-------> <--f3-->  f4 <----------f5----------->
  */
  
object LogParser {
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

  private val regex = s"$f1 $f2 $f3 $f4 $f5"
  private val p = Pattern.compile(regex)

  /**
    * Parsing function
    * @param record Assumed to be a log combined record.
    */

  def parseRecord(record: String): Option[BuildRecord.LogRecord] = {
    val matcher = p.matcher(record)
    if (matcher.find) {
      /**format is correct
      * now build record 
      */
      Some(BuildRecord.buildLogRecord(matcher))
     
      /**
      * Work in progress
      *  - call Hashmap function
      *  - Check for error
      */
    }
    else {
      //format wrong do nothing
      None
    }
  }
}
