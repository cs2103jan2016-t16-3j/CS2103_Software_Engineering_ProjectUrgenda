//@@author A0127764X
package urgenda.parser.commandParser;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.ocpsoft.prettytime.nlp.PrettyTimeParser;
import org.ocpsoft.prettytime.nlp.parse.DateGroup;

import urgenda.command.*;
import urgenda.parser.DateTimeParser;
import urgenda.parser.PublicFunctions;
import urgenda.parser.PublicVariables;
import urgenda.parser.TaskDetailsParser;
import urgenda.parser.PublicVariables.COMMAND_TYPE;
import urgenda.util.Task;

public class NewEditCommandParser {
	private static String _argsString;
	private static int _index;

	private static String startFlagRegex = "(-s)|(-s:)|(from)";
	private static String endFlagRegex = "(-e)|(-e:)|(to)|(by)";
	private static String removeFlagRegex = "((-r)|(-rm))";
	private static String locationRegex = "(@)";
	private static String combinedRegex = "((\\A|\\D)(" + startFlagRegex + "|" + endFlagRegex + ")(\\Z|\\D))|" + locationRegex;

	private static LocalDateTime startTime;
	private static LocalDateTime endTime;
	private static LocalDateTime unknownTime;
	private static String descString;
	private static String location;
	private static Integer index;

	public NewEditCommandParser(String argsString, int index) {
		_argsString = argsString;
		_index = index;
	}

	public static Command generateAndReturn() {
		if (_argsString == null) {
			return new Invalid();
		} else {
			_argsString = PublicFunctions.reformatArgsString(_argsString).trim();
			reinitializeVariables();
			String reducedString = searchIndex();
			searchDetails(reducedString);
			int numberOfRmFlag = countRmFlag(reducedString);
			
//			System.out.print(startTime + "\n");
//			System.out.print(endTime + "\n");
//			System.out.print(unknownTime + "\n");
//			System.out.print(descString + "\n");
//			System.out.print(numberOfRmFlag + "\n");
			
			Edit editCommand = new Edit();
			Task newTask = new Task();
			if (startTime != null) {
				newTask.setStartTime(startTime);
			}
			if (endTime != null) {
				newTask.setEndTime(endTime);
			}
			if (descString != null) {
				newTask.setDesc(descString);
			}
			if (location != null) {
				newTask.setLocation(location);
			}
			if (unknownTime != null) {
				editCommand.setUnknown(unknownTime);
			}
			if (index != null) {
				editCommand.setId(index);
			} else {
				editCommand.setId(_index);
			}
			editCommand.setNewTask(newTask);

			switch (numberOfRmFlag) {
			case 0:
				return editCommand;
			case 1:
				editCommand.setIsRemovedOnce();
				return editCommand;
			case 2:
				editCommand.setIsRemovedOnce();
				editCommand.setIsRemovedTwice();
				return editCommand;
			case 3:
				return new Invalid();
			}
		}
		return new Invalid();
	}

	private static int countRmFlag(String reducedString) {
		int index = reducedString.indexOf("-r");
		int count = 0;
		while (index != -1) {
		    count++;
		    reducedString = reducedString.substring(index + 1);
		    index = reducedString.indexOf("-r");
		}
		index = reducedString.indexOf("-rm");
		while (index != -1) {
		    count++;
		    reducedString = reducedString.substring(index + 1);
		    index = reducedString.indexOf("-rm");
		}
		return count;
	}

	private static String searchIndex() {
		String firstWord = PublicFunctions.getFirstWord(_argsString);
		try {
			index = Integer.parseInt(firstWord) - 1;
			return PublicFunctions.removeFirstWord(_argsString);
		} catch (Exception e) {
			return _argsString;
		}
	}

