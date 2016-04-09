//@@author A0127764X
package urgenda.parser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.ocpsoft.prettytime.nlp.PrettyTimeParser;
import org.ocpsoft.prettytime.nlp.parse.DateGroup;
import org.ocpsoft.prettytime.shade.net.fortuna.ical4j.model.DateTime;

import urgenda.util.MultipleSlot;

public class DateTimeParser {
	private static ArrayList<Date> dateWithoutTime;
	private static List<DateGroup> dateGroups;
	private static String _argsString;
	
	private static String whiteSpace = " ";

	public static String searchTaskTimes(String argsString) {
		_argsString = argsString;
		dateGroups = new PrettyTimeParser().parseSyntax(_argsString);
		dateWithoutTime = searchDatesWithoutTime();
		int numberOfDateGroup = dateGroups.size();

		switch (numberOfDateGroup) {
		case 0:
			return argsString;
		case 1:
			return handlel1DateGroup();
		case 2:
			return handle2DateGroups();
		default:
			dateGroups = reselectDateGroups();
			return handle2DateGroups();
		}
	}

	private static ArrayList<Date> searchDatesWithoutTime() {
		PrettyTimeParser prettyParser = new PrettyTimeParser();
		List<DateGroup> secondParse = prettyParser.parseSyntax(_argsString);
		ArrayList<Date> dateWithoutTime = new ArrayList<Date>();
		for (int i = 0; i < dateGroups.size(); i++) {
			for (int j = 0; j < dateGroups.get(i).getDates().size(); j++) {
				if (!dateGroups.get(i).getDates().get(j).equals(secondParse.get(i).getDates().get(j))) {
					dateWithoutTime.add(dateGroups.get(i).getDates().get(j));
				}
			}
		}

		return dateWithoutTime;
	}

	private static List<DateGroup> reselectDateGroups() {
		int startIndex = dateGroups.size() - 2;
		int endIndex = dateGroups.size() - 1;
		return dateGroups.subList(startIndex, endIndex);
	}

	// need refractoring after done
	private static String handlel1DateGroup() {
		int numberOfDates = dateGroups.get(0).getDates().size();
		int parsePosition = dateGroups.get(0).getPosition();
		String dateString = dateGroups.get(0).getText();
		String preceedingWord = PublicFunctions.getPreceedingWord(parsePosition, _argsString);
		switch (numberOfDates) {
		case 1:
			return handle1DateIn1DateGroup(dateString, preceedingWord);
		case 2:
			return handle2DatesIn1DateGroup(dateString, preceedingWord);
		case 3:
			return handle3DatesIn1DateGroup(dateString, preceedingWord);
		default:
			return _argsString;
		}
	}

	private static String handle3DatesIn1DateGroup(String dateString, String preceedingWord) {
		Date time1 = dateGroups.get(0).getDates().get(0);
		Date time2 = dateGroups.get(0).getDates().get(1);
		Date time3 = dateGroups.get(0).getDates().get(2);

		Date minDate;
		Date maxDate;
		if (time1.equals(time2) || (time1.equals(time3))) {
			minDate = getMin(time1, time3);
			maxDate = getMax(time1, time3);
		} else if (time2.equals(time3)) {
			minDate = getMin(time1, time2);
			maxDate = getMax(time1, time2);
		} else {
			minDate = getMin(time3, time2);
			maxDate = getMax(time3, time2);
		}

		LocalDateTime minDateTime = getLocalDateTimeFromDate(minDate);
		LocalDateTime maxDateTime = getLocalDateTimeFromDate(maxDate);
		if (isDateOnly(minDate)) {
			minDateTime = adjustedDateEvent(minDateTime);
		}
		if (isDateOnly(maxDate)) {
			maxDateTime = adjustedDateEvent(maxDateTime);
		}

		setTaskStartTime(minDateTime);
		setTaskEndTime(maxDateTime);

		if (isStartKeyWords(preceedingWord)) {
			return PublicFunctions.reselectString(_argsString, preceedingWord + whiteSpace + dateString);
		} else {
			return PublicFunctions.reselectString(_argsString, dateString);
		}
	}

