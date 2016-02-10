# ScalaLogAnalyser
A Simple Log Analyser

To run the application Execute main.scala

Finished:-
1.Reading the file as a RDD[STRING] type.
2.Parsing the file to check its correctness with format.
3.isolating record with the keywords - Error,Failure


TODO:-
1.Build a hashmap with multimap
    Hashmap<Key=component.functionname, value = TimeDetails>.
    Hashmap<Key=component.functionname, value = ErrorDetails>>
    TimeDetails - class with variables, exit and entry time
    ErrorDetails - class with variables, error stack and timestamp of error
    
2.isolating record with entry points without any matching exit point.

3.Display the error stack

4.Build a UI