	private static void searchDetails(String argsString) {
		String temp = argsString;
		String[] stringArray = temp.trim().split(combinedRegex);
		if (stringArray.length == 0) {
			unknownTime = parseUnknownTime(temp);
		} else {
			for (int i = 0; i < stringArray.length; i++) {	
				int position = temp.indexOf(stringArray[i].trim());
				String preceedingWord = PublicFunctions.getPreceedingWord(position, temp);
				if (preceedingWord.equals("-s") || preceedingWord.equals("-s:") || preceedingWord.equals("from")) {
					startTime = parseStartTime(stringArray[i].trim());
				} else if (preceedingWord.equals("-e") || preceedingWord.equals("-e:") || preceedingWord.equals("to")
						|| preceedingWord.equals("by")) {
					endTime = parseEndTime(stringArray[i].trim());
				} else if (preceedingWord.equals("@")) {
					location = stringArray[i].trim();
				} else {
					unknownTime = parseUnknownTime(stringArray[i].replaceAll("(\\A|\\D)((-rm)|(-r))(\\Z|\\D)","").trim());
					if (i==0 && unknownTime == null) {
						String[] array = stringArray[i].split(removeFlagRegex);
						if (array.length == 2) {
							unknownTime = parseUnknownTime(array[1].trim());
							if (unknownTime != null) {
								descString = array[0];
							} else {
								descString = stringArray[i].trim();							
							}
						} else {
							descString = stringArray[i].replaceAll("((-rm)|(-r))","").trim();
						}
					}
				}
			}
		}
	}

	// taken from DateTimeParser, refractor later
	private static LocalDateTime parseStartTime(String argsString) {
		List<DateGroup> dateGroups = new PrettyTimeParser().parseSyntax(argsString);
		if (dateGroups.size() == 1) {
			String parsedString = dateGroups.get(0).getText();
			if (dateGroups.get(0).getDates().size() == 1 && parsedString.trim().equals(argsString)) {
				List<DateGroup> dateGroups2 = new PrettyTimeParser().parseSyntax(argsString);
				Date firstParseDate = dateGroups.get(0).getDates().get(0);
				Date secondParseDate = dateGroups2.get(0).getDates().get(0);
				LocalDateTime localDateTime = PublicFunctions.getLocalDateTimeFromDate(firstParseDate);
				if (firstParseDate.equals(secondParseDate)) {
					return localDateTime;
				} else {
					return DateTimeParser.adjustedDateEvent(localDateTime);
				}
			}
		}
		return null;
	}

	private static LocalDateTime parseEndTime(String argsString) {
		List<DateGroup> dateGroups = new PrettyTimeParser().parseSyntax(argsString);
		if (dateGroups.size() == 1) {
			String parsedString = dateGroups.get(0).getText();
			if (dateGroups.get(0).getDates().size() == 1 && parsedString.trim().equals(argsString)) {
				List<DateGroup> dateGroups2 = new PrettyTimeParser().parseSyntax(argsString);
				Date firstParseDate = dateGroups.get(0).getDates().get(0);
				Date secondParseDate = dateGroups2.get(0).getDates().get(0);
				LocalDateTime localDateTime = PublicFunctions.getLocalDateTimeFromDate(firstParseDate);
				if (firstParseDate.equals(secondParseDate)) {
					return localDateTime;
				} else {
					return DateTimeParser.adjustedDateDeadline(localDateTime);
				}
			}
		}
		return null;
	}

	private static LocalDateTime parseUnknownTime(String argsString) {
		List<DateGroup> dateGroups = new PrettyTimeParser().parseSyntax(argsString);
		if (dateGroups.size() == 1) {
			String parsedString = dateGroups.get(0).getText();
			if (dateGroups.get(0).getDates().size() == 1 && parsedString.trim().equals(argsString)) {
				Date parseDate = dateGroups.get(0).getDates().get(0);
				LocalDateTime localDateTime = PublicFunctions.getLocalDateTimeFromDate(parseDate);
				return localDateTime;
			}
		}
		return null;
	}
	
//	private static String searchLocation(String string) {
//		if (string == null || string.trim().equals("")) {
//			return "";
//		} else {
//			Matcher matcher = Pattern.compile("@\\")
//		}
//	}
	
	private static void reinitializeVariables() {
		startTime = null;
		endTime = null;
		unknownTime = null;
		descString = null;
		location = null;
		index = null;
	}
}