	private static boolean isStartKeyWords(String preceedingWord) {
		return PublicVariables.startTimeWords.contains(preceedingWord)
				|| PublicVariables.periodWords.contains(preceedingWord);
	}

	private static String handle2DatesIn1DateGroup(String dateString, String preceedingWord) {
		Date timeInDate;
		timeInDate = dateGroups.get(0).getDates().get(0);
		LocalDateTime dateTime1 = getLocalDateTimeFromDate(timeInDate);
		if (isDateOnly(timeInDate)) {
			dateTime1 = adjustedDateEvent(dateTime1);
		}

		timeInDate = dateGroups.get(0).getDates().get(1);
		LocalDateTime dateTime2 = getLocalDateTimeFromDate(timeInDate);
		if (isDateOnly(timeInDate)) {
			dateTime2 = adjustedDateEvent(dateTime2);
		}

		setTaskStartTime(dateTime1);
		setTaskEndTime(dateTime2);

		if (isStartKeyWords(preceedingWord)) {
			return PublicFunctions.reselectString(_argsString, preceedingWord + whiteSpace + dateString);
		} else {
			return PublicFunctions.reselectString(_argsString, dateString);
		}
	}

	private static String handle1DateIn1DateGroup(String dateString, String preceedingWord) {
		Date timeInDate;
		timeInDate = dateGroups.get(0).getDates().get(0);
		LocalDateTime dateTime = getLocalDateTimeFromDate(timeInDate);

		if (PublicVariables.startTimeWords.contains(preceedingWord)) {
			if (isDateOnly(timeInDate)) {
				dateTime = adjustedDateEvent(dateTime);
			}
			setTaskStartTime(dateTime);
			setTaskEndTime(dateTime.plusHours(1));
			return PublicFunctions.reselectString(_argsString, preceedingWord + whiteSpace + dateString);
		} else if (PublicVariables.endTimeWords.contains(preceedingWord)) {
			if (isDateOnly(timeInDate)) {
				dateTime = adjustedDateDeadline(dateTime);
			}
			setTaskEndTime(dateTime);
			return PublicFunctions.reselectString(_argsString, preceedingWord + whiteSpace + dateString);
		} else {
			if (isDateOnly(timeInDate)) {
				dateTime = adjustedDateEvent(dateTime);
			}
			setTaskStartTime(dateTime);
			setTaskEndTime(dateTime.plusHours(1));
			return PublicFunctions.reselectString(_argsString, dateString);
		}
	}

	private static String handle2DateGroups() {
		int numberOfDatesInGroup1 = dateGroups.get(0).getDates().size();
		int numberOfDatesInGroup2 = dateGroups.get(1).getDates().size();

		if (numberOfDatesInGroup1 == 1 && numberOfDatesInGroup2 == 2) {
			return handleOneDateTwoDates();
		} else if (numberOfDatesInGroup1 == 2 && numberOfDatesInGroup2 == 1) {
			return handleTwoDatesOneDate();
		} else if (numberOfDatesInGroup1 == 1 && numberOfDatesInGroup2 == 1) {
			return handleOneDateOneDate();
		} else {
			return _argsString;
		} 
	}

	private static String handleOneDateOneDate() {
		Date group1Date = dateGroups.get(0).getDates().get(0);
		Date group2Date = dateGroups.get(1).getDates().get(0);

		if (is1TimeOnly1DateOnly(group1Date, group2Date)) {
			return handle1DateOnly1TimeOnly();
		} else if (isBothDateOnly(group1Date, group2Date)) {
			return _argsString;
		} else {
			return handleBothTimeOnly();
		}
	}

