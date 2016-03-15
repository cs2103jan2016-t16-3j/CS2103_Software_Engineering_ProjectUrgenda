package urgenda.parser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.ocpsoft.prettytime.nlp.PrettyTimeParser;
import org.ocpsoft.prettytime.nlp.parse.DateGroup;

public class DateTimeParser {
	public static String searchTaskTimes(String argsString) {
		ArrayList<Date> dateWithoutTime = searchDateWithoutTime(argsString);
		List<DateGroup> dateGroups = new PrettyTimeParser().parseSyntax(argsString);
		int numberOfDateGroup = dateGroups.size();

		switch (numberOfDateGroup) {
		case 0:
			return argsString;
		case 1:
			return handlel1DateGroup(argsString, dateGroups);
		case 2:
			return handle2DateGroups(argsString, dateGroups);
		case 3:
			return handle3DateGroups(argsString, dateGroups);
		case 4:
			return handle4DateGroups(argsString, dateGroups);
		default:
			dateGroups = reselectDateGroups(dateGroups);
			return handle4DateGroups(argsString, dateGroups);
		}
	}

	private static ArrayList<Date> searchDateWithoutTime(String argsSring) {
		PrettyTimeParser prettyParser = new PrettyTimeParser();
		List<Date> firstParse = prettyParser.parse(argsSring);
		List<Date> secondParse = prettyParser.parse(argsSring);
		ArrayList<Date> dateWithoutTime = new ArrayList<Date>();
		for (int i = 0; i < firstParse.size(); i++) {
			if (firstParse.get(i) != secondParse.get(i)) {
				dateWithoutTime.add(firstParse.get(i));
			}
		}
		return dateWithoutTime;
	}

	private static List<DateGroup> reselectDateGroups(List<DateGroup> dateGroups) {
		int startIndex = dateGroups.size() - 4;
		int endIndex = dateGroups.size() - 1;
		return dateGroups.subList(startIndex, endIndex);
	}

	private static String handlel1DateGroup(String argsString, List<DateGroup> dateGroups) {
		int numberOfDates = dateGroups.get(0).getDates().size();
		switch (numberOfDates) {
		case 1:
			int parsePosition = dateGroups.get(0).getPosition();
			String dateString = dateGroups.get(0).getText();
			String preceedingWord = PublicFunctions.getPreceedingWord(parsePosition, argsString);
			if (PublicVariables.startTimeWords.contains(preceedingWord)) {
				PublicVariables.taskStartTime = dateGroups.get(0).getDates().get(0);
				return PublicFunctions.reselectString(argsString, preceedingWord + " " + dateString);
			} else if (PublicVariables.endTimeWords.contains(preceedingWord)) {
				PublicVariables.taskEndTime = dateGroups.get(0).getDates().get(0);
				return PublicFunctions.reselectString(argsString, preceedingWord + " " + dateString);
			} else {
				PublicVariables.taskStartTime = dateGroups.get(0).getDates().get(0);
				return PublicFunctions.reselectString(argsString, dateString);
			}
		case 2:
			
		case 3:
		default:

		}
	}

	private static String handle2DateGroups(String argsString, List<DateGroup> dateGroups) {

	}

	private static String handle3DateGroups(String argsString, List<DateGroup> dateGroups) {

	}

	private static String handle4DateGroups(String argsString, List<DateGroup> dateGroups) {

	}

}
