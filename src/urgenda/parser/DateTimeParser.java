package urgenda.parser;

import java.time.LocalDateTime;
import java.time.ZoneId;
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

	// need refractoring after done
	private static String handlel1DateGroup(String argsString, List<DateGroup> dateGroups) {
		int numberOfDates = dateGroups.get(0).getDates().size();
		int parsePosition = dateGroups.get(0).getPosition();
		String dateString = dateGroups.get(0).getText();
		String preceedingWord = PublicFunctions.getPreceedingWord(parsePosition, argsString);
		Date timeInDate;
		switch (numberOfDates) {
		case 1:
			if (PublicVariables.startTimeWords.contains(preceedingWord)) {
				timeInDate = dateGroups.get(0).getDates().get(0);
				PublicVariables.taskStartTime = getLocalDateTimeFromDate(timeInDate);
				PublicVariables.taskEndTime = PublicVariables.taskStartTime.plusHours(1);
				return PublicFunctions.reselectString(argsString, preceedingWord + " " + dateString);
			} else if (PublicVariables.endTimeWords.contains(preceedingWord)) {
				timeInDate = dateGroups.get(0).getDates().get(0);
				PublicVariables.taskEndTime = getLocalDateTimeFromDate(timeInDate);
				return PublicFunctions.reselectString(argsString, preceedingWord + " " + dateString);
			} else {
				timeInDate = dateGroups.get(0).getDates().get(0);
				PublicVariables.taskStartTime = getLocalDateTimeFromDate(timeInDate);
				PublicVariables.taskEndTime = PublicVariables.taskStartTime.plusHours(1);
				return PublicFunctions.reselectString(argsString, dateString);
			}
		case 2:
			timeInDate = dateGroups.get(0).getDates().get(0);
			PublicVariables.taskStartTime = getLocalDateTimeFromDate(timeInDate);
			timeInDate = dateGroups.get(0).getDates().get(1);
			PublicVariables.taskEndTime = getLocalDateTimeFromDate(timeInDate);
			if (PublicVariables.startTimeWords.contains(preceedingWord)
					|| PublicVariables.periodWords.contains(preceedingWord)) {
				return PublicFunctions.reselectString(argsString, preceedingWord + " " + dateString);
			} else {
				return PublicFunctions.reselectString(argsString, dateString);
			}
		case 3:
			Date time1 = dateGroups.get(0).getDates().get(0);
			Date time2 = dateGroups.get(0).getDates().get(1);
			Date time3 = dateGroups.get(0).getDates().get(2);
			
			if (time1.equals(time2) || (time1.equals(time3))) {
				PublicVariables.taskStartTime = getLocalDateTimeFromDate(getMin(time1,time3));
				PublicVariables.taskEndTime = getLocalDateTimeFromDate(getMax(time1,time3));
			} else if (time2.equals(time3)) {
				PublicVariables.taskStartTime = getLocalDateTimeFromDate(getMin(time1,time2));
				PublicVariables.taskEndTime = getLocalDateTimeFromDate(getMax(time1,time2));
			} else {
				PublicVariables.taskStartTime = getLocalDateTimeFromDate(getMin(time2,time3));
				PublicVariables.taskEndTime = getLocalDateTimeFromDate(getMax(time2,time3));
			}
			
			if (PublicVariables.startTimeWords.contains(preceedingWord)
					|| PublicVariables.periodWords.contains(preceedingWord)) {
				return PublicFunctions.reselectString(argsString, preceedingWord + " " + dateString);
			} else {
				return PublicFunctions.reselectString(argsString, dateString);
			}			
		default:
			// can add handler
			return argsString;
		}
	}

	private static String handle2DateGroups(String argsString, List<DateGroup> dateGroups) {
		
	}

	private static String handle3DateGroups(String argsString, List<DateGroup> dateGroups) {

	}

	private static String handle4DateGroups(String argsString, List<DateGroup> dateGroups) {

	}

	public static LocalDateTime getLocalDateTimeFromDate(Date date) {
		return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
	}
	
	public static Date getMin(Date date1, Date date2) {
		if (date1.compareTo(date2) < 0) {
			return date1;
		} else {
			return date2;
		}
	}
	
	public static Date getMax(Date date1, Date date2) {
		if (date1.compareTo(date2) < 0) {
			return date2;
		} else {
			return date1;
		}
	}
}