	private static String handleBothTimeOnly() {
		int parsePositionGroup1 = dateGroups.get(0).getPosition();
		String dateStringGroup1 = dateGroups.get(0).getText();
		String preceedingWordGroup1 = PublicFunctions.getPreceedingWord(parsePositionGroup1, _argsString);

		int parsePositionGroup2 = dateGroups.get(1).getPosition();
		String dateStringGroup2 = dateGroups.get(1).getText();
		String preceedingWordGroup2 = PublicFunctions.getPreceedingWord(parsePositionGroup2, _argsString);
		
		Date group1Date = dateGroups.get(0).getDates().get(0);
		Date group2Date = dateGroups.get(1).getDates().get(0);
		LocalDateTime group1LocalDate = getLocalDateTimeFromDate(group1Date);
		LocalDateTime group2LocalDate = getLocalDateTimeFromDate(group2Date);
		setTaskStartTime(group1LocalDate);
		setTaskEndTime(group2LocalDate);

		String reducedArgsString;

		if (PublicVariables.startTimeWords.contains(preceedingWordGroup1)) {
			reducedArgsString = PublicFunctions.reselectString(_argsString,
					preceedingWordGroup1 + whiteSpace + dateStringGroup1);
		} else {
			reducedArgsString = PublicFunctions.reselectString(_argsString, dateStringGroup2);
		}

		if (PublicVariables.endTimeWords.contains(preceedingWordGroup2)) {
			reducedArgsString = PublicFunctions.reselectString(reducedArgsString,
					preceedingWordGroup2 + whiteSpace + dateStringGroup2);
		} else {
			reducedArgsString = PublicFunctions.reselectString(reducedArgsString, dateStringGroup2);
		}

		return reducedArgsString;
	}

	private static boolean isBothDateOnly(Date group1Date, Date group2Date) {
		return isDateOnly(group2Date) && isDateOnly(group1Date);
	}

	private static boolean is1TimeOnly1DateOnly(Date group1Date, Date group2Date) {
		return (isDateOnly(group1Date) && isTimeOnly(group2Date))
				|| (isDateOnly(group2Date) && isTimeOnly(group1Date));
	}

	private static String handle1DateOnly1TimeOnly() {
		int parsePositionGroup1 = dateGroups.get(0).getPosition();
		String dateStringGroup1 = dateGroups.get(0).getText();
		String preceedingWordGroup1 = PublicFunctions.getPreceedingWord(parsePositionGroup1, _argsString);

		int parsePositionGroup2 = dateGroups.get(1).getPosition();
		String dateStringGroup2 = dateGroups.get(1).getText();
		String preceedingWordGroup2 = PublicFunctions.getPreceedingWord(parsePositionGroup2, _argsString);
		
		Date group1Date = dateGroups.get(0).getDates().get(0);
		Date group2Date = dateGroups.get(1).getDates().get(0);
		LocalDateTime group1LocalDate = getLocalDateTimeFromDate(group1Date);
		LocalDateTime group2LocalDate = getLocalDateTimeFromDate(group2Date);
		LocalDate dateComponent;
		LocalTime timeComponent;
		LocalDateTime taskDateTime;

		if (isDateOnly(group1Date) && isTimeOnly(group2Date)) {
			dateComponent = group1LocalDate.toLocalDate();
			timeComponent = group2LocalDate.toLocalTime();
			taskDateTime = LocalDateTime.of(dateComponent, timeComponent);
			System.out.print(taskDateTime + "\n");
		} else {
			dateComponent = group2LocalDate.toLocalDate();
			timeComponent = group1LocalDate.toLocalTime();
			taskDateTime = LocalDateTime.of(dateComponent, timeComponent);
		}

		if (PublicVariables.endTimeWords.contains(preceedingWordGroup1)) {
			setTaskEndTime(taskDateTime);

			if (PublicVariables.startTimeWords.contains(preceedingWordGroup2)) {
				String reducedArgsString = PublicFunctions.reselectString(_argsString,
						preceedingWordGroup2 + whiteSpace + dateStringGroup2);
				reducedArgsString = PublicFunctions.reselectString(reducedArgsString,
						preceedingWordGroup1 + whiteSpace + dateStringGroup1);

				return reducedArgsString;
			} else {
				String reducedArgsString = PublicFunctions.reselectString(_argsString, dateStringGroup2);
				reducedArgsString = PublicFunctions.reselectString(reducedArgsString,
						preceedingWordGroup1 + whiteSpace + dateStringGroup1);

				return reducedArgsString;
			}
		} else if (PublicVariables.endTimeWords.contains(preceedingWordGroup2)) {
			setTaskEndTime(taskDateTime);

			if (PublicVariables.startTimeWords.contains(preceedingWordGroup1)) {
				String reducedArgsString = PublicFunctions.reselectString(_argsString,
						preceedingWordGroup1 + whiteSpace + dateStringGroup1);
				reducedArgsString = PublicFunctions.reselectString(reducedArgsString,
						preceedingWordGroup2 + whiteSpace + dateStringGroup2);

				return reducedArgsString;
			} else {
				String reducedArgsString = PublicFunctions.reselectString(_argsString, dateStringGroup1);
				reducedArgsString = PublicFunctions.reselectString(reducedArgsString,
						preceedingWordGroup2 + whiteSpace + dateStringGroup2);

				return reducedArgsString;
			}
		} else {
			setTaskStartTime(taskDateTime);
			setTaskEndTime(taskDateTime.plusHours(1));

			String reducedArgsString;

			if (PublicVariables.startTimeWords.contains(preceedingWordGroup1)) {
				reducedArgsString = PublicFunctions.reselectString(_argsString,
						preceedingWordGroup1 + whiteSpace + dateStringGroup1);
			} else {
				reducedArgsString = PublicFunctions.reselectString(_argsString, dateStringGroup1);
			}

			if (PublicVariables.startTimeWords.contains(preceedingWordGroup2)) {
				reducedArgsString = PublicFunctions.reselectString(reducedArgsString,
						preceedingWordGroup2 + whiteSpace + dateStringGroup2);
			} else {
				reducedArgsString = PublicFunctions.reselectString(reducedArgsString, dateStringGroup2);
			}

			return reducedArgsString;
		}
	}

