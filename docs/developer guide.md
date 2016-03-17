#Developer Guide

##Introduction

##Table of Contents
* [Architecture](#architecture)
* [UI component](#ui-component)
	* [Main class](#main-class)
	* [MainController class](#maincontroller-class)
	* [DisplayController class](#displaycontroller-class)
	* [TaskController class](#taskcontroller-class)
	* [TaskDetailsController class](#taskdetailscontroller-class)
* [Logic component](#logic-component)
	* [Logic class](#logic-class)
	* [LogicData class](#logicdata-class)
	* [LogicCommand class](#logiccommand-class)
* [Command component](#command-component)
* [Parser component](#parser-component)
	* [Parser Class](#parser-class)
* [Storage component](#storage-component)
	* [Storage class](#storage-class)

# Architecture
![Architecture](https://github.com/cs2103jan2016-t16-3j/main/blob/master/docs/UML%20Diagrams/Architecture%20(new).png?raw=true)
> Figure 1: Architecture of Urgenda

# UI Component
![UI](/docs/UML Diagrams/UI.png)
> Figure 2: Structure of UI component

The UI is actually a piece of shit. This UI is really really bad HFE designing. No task analysis is done at all, with zero observational study, usability testing or any data gathering done. 
I am not even trying to applying what I have learnt in class, but I'm damn proud of it. 'Cus Im an asshole. 

## Main Class
## MainController Class
## DisplayController Class
## TaskController Class
## TaskDetailsController Class
# Logic Component
![Logic](/docs/UML Diagrams/Logic.png)
> Figure 3: Structure of Logic component

The Logic component is accessible through the `Logic` class using the facade pattern, in which it is in charge of handling the execution of user inputs from the UI component. This component only relies on the Parser component and Storage component and works independently from the UI component.

## Logic Class
![Logic](/docs/UML Diagrams/Logic sequence diagram.png)
> Figure 4: Sequence Diagram when a add command is given

The `Logic` class contains the methods that handle the core functionality of Urgenda. It can be thought of as the "processor" of Urgenda. User inputs are passed to the `executeCommand(String, int)` to determine the corresponding command object based on the user input by the `Parser` component.

After knowing the type of command, `Logic`retrieves the updated state and data per launch time from `LogicData` via the `UpdateSate()` method call. After which the command object will be passed to `LogicCommand` for process through the `processCommand(Command)` method call. The command will then be executed, and `LogicData` will update its relevant fields. In the case of adding a task, the task will be added task list via the `addTask(Task)` method call and the display state will be updated correspondingly.  `LogicData` maintains a temporary set of data same as that displayed to the user per launch time so as to facillitate number pointing of task and reduce dependency with `Storage` component (e.g. when user inputs delete 4, `Logic` is able to determine which is task 4 without having to call `Storage`). `Storage` component will then store the data to ensure no loss of user data upon unintentional early termination of Urgenda Program. More details of the storing procedure are mentioned in the `Storage` section.

The executeCommand(String) method will then return the appropriate feedback to its caller method. The caller method can then decide how to update the user interface.

## LogicData Class
## LogicCommand Class
# Command Component
![Command](https://github.com/cs2103jan2016-t16-3j/main/blob/master/docs/UML%20Diagrams/Command.png?raw=true)
> Figure 5: Structure of Command component where the Command Pattern is used

`Command` is an abstract class that uses the Command Pattern and holds the `execute()` method where the generic execution of `Command.execute()` can be used. Classes that extends from it will have their own implementation of the `execute()` method. `TaskCommand` is another abstract class which extends `Command` and is for commands that deal with manipulation of Task objects. `TaskCommand` has two abstract functions which are `Undo()` and `Redo()` which are also implemented separately by the child classes to revert the changes made by that command. 

# Parser Component
![Parser](/docs/UML Diagrams/Parser.png)
> Figure 6: Structure of Parser component
## Parser Class

# Storage Component
![Storage](/docs/UML Diagrams/Storage.png)
> Figure 7: Structure of Storage component

The Storage component is accessible through the `Storage` class using the facade pattern, where it handles and directs file manipulation using the respective classes. Gestalt's Principle is used in this component to enhance the cohesiveness of each class and reduce the coupling, where only necessary dependencies are utilized. The functions of each class are grouped accordingly to the very meaning that each class name suggest. 

Urgenda primarily has 3 files:
* `data.txt` stores all the tasks, either completed or uncompleted, in JSON format. Each line, or each string, represents one task. This file is able to be renamed or moved to other directories as per user's desire. 
* `settings.txt` stores the settings of the user, such as the file name and file location, so that on start-up Urgenda can retrieve these settings, retrieve the datafile and set preferences for the user. This file cannot be moved or renamed. 
* `help.txt` is where the documentation for the user is stored. If the user ever require any form of assistance in entering commands, he can bring up the help panel, which retrieves the text from this file. This file cannot be moved or renamed. 

## Storage Class
In every case where `LogicData` needs to access or edit the data in the file or the datafile itself, it goes through `Storage` class, which then dispatches the corresponding method using the respective classes within the `Storage` component. 

As mentioned above, apart from the `Storage` class which acts as the facade, all other classes have their own specific functions:

Class | Function
--- | ---
`FileEditor` | Contains all the file manipulation methods that is required in the `Storage` component - retrieving from file, writing to file, renaming, moving to other directories, clearing the file. Essentially, only this class can access and manipulate the actual file itself
`JsonCipher` | The primary ciphering tool. `Storage` uses the external library Gson that allows conversion of objects to a string. In this class, instead of converting directly from a `Task` to a `String`, a `Task` is converted to a `LinkedHashMap<String, String>`, then converted into a `String`. This allows for easier conversion back into a `Task` from a `String`. `JsonCipher` provides the tools required for converting from `Task` <=> `LinkedHashMap<String, String>` <=> `String`
`Encryptor` | A subclass of `JsonCipher`, the role of `Encryptor` is to encrypt all `Task` into a `String` using `JsonCipher` as a means of doing so.
`Decryptor` | A subclass of `JsonCipher`, the role of `Decryptor` is to decrypt all `String` into a `Task` using `JsonCipher` as a means of doing so.
`SettingsEditor` | This class handles all matters related to the settings of the user, in order not to mix it with the actual data file. File manipulation and encryption/decryption is done using the `FileEditor` class and through `JsonCipher` directly, since there is no need `Task` involved with the settings. 

The main APIs of the `Storage` class include:

Method | Return type and function
--- | --- 
`updateArrayList()` | Returns `ArrayList<Task>`. This method is used during startup to retrieve all tasks in the datafile and pack it into an ArrayList.
`save(ArrayList<Task> tasks, ArrayList<Task> archives)` | Void function. This method is used to store all tasks in the datafile, for easy retrieval, relocation to another computer. 
`changeFileSettings(String path, String name)` | Void function. This method allows the datafile to be renamed and move to other directories/folders through Urgenda itself, with no need to enter File Explorer


### Sequence diagram `updateArrayList`
![updateArrayListSD](/docs/UML Diagrams/updateSDStorage.png)
> Figure 8: Sequence diagram of `updateArrayList()`

`updateArrayList()` is the generic method for `updateCurrentTaskList()` and `updateArchiveTaskList()`.



### Sequence diagram `save(ArrayList<Task> tasks, ArrayList<Task> archives)`
![saveSD](/docs/UML Diagrams/saveSDStorage.png)
> Figure 9: Sequence diagram of `save(ArrayList<Task> tasks, ArrayList<Task> archives)`

`save(ArrayList<Task> tasks, ArrayList<Task> archives)` saves the current list of tasks into the specified file by writing onto it.



### Sequence diagram `changeFileSettings(String path, String name)`
![changeFileSettingsSD](/docs/UML Diagrams/changeFileSettingsSDStorage.png)
> Figure 10: Sequence diagram of `changeFileSettings(String path, String name)`

`changeFilePath(String path)` and `changeFileName(String name)` are similar methods to `changeFileSettings(String path, String name)`, whereby the latter changes both the name and the directory the datafile is saved in.
`changeFileSettings(String path, String name)` has two parts to it:
* edit the preferred file name and file location in `settings.txt`
* rename/relocate the actual datafile with the preferred name to the preferred location
