//@@author A0127764X
package urgenda.parser;

import urgenda.parser.PublicVariables.*;

public class CommandTypeParser {
	
	/**
	 * This function parses the command string to determine the type of command being input
	 * @param commandString The user's input command string
	 * @return The enum for the type of command of the input
	 */
	public static COMMAND_TYPE getCommandType(String commandString) {
		String lowerCaseFirstWord = PublicFunctions.getFirstWord(commandString).toLowerCase();

		if (PublicVariables.deleteKeyWords.contains(lowerCaseFirstWord)) {
			return COMMAND_TYPE.DELETE;
		} else if (PublicVariables.addKeyWords.contains(lowerCaseFirstWord)) {
			return COMMAND_TYPE.ADD;
		} else if (PublicVariables.doneKeyWords.contains(lowerCaseFirstWord)) {
			return COMMAND_TYPE.COMPLETE;
		} else if (PublicVariables.updateKeyWords.contains(lowerCaseFirstWord)) {
			return COMMAND_TYPE.EDIT;
		} else if (PublicVariables.searchKeyWords.contains(lowerCaseFirstWord)) {
			return COMMAND_TYPE.SEARCH;
		} else if (PublicVariables.showDetailsKeyWords.contains(lowerCaseFirstWord)) {
			return COMMAND_TYPE.SHOW_DETAILS;
		} else if (PublicVariables.blockKeyWords.contains(lowerCaseFirstWord)) {
			return COMMAND_TYPE.BLOCK;
		} else if (PublicVariables.undoKeywords.contains(lowerCaseFirstWord)) {
			return COMMAND_TYPE.UNDO;
		} else if (PublicVariables.findFreeKeyWords.contains(lowerCaseFirstWord)) {
			return COMMAND_TYPE.FIND_FREE;
		} else if (PublicVariables.redoKeywords.contains(lowerCaseFirstWord)) {
			return COMMAND_TYPE.REDO;
		} else if (PublicVariables.archiveKeyWords.contains(lowerCaseFirstWord)) {
			return COMMAND_TYPE.SHOW_ARCHIVE;
		} else if (PublicVariables.prioritiseKeyWords.contains(lowerCaseFirstWord)) {
			return COMMAND_TYPE.PRIORITISE;
		} else if (PublicVariables.homeKeyWords.contains(lowerCaseFirstWord)) {
			return COMMAND_TYPE.HOME;
		} else if (PublicVariables.postponeKeyWords.contains(lowerCaseFirstWord)) {
			return COMMAND_TYPE.POSTPONE;
		} else if (PublicVariables.confirmKeyWords.contains(lowerCaseFirstWord)) {
			return COMMAND_TYPE.CONFIRM;
		} else if (PublicVariables.setDirectoryKeyWords.contains(lowerCaseFirstWord)) {
			return COMMAND_TYPE.SET_DIRECTORY;
		} else if (PublicVariables.exitKeyWords.contains(lowerCaseFirstWord)) {
			return COMMAND_TYPE.EXIT;
		} else if (PublicVariables.helpKeyWords.contains(lowerCaseFirstWord)) {
			return COMMAND_TYPE.HELP;
		} else if (PublicVariables.demoKeyWords.contains(lowerCaseFirstWord)) {
			return COMMAND_TYPE.DEMO;
		} else if (PublicVariables.hideKeyWords.contains(lowerCaseFirstWord)) {
			return COMMAND_TYPE.HIDE;
		} else {
			return COMMAND_TYPE.INVALID;
		}
	}

	
	/**
	 * Removes the first word (command word) from user's input and return the rest of the string (arguments) to be parsed
	 * @param commandString user's input
	 * @return the argument string with command word removed
	 */
	public static String getArgsString(String commandString) {
		return PublicFunctions.removeFirstWord(commandString);
	}
}