	private static String handleTwoDatesOneDate() {
		int parsePositionGroup1 = dateGroups.get(0).getPosition();
		String dateStringGroup1 = dateGroups.get(0).getText();
		String preceedingWordGroup1 = PublicFunctions.getPreceedingWord(parsePositionGroup1, _argsString);

		int parsePositionGroup2 = dateGroups.get(1).getPosition();
		String dateStringGroup2 = dateGroups.get(1).getText();
		String preceedingWordGroup2 = PublicFunctions.getPreceedingWord(parsePositionGroup2, _argsString);
		
		Date group2Date = dateGroups.get(1).getDates().get(0);
		Date group1Date1 = dateGroups.get(0).getDates().get(0);
		Date group1Date2 = dateGroups.get(0).getDates().get(1);
		LocalDateTime group2LocalDate = getLocalDateTimeFromDate(group2Date);
		LocalDateTime group1LocalDate1 = getLocalDateTimeFromDate(group1Date1);
		LocalDateTime group1LocalDate2 = getLocalDateTimeFromDate(group1Date2);

		if (isDateOnly(group2Date) && isTimeOnly(group1Date1) && isTimeOnly(group1Date2)) {
			LocalDate commonDate = group2LocalDate.toLocalDate();
			LocalTime startTime = group1LocalDate1.toLocalTime();
			LocalTime endTime = group1LocalDate2.toLocalTime();

			setTaskStartTime(LocalDateTime.of(commonDate, startTime));
			setTaskEndTime(LocalDateTime.of(commonDate, endTime));

			String returnedString;
			if (PublicVariables.startTimeWords.contains(preceedingWordGroup2)) {
				returnedString = PublicFunctions.reselectString(_argsString,
						preceedingWordGroup2 + whiteSpace + dateStringGroup2);
			} else {
				returnedString = PublicFunctions.reselectString(_argsString, dateStringGroup2);
			}

			if (isStartKeyWords(preceedingWordGroup1)) {
				returnedString = PublicFunctions.reselectString(returnedString,
						preceedingWordGroup1 + whiteSpace + dateStringGroup1);
			} else {
				returnedString = PublicFunctions.reselectString(returnedString, dateStringGroup1);
			}

			return returnedString;
		} else {
			setTaskStartTime(group1LocalDate1);
			setTaskEndTime(group1LocalDate2);

			if (PublicVariables.startTimeWords.contains(preceedingWordGroup1)) {
				return PublicFunctions.reselectString(_argsString, preceedingWordGroup1 + whiteSpace + dateStringGroup1);
			} else {
				return PublicFunctions.reselectString(_argsString, dateStringGroup1);
			}
		}
	}

