# ScalaLogAnalyser
A Simple Log Analyser

To run the application Execute main.scala

Finished:-
1.A module to read the log file - LogReader.scala
2.A module to parse the log records and check the format and rebuild accordingly - LogParser.scala , BuildRecord.scala

TODO:-
1.Build a hashmap with multimap
    Hashmap<Key=component.functionname, value = TimeDetails>.
    Hashmap<Key=component.functionname, value = ErrorDetails>>
    TimeDetails - class with variables, exit and entry time
    ErrorDetails - class with variables, error stack and timestamp of error
    
2.Check for errors based on entry/exit points and the keywords-(Exception, Failure) 

3.Display the error stack

4.Build a UI
