# Interpreter JavaFX
This project is the GUI continuation of the console version. Images of the user interface can be found in this directory.

### 1
Implement a GUI using JavaFX. The GUI must support the following operations:
- A window to select the program that will be executed. You can display the list of possible programs as a ListView. Each item of the ListView is the string representation of a possible program (IStmt)
- A window that displays the following information:
    - the number of PrgStates as a TextField
    - the HeapTable as a TableView with two columns: Address and Value
    - the Output as a ListView
    - the FileTable as a ListView
    - the list of PrgState identifiers as a ListView
    - a TableView with two columns: Name and Value, which displays the SymTable of the chosen PrgState ID that has been selected from the list described previously
    - a ListView which displays the ExeStack of the chosen PrgState ID that has been selected from the list described previously. The first element of the ListView is a string represenatation of the top of the ExeStack, the second element represents the second element from the ExeStack and so on.
    - A "One Step" button that runs the `oneStep` function of the `Controller` class. The displayed information is updated after each run. You may want to write a wrapper service for the repository and signal any change in the list of PrgStates.