	private static String handleOneDateTwoDates() {
		int parsePositionGroup1 = dateGroups.get(0).getPosition();
		String dateStringGroup1 = dateGroups.get(0).getText();
		String preceedingWordGroup1 = PublicFunctions.getPreceedingWord(parsePositionGroup1, _argsString);

		int parsePositionGroup2 = dateGroups.get(1).getPosition();
		String dateStringGroup2 = dateGroups.get(1).getText();
		String preceedingWordGroup2 = PublicFunctions.getPreceedingWord(parsePositionGroup2, _argsString);
		
		Date group1Date = dateGroups.get(0).getDates().get(0);
		Date group2Date1 = dateGroups.get(1).getDates().get(0);
		Date group2Date2 = dateGroups.get(1).getDates().get(1);
		LocalDateTime group1LocalDate = getLocalDateTimeFromDate(group1Date);
		LocalDateTime group2LocalDate1 = getLocalDateTimeFromDate(group2Date1);
		LocalDateTime group2LocalDate2 = getLocalDateTimeFromDate(group2Date2);

		if (isDateOnly(group1Date) && isTimeOnly(group2Date1) && isTimeOnly(group2Date2)) {
			LocalDate commonDate = group1LocalDate.toLocalDate();
			LocalTime startTime = group2LocalDate1.toLocalTime();
			LocalTime endTime = group2LocalDate2.toLocalTime();

			setTaskStartTime(LocalDateTime.of(commonDate, startTime));
			setTaskEndTime(LocalDateTime.of(commonDate, endTime));

			String returnedString;

			if (PublicVariables.startTimeWords.contains(preceedingWordGroup1)) {
				returnedString = PublicFunctions.reselectString(_argsString,
						preceedingWordGroup1 + whiteSpace + dateStringGroup1);
			} else {
				returnedString = PublicFunctions.reselectString(_argsString, dateStringGroup1);
			}

			if (isStartKeyWords(preceedingWordGroup2)) {
				returnedString = PublicFunctions.reselectString(returnedString,
						preceedingWordGroup2 + whiteSpace + dateStringGroup2);
			} else {
				returnedString = PublicFunctions.reselectString(returnedString, dateStringGroup2);
			}
			return returnedString;
		} else {
			setTaskStartTime(group2LocalDate1);
			setTaskEndTime(group2LocalDate2);

			if (PublicVariables.startTimeWords.contains(preceedingWordGroup2)) {
				return PublicFunctions.reselectString(_argsString, preceedingWordGroup2 + whiteSpace + dateStringGroup2);
			} else {
				return PublicFunctions.reselectString(_argsString, dateStringGroup2);
			}
		}
	}

	public static LocalDateTime getLocalDateTimeFromDate(Date date) {
		return PublicFunctions.getLocalDateTimeFromDate(date);
	}

	private static Date getMin(Date date1, Date date2) {
		if (date1.compareTo(date2) < 0) {
			return date1;
		} else {
			return date2;
		}
	}

	private static Date getMax(Date date1, Date date2) {
		if (date1.compareTo(date2) < 0) {
			return date2;
		} else {
			return date1;
		}
	}

	private static boolean isDateOnly(Date date) {
		Boolean returnedValue = false;
		for (int i = 0; i < dateWithoutTime.size(); i++) {
			if (date.compareTo(dateWithoutTime.get(i)) == 0) {
				returnedValue = true;
			}
		}
		return returnedValue;
	}

	private static boolean isTimeOnly(Date date) {
		LocalDateTime localDate = getLocalDateTimeFromDate(date);
		if (localDate.getDayOfYear() == LocalDateTime.now().getDayOfYear()) {
			return true;
		} else {
			return false;
		}
	}

	private static void setTaskStartTime(LocalDateTime startTime) {
		PublicVariables.taskStartTime = startTime;
	}

	private static void setTaskEndTime(LocalDateTime endTime) {
		PublicVariables.taskEndTime = endTime;
	}

