#Developer Guide

##Introduction

##Table of Contents
* [Architecture](#architecture)
* [Utility component](#utility-component)
	* [Task class](#task-class)
	* [TaskList class](#tasklist-class)
	* [MultipleSlot class](#multipleslot-class)
	* [StateFeedback class](#statefeedback-class)
* [UI component](#ui-component)
	* [Main class](#main-class)
	* [MainController class](#maincontroller-class)
	* [DisplayController class](#displaycontroller-class)
	* [TaskController class](#taskcontroller-class)
	* [TaskDetailsController class](#taskdetailscontroller-class)
* [Logic component](#logic-component)
	* [Logic class](#logic-class)
	* [LogicData class](#logicdata-class)
* [Command component](#command-component)
	* [AddTask class](#addtask-class)
	* [BlockSlots class](#blockslots-class)
	* [Command class](#command-class)
	* [Complete class](#complete-class)
	* [DeleteTask class](#deletetask-class)
	* [Edit class](#edit-class)
	* [Exit class](#exit-class)
	* [Help class](#help-class)
	* [Invalid class](#invalid-class)
	* [Prioritise class](#prioritise-class)
	* [Redo class](#redo-class)
	* [Search class](#search-class)
	* [ShowArchive class](#showarchive-class)
	* [ShowDetails class](#showdetails-class)
	* [Undo class](#undo-class)
	* [Undoable class](#undoable-class)
* [Parser component](#parser-component)
	* [Parser Class](#parser-class)
* [Storage component](#storage-component)
	* [Storage class](#storage-class)
	* [FileEditor class](#fileeditor-class)
	* [JsonCipher class](#jsoncipher-class)
	* [Encryptor class](#encryptor-class)
	* [Decryptor class](#decryptor-class)
	* [SetingsEditor class](#settingseditor-class)

# Architecture
![Architecture](https://github.com/cs2103jan2016-t16-3j/main/blob/master/docs/UML%20Diagrams/Architecture%20(new).png?raw=true)
> Figure 1: Architecture of Urgenda

# Utility Component
## Task Class
## TaskList Class
## MultipleSlot Class
## StateFeedback Class
# UI Component
![UI](/docs/UML Diagrams/UI.png)
> Figure 2: Structure of UI component

## Main Class
## MainController Class
## DisplayController Class
## TaskController Class
## TaskDetailsController Class
# Logic Component
![Logic](https://github.com/cs2103jan2016-t16-3j/main/blob/master/docs/UML%20Diagrams/Logic.png?raw=true)
> Figure 3: Structure of Logic component

The Logic component is accessible through the `Logic` class using the facade pattern, in which it is in charge of handling the execution of user inputs from the UI component. This component only relies on the Parser component and Storage component and works independently from the UI component.

## Logic Class
![Logic](/docs/UML Diagrams/Logic sequence diagram.png)
> Figure 4: Sequence Diagram when a add command is given

The `Logic` class contains the methods that handle the core functionality of Urgenda. It can be thought of as the "processor" of Urgenda. User inputs are passed to the `executeCommand(String,int)` to determine the corresponding command object based on the user input by the `Parser` component.

After knowing the type of command, `Logic`retrieves the updated state and data per launch time from `LogicData` via the `UpdateSate()` method call. After which the command object will be passed to `LogicCommand` for process through the `processCommand(Command)` method call. The command will then be executed, and `LogicData` will updates its relevant fields. `LogicData` maintains a set of data same as that displayed to the user per launch time so as to facillitate number pointing of task and reduce dependency with `Storage` component (e.g. when user inputs delete 4, `Logic` is able to determine which is task 4 without having to call `Storage`). `Storage` component will then store the data to ensure no loss of user data upon unintentional early termination of Urgenda Program. More details of the storing procedure are mentioned in the `Storage` section.

The executeCommand(String) method will then return the appropriate feedback to its caller method. The caller method can then decide how to update the user interface.

## LogicData Class
# Command Component
![Command](https://github.com/cs2103jan2016-t16-3j/main/blob/master/docs/UML%20Diagrams/Command.png?raw=true)
> Figure 5: Structure of Command component where the Command Pattern is used

`Command` is an abstract class that uses the Command Pattern and holds the `execute()` method where the generic execution of `Command.execute()` can be used. Classes that extends from it will have their own implementation of the `execute()` method. `TaskCommand` is another abstract class which extends `Command` and is for commands that deal with manipulation of Task objects. `TaskCommand` has two abstract functions which are `Undo()` and `Redo()` which are also implemented separately by the child classes to revert the changes made by that command. 

## AddTask Class
## BlockSlots Class
## Command Class
## Complete Class
## Edit Class
## Exit Class
## Help Class
## Prioritise Class
## Redo Class
## Search Class
## ShowArchive Class
## ShowDetails Class
## Undo Class
## Undoable Class
# Parser Component
## Parser Class
# Storage Component
![Storage](/docs/UML Diagrams/Storage.png)
> Figure 6: Structure of Storage component

The Storage component is accessible through the `Storage` class using the facade pattern, where it handles and directs file manipulation using the respective classes. 
## Storage Class
## FileEditor Class
## JsonCipher Class
## Encryptor Class
## Decryptor Class
## SettingsEditor Class
