#User Guide

This guide helps you with understanding how to use Urgenda effectively as a task manager for your daily needs.

##Table of Contents
* [Getting Started](#getting-started)
* [Feature Details](#feature-details)
	* [Add](#add)
	* [Delete](#delete)
	* [Mark as Complete](#mark-as-completed)
	* [Edit](#edit)
	* [Search](#search)
	* [Exit](#exit)
* [Advanced Features](#advanced-features)
	* [Show more details](#show-more-details)
	* [Archive](#archive)
	* [Undo/Redo](#undo/redo)
	* [Block Multiple Timeslot](#block-multiple-timeslot)
	* [Prioritise](#prioritise)
* [Shortcuts](#shortcuts)

# Getting Started

```java
//TODO: insert image
```

1. User input/command bar: Enter your to-dos here easily using the given Command Tags.
2. Feedback prompt: The outcome to any changes that you make to your tasks is shown here.
3. Display screen: Your tasks are categorised and displayed here according to time and priority by default.
4. Task category: This shows the type or category that the displayed tasks belong to (e.g. ALL TASKS,
OVERDUE TASKS, #Assignment, etc.)
5. Task header: This indicates each sub-category for different tasks.
6. Exit button: Click to exit the program. Alternatively, type exit in the command bar to exit.

# Feature Details

## Add

To create a new task, **Add** or **Create** are the command words, with the task name following thereafter, ie "Dinner with Mum"

Use **Command Tags** to add in details for the task, "**at**" for time, "**@**" for location, etc. For the **full list of Command Tags**, refer to [Shortcuts](#shortcuts)

Tasks are separated into 3 categories, Events, Deadlines and Floatings. 
* Event: Task is given with a start time and end time.
Example: _**Add** Dinner with Mum **at** 23/3/2016 7:00pm to 9:00pm_
	* If only a start time is given, then end time will automatically be set to 1 hour after the start time. 
	```java
	//TODO: insert image
	```

* Deadline: Task is given with only an end time.
Example: _**Add** Return home **by** 23/3/2016 7:00pm_
	```java
	//TODO: insert image
	```

* Floating: Task is given with no start time and end time. 
Example: _**Add** Dinner with Mum_
	```java
	//TODO: insert image
	```

## Delete

To delete an existing task, **Delete**, **Del**, **Erase** or **Remove** are the command words that can be used to delete a task. Note that deleting a task _IS NOT_ completing a task. 

Specify the task number, the task description or simply highlight the task to be deleted. 

Example:
* _**Delete** Dinner with Mum_
* _**Delete** 4_

```java
//TODO: insert image
```

## Mark as Completed

To mark a task as done, **Done**, **Completed**, **Do**, **Mark**, **Finish**, **Fin** are the command words that can be used. 

Similar to [Delete](#delete), specify the task number, the task description or simply highlight the task to be marked as completed.

Example:
* _**Mark** Dinner with Mum_
* _**Done** 3_

```java
//TODO: insert image
```

## Edit

To edit an existing task, **Edit**, **Update**, **Change** and **Mod** are the commands that can be used. 

Use **Command Tags** to change details for the task, "**at**" for time, "**@**" for location, etc. For the **full list of Command Tags**, refer to [Shortcuts](#shortcuts)

If the task already has those details, then the new details will overwrite the old details. 

Similar to [Delete](#delete) and [Mark as Completed](#mark-as-completed), specify the task number, the task description or simply highlight the task to be edited.

Example: 
* _**Change** Dinner with Mum **@** NEX
* _**Edit** 4 Dinner with Mum and Dad

```java
//TODO: insert image
```

## Search

To search for a word, **Search**, **Fine**, **Show**, **View** or **List** are the commands that can be used. 

**#** searches for the tags, and thereafter displaying the tasks.
* When searching for **#cow**, tasks with tags **#cows** and **#cowmilk** will also appear in the search result. 

When searching for a date, all tasks with this particular date set as the deadline will be displayed. 
When searching for a word or phrase, all tasks that contain this particular work or phrase in their task description(s) will be displayed. 
When searching for time, time block with assigned tasks will be displayed. 

Entering the search commands alone will simply show all existing tasks. 

Example:
_**Search** boss_
* _Report to boss by 4/4/2016 3:00pm_
* _Meeting with boss at 6/4/2016 2:00pm to 5:00pm_

```java
//TODO: insert image
```

## Exit

To exit Urgenda, click the top right exit button, or type **exit**

# Advanced Features

## Show more details

**Showmore** is a specific command to expand a specific task for the user to view more ddetails about that task, such as the locaiton, the tags for that task, or for any other additional details. 

```java
//TODO: insert image
```

## Archive

To show previously completed tasks, the commands are **Archive** and **Showarchive**

```java
//TODO: insert image
```

## Undo/Redo

- **Undo** and **Redo** are classic features, allowing you to undo the previous action taken. This will result in any changes made to certain task(s) being restored. A maximum of 10 consecutive actions are possible for both **undo** and **redo**. For example:
	- _**delete** Dinner with Mom_ (Task with Dinner with Mom is deleted)
	- _**undo**_ (Task Dinner with Mom is restored with all its previous details)
- However, when undo is implemented at least once, any new commands (other than **undo** and **redo**) will discard all saved redo actions after the new action has been implemented. For example:
* _**delete** Fix lightbulb_ (Task “fix lightbulb” is deleted)
* _**undo**_ (Task “fix lightbulb” is restored)
* _**add** Meeting with boss_ (Task “Meeting with boss” is added)
* _**redo**_ (No tasks to redo, previously undone actions are cleared.)


## Block Multiple Timeslot

## Prioritise

This feature allows you to mark certain tasks as important. These tasks will be displayed with a logo next to the task.

**Urgent**, **Important**, **Impt**, **Pri** or **Pin** are the commands to prioritise a task. 
Example:
```java
//TODO: insert image
```

# Shortcuts
* Ctrl + Z / Ctrl + Y : Undoing/Redoing what was previously written in the command bar
* Ctrl + D : Displays that default view of showing all tasks
* 🔺 : Cycle through previous commands from command history
* 🔻 : Cycle through next commands from command history
* Ctrl + 🔺, Ctrl + 🔻 : Changes the highlighted task on graphical display
* Alt + F4 : Exits Urgenda
* Ctrl + Alt + D : Quick launch of Urgenda
