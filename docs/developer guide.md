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
![Architecture](https://cloud.githubusercontent.com/assets/17000137/13753825/9952069c-ea4f-11e5-8e69-a18c7dcdb6cd.png)
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
![Logic](/docs/UML Diagrams/Logic.png)
> Figure 3: Structure of Logic component
## Logic Class
## LogicData Class
# Command Component
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
> Figure 4: Structure of Storage component

The Storage component is accessible through the `Storage` class using the facade pattern, where it handles and directs file manipulation using the respective classes. 
## Storage Class
## FileEditor Class
## JsonCipher Class
## Encryptor Class
## Decryptor Class
## SettingsEditor Class