	public static LocalDate tryParseDate(String argsString) {
		List<DateGroup> dateGroups = new PrettyTimeParser().parseSyntax(argsString);
		if (dateGroups.size() == 1) {
			String parsedString = dateGroups.get(0).getText();
			if (dateGroups.get(0).getDates().size() == 1 && parsedString.trim().equals(argsString)) {
				List<DateGroup> dateGroups2 = new PrettyTimeParser().parseSyntax(argsString);
				Date firstParseDate = dateGroups.get(0).getDates().get(0);
				Date secondParseDate = dateGroups2.get(0).getDates().get(0);
				if (!firstParseDate.equals(secondParseDate)) {
					LocalDateTime localDateTime = getLocalDateTimeFromDate(firstParseDate);
					return localDateTime.toLocalDate();
				}
			}
		}
		return null;
	}

	public static LocalDateTime tryParseTime(String argsString) {
		List<DateGroup> dateGroups = new PrettyTimeParser().parseSyntax(argsString);
		if (dateGroups.size() == 1) {
			String parsedString = dateGroups.get(0).getText();
			if (dateGroups.get(0).getDates().size() == 1 && parsedString.trim().equals(argsString)) {
				List<DateGroup> dateGroups2 = new PrettyTimeParser().parseSyntax(argsString);
				Date firstParseDate = dateGroups.get(0).getDates().get(0);
				Date secondParseDate = dateGroups2.get(0).getDates().get(0);
				if (firstParseDate.equals(secondParseDate)) {
					LocalDateTime localDateTime = getLocalDateTimeFromDate(firstParseDate);
					return localDateTime;
				}
			}
		}
		return null;
	}

	public static Month tryParseMonth(String argsString) {
		if (PublicVariables.janWords.contains(argsString)) {
			return Month.JANUARY;
		} else if (PublicVariables.febWords.contains(argsString)) {
			return Month.FEBRUARY;
		} else if (PublicVariables.marWords.contains(argsString)) {
			return Month.MARCH;
		} else if (PublicVariables.aprWords.contains(argsString)) {
			return Month.APRIL;
		} else if (PublicVariables.mayWords.contains(argsString)) {
			return Month.MAY;
		} else if (PublicVariables.junWords.contains(argsString)) {
			return Month.JUNE;
		} else if (PublicVariables.julWords.contains(argsString)) {
			return Month.JULY;
		} else if (PublicVariables.augWords.contains(argsString)) {
			return Month.AUGUST;
		} else if (PublicVariables.sepWords.contains(argsString)) {
			return Month.SEPTEMBER;
		} else if (PublicVariables.octWords.contains(argsString)) {
			return Month.OCTOBER;
		} else if (PublicVariables.novWords.contains(argsString)) {
			return Month.NOVEMBER;
		} else if (PublicVariables.decWords.contains(argsString)) {
			return Month.DECEMBER;
		}
		return null;
	}

	public static void searchTaskSlots(ArrayList<String> taskTimeStrings) {
		PublicVariables.taskSlots = new MultipleSlot();
		try {
			for (String taskTime : taskTimeStrings) {
				dateGroups = new PrettyTimeParser().parseSyntax(taskTime);
				if (dateGroups.size() == 1 && dateGroups.get(0).getDates().size() == 2) {
					LocalDateTime start = getLocalDateTimeFromDate(dateGroups.get(0).getDates().get(0));
					LocalDateTime end = getLocalDateTimeFromDate(dateGroups.get(0).getDates().get(1));
					PublicVariables.taskSlots.addTimeSlot(start, end);
				}
			}
		} catch (Exception e) {

		}

		if (PublicVariables.taskSlots.isEmpty()) {
			PublicVariables.taskSlots = null;
		}
	}

	public static LocalDateTime adjustedDateDeadline(LocalDateTime date) {
		date = date.withHour(23);
		date = date.withMinute(59);
		date = date.withSecond(0);
		date = date.withNano(0);
		return date;
	}

	public static LocalDateTime adjustedDateEvent(LocalDateTime date) {
		try {
			date = date.withHour(date.getHour() + 1);
		} catch (Exception e) {
			date = date.withDayOfMonth(date.getDayOfMonth() + 1);
			date = date.withHour((date.getHour() + 1) % 24);
		}

		date = date.withMinute(0);
		date = date.withSecond(0);
		date = date.withNano(0);
		return date;
	}
}
