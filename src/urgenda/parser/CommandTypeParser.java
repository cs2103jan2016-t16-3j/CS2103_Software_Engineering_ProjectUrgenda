package urgenda.parser;

import urgenda.parser.PublicVariables.*;

public class CommandTypeParser {
	public static COMMAND_TYPE getCommandType(String commandString) {
		String lowerCaseFirstWord = PublicFunctions.getFirstWord(commandString).toLowerCase();

		if (PublicVariables.deleteKeyWords.contains(lowerCaseFirstWord)) {
			return COMMAND_TYPE.DELETE;
		} else if (PublicVariables.addKeyWords.contains(lowerCaseFirstWord)) {
			return COMMAND_TYPE.ADD;
		} else if (PublicVariables.allocateKeyWords.contains(lowerCaseFirstWord)) {
			return COMMAND_TYPE.ALLOCATE;
		} else if (PublicVariables.doneKeyWords.contains(lowerCaseFirstWord)) {
			return COMMAND_TYPE.COMPLETE;
		} else if (PublicVariables.updateKeyWords.contains(lowerCaseFirstWord)) {
			return COMMAND_TYPE.EDIT;
		} else if (PublicVariables.searchKeyWords.contains(lowerCaseFirstWord)) {
			return COMMAND_TYPE.SEARCH;
		} else if (PublicVariables.showDetailsKeyWords.contains(lowerCaseFirstWord)) {
			return COMMAND_TYPE.SHOW_DETAILS;
		} else if (PublicVariables.undoKeywords.contains(lowerCaseFirstWord)) {
			return COMMAND_TYPE.UNDO;
		} else if (PublicVariables.redoKeywords.contains(lowerCaseFirstWord)) {
			return COMMAND_TYPE.REDO;
		} else if (PublicVariables.archiveKeyWords.contains(lowerCaseFirstWord)) {
			return COMMAND_TYPE.SHOW_ARCHIVE;
		} else if (PublicVariables.prioritiseKeyWords.contains(lowerCaseFirstWord)) {
			return COMMAND_TYPE.PRIORITISE;
		} else if (PublicVariables.exitKeyWords.contains(lowerCaseFirstWord)) {
			return COMMAND_TYPE.EXIT;
		} else {
			return COMMAND_TYPE.INVALID;
		}
	}
	
	public static String getArgsString(String commandString) {
		return PublicFunctions.removeFirstWord(commandString);
	}
}
