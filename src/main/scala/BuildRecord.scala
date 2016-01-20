package org.Analyser.Log

import java.util.regex.Matcher

object BuildRecord {
  def buildLogRecord(matcher: Matcher) = {
    LogRecord(
      matcher.group(1),
      matcher.group(2),
      matcher.group(3),
      matcher.group(4),
      matcher.group(5))
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
