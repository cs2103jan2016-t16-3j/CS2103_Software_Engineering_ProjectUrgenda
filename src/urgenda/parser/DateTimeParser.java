package urgenda.parser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.ocpsoft.prettytime.nlp.PrettyTimeParser;
import org.ocpsoft.prettytime.nlp.parse.DateGroup;

public class DateTimeParser {
	private static ArrayList<Date> dateWithoutTime;
	private static List<DateGroup> dateGroups;
	private static String _argsString;

	public static String searchTaskTimes(String argsString) {
		_argsString = argsString;
		dateWithoutTime = searchDateWithoutTime();
		dateGroups = new PrettyTimeParser().parseSyntax(_argsString);
		int numberOfDateGroup = dateGroups.size();

		switch (numberOfDateGroup) {
		case 0:
			return argsString;
		case 1:
			return handlel1DateGroup();
		case 2:
			return handle2DateGroups();
		case 3:
			return handle3DateGroups();
		case 4:
			return handle4DateGroups();
		default:
			dateGroups = reselectDateGroups();
			return handle4DateGroups();
		}
	}

	private static ArrayList<Date> searchDateWithoutTime() {
		PrettyTimeParser prettyParser = new PrettyTimeParser();
		List<Date> firstParse = prettyParser.parse(_argsString);
		List<Date> secondParse = prettyParser.parse(_argsString);
		ArrayList<Date> dateWithoutTime = new ArrayList<Date>();
		for (int i = 0; i < firstParse.size(); i++) {
			if (firstParse.get(i) != secondParse.get(i)) {
				dateWithoutTime.add(firstParse.get(i));
			}
		}
		return dateWithoutTime;
	}

	private static List<DateGroup> reselectDateGroups() {
		int startIndex = dateGroups.size() - 4;
		int endIndex = dateGroups.size() - 1;
		return dateGroups.subList(startIndex, endIndex);
	}

	// need refractoring after done
	private static String handlel1DateGroup() {
		int numberOfDates = dateGroups.get(0).getDates().size();
		int parsePosition = dateGroups.get(0).getPosition();
		String dateString = dateGroups.get(0).getText();
		String preceedingWord = PublicFunctions.getPreceedingWord(parsePosition, _argsString);
		Date timeInDate;
		switch (numberOfDates) {
		case 1:
			if (PublicVariables.startTimeWords.contains(preceedingWord)) {
				timeInDate = dateGroups.get(0).getDates().get(0);
				setTaskStartTime(getLocalDateTimeFromDate(timeInDate));
				setTaskEndTime(PublicVariables.taskStartTime.plusHours(1));

				return PublicFunctions.reselectString(_argsString, preceedingWord + " " + dateString);
			} else if (PublicVariables.endTimeWords.contains(preceedingWord)) {
				timeInDate = dateGroups.get(0).getDates().get(0);
				setTaskEndTime(getLocalDateTimeFromDate(timeInDate));

				return PublicFunctions.reselectString(_argsString, preceedingWord + " " + dateString);
			} else {
				timeInDate = dateGroups.get(0).getDates().get(0);
				setTaskStartTime(getLocalDateTimeFromDate(timeInDate));
				setTaskEndTime(PublicVariables.taskStartTime.plusHours(1));

				return PublicFunctions.reselectString(_argsString, dateString);
			}
		case 2:
			timeInDate = dateGroups.get(0).getDates().get(0);
			setTaskStartTime(getLocalDateTimeFromDate(timeInDate));

			timeInDate = dateGroups.get(0).getDates().get(1);
			setTaskEndTime(getLocalDateTimeFromDate(timeInDate));

			if (PublicVariables.startTimeWords.contains(preceedingWord)
					|| PublicVariables.periodWords.contains(preceedingWord)) {
				return PublicFunctions.reselectString(_argsString, preceedingWord + " " + dateString);
			} else {
				return PublicFunctions.reselectString(_argsString, dateString);
			}
		case 3:
			Date time1 = dateGroups.get(0).getDates().get(0);
			Date time2 = dateGroups.get(0).getDates().get(1);
			Date time3 = dateGroups.get(0).getDates().get(2);

			if (time1.equals(time2) || (time1.equals(time3))) {
				setTaskStartTime(getLocalDateTimeFromDate(getMin(time1, time3)));
				setTaskEndTime(getLocalDateTimeFromDate(getMax(time1, time3)));
			} else if (time2.equals(time3)) {
				setTaskStartTime(getLocalDateTimeFromDate(getMin(time1, time2)));
				setTaskEndTime(getLocalDateTimeFromDate(getMax(time1, time2)));
			} else {
				setTaskStartTime(getLocalDateTimeFromDate(getMin(time2, time3)));
				setTaskEndTime(getLocalDateTimeFromDate(getMax(time2, time3)));
			}

			if (PublicVariables.startTimeWords.contains(preceedingWord)
					|| PublicVariables.periodWords.contains(preceedingWord)) {
				return PublicFunctions.reselectString(_argsString, preceedingWord + " " + dateString);
			} else {
				return PublicFunctions.reselectString(_argsString, dateString);
			}
		default:
			// can add handler
			return _argsString;
		}
	}

	// need refractoring after done
	private static String handle2DateGroups() {
		int numberOfDatesInGroup1 = dateGroups.get(0).getDates().size();
		int parsePositionGroup1 = dateGroups.get(0).getPosition();
		String dateStringGroup1 = dateGroups.get(0).getText();
		String preceedingWordGroup1 = PublicFunctions.getPreceedingWord(parsePositionGroup1, _argsString);

		int numberOfDatesInGroup2 = dateGroups.get(0).getDates().size();
		int parsePositionGroup2 = dateGroups.get(0).getPosition();
		String dateStringGroup2 = dateGroups.get(0).getText();
		String preceedingWordGroup2 = PublicFunctions.getPreceedingWord(parsePositionGroup2, _argsString);

		if (numberOfDatesInGroup1 == 1 && numberOfDatesInGroup2 == 2) {
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
							preceedingWordGroup1 + " " + dateStringGroup1);
				} else {
					returnedString = PublicFunctions.reselectString(_argsString, dateStringGroup1);
				}
				if (PublicVariables.startTimeWords.contains(preceedingWordGroup2)) {
					returnedString = PublicFunctions.reselectString(_argsString,
							preceedingWordGroup2 + " " + dateStringGroup2);
				} else {
					returnedString = PublicFunctions.reselectString(_argsString, dateStringGroup2);
				}

				return returnedString;
			} else {
				setTaskStartTime(group2LocalDate1);
				setTaskEndTime(group2LocalDate2);

				if (PublicVariables.startTimeWords.contains(preceedingWordGroup2)) {
					return PublicFunctions.reselectString(_argsString, preceedingWordGroup2 + " " + dateStringGroup2);
				} else {
					return PublicFunctions.reselectString(_argsString, dateStringGroup2);
				}
			}
		} else if (numberOfDatesInGroup1 == 2 && numberOfDatesInGroup2 == 1) {
			// basically reverse of case above, refractor later
			Date group2Date = dateGroups.get(0).getDates().get(0);
			Date group1Date1 = dateGroups.get(1).getDates().get(0);
			Date group1Date2 = dateGroups.get(1).getDates().get(1);
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
							preceedingWordGroup2 + " " + dateStringGroup2);
				} else {
					returnedString = PublicFunctions.reselectString(_argsString, dateStringGroup1);
				}
				if (PublicVariables.startTimeWords.contains(preceedingWordGroup1)) {
					returnedString = PublicFunctions.reselectString(_argsString,
							preceedingWordGroup1 + " " + dateStringGroup1);
				} else {
					returnedString = PublicFunctions.reselectString(_argsString, dateStringGroup1);
				}

				return returnedString;
			} else {
				setTaskStartTime(group1LocalDate1);
				setTaskEndTime(group1LocalDate2);

				if (PublicVariables.startTimeWords.contains(preceedingWordGroup1)) {
					return PublicFunctions.reselectString(_argsString, preceedingWordGroup1 + " " + dateStringGroup1);
				} else {
					return PublicFunctions.reselectString(_argsString, dateStringGroup1);
				}
			}
		} else if (numberOfDatesInGroup1 == 1 && numberOfDatesInGroup2 == 1) {
			Date group1Date = dateGroups.get(0).getDates().get(0);
			Date group2Date = dateGroups.get(1).getDates().get(0);
			LocalDateTime group1LocalDate = getLocalDateTimeFromDate(group1Date);
			LocalDateTime group2LocalDate = getLocalDateTimeFromDate(group2Date);

			if ((isDateOnly(group1Date) && isTimeOnly(group2Date))
					|| (isDateOnly(group2Date) && isTimeOnly(group1Date))) {
				LocalDate dateComponent;
				LocalTime timeComponent;
				LocalDateTime taskDateTime;

				if (isDateOnly(group1Date) && isTimeOnly(group2Date)) {
					dateComponent = group1LocalDate.toLocalDate();
					timeComponent = group2LocalDate.toLocalTime();
					taskDateTime = LocalDateTime.of(dateComponent, timeComponent);
				} else {
					dateComponent = group2LocalDate.toLocalDate();
					timeComponent = group1LocalDate.toLocalTime();
					taskDateTime = LocalDateTime.of(dateComponent, timeComponent);
				}
				
				if (PublicVariables.endTimeWords.contains(preceedingWordGroup1)) {
					setTaskEndTime(taskDateTime);

					if (PublicVariables.startTimeWords.contains(preceedingWordGroup2)) {
						String reducedArgsString = PublicFunctions.reselectString(_argsString,
								preceedingWordGroup2 + " " + dateStringGroup2);
						reducedArgsString = PublicFunctions.reselectString(reducedArgsString,
								preceedingWordGroup1 + " " + dateStringGroup1);

						return reducedArgsString;
					} else {
						String reducedArgsString = PublicFunctions.reselectString(_argsString, dateStringGroup2);
						reducedArgsString = PublicFunctions.reselectString(reducedArgsString,
								preceedingWordGroup1 + " " + dateStringGroup1);

						return reducedArgsString;
					}
				} else if (PublicVariables.endTimeWords.contains(preceedingWordGroup2)) {
					setTaskEndTime(taskDateTime);

					if (PublicVariables.startTimeWords.contains(preceedingWordGroup1)) {
						String reducedArgsString = PublicFunctions.reselectString(_argsString,
								preceedingWordGroup1 + " " + dateStringGroup1);
						reducedArgsString = PublicFunctions.reselectString(reducedArgsString,
								preceedingWordGroup2 + " " + dateStringGroup2);

						return reducedArgsString;
					} else {
						String reducedArgsString = PublicFunctions.reselectString(_argsString, dateStringGroup1);
						reducedArgsString = PublicFunctions.reselectString(reducedArgsString,
								preceedingWordGroup2 + " " + dateStringGroup2);

						return reducedArgsString;
					}
				} else {
					setTaskStartTime(taskDateTime);

					String reducedArgsString;

					if (PublicVariables.startTimeWords.contains(preceedingWordGroup1)) {
						reducedArgsString = PublicFunctions.reselectString(_argsString,
								preceedingWordGroup1 + " " + dateStringGroup1);
					} else {
						reducedArgsString = PublicFunctions.reselectString(_argsString, dateStringGroup2);
					}

					if (PublicVariables.startTimeWords.contains(preceedingWordGroup2)) {
						reducedArgsString = PublicFunctions.reselectString(_argsString,
								preceedingWordGroup2 + " " + dateStringGroup2);
					} else {
						reducedArgsString = PublicFunctions.reselectString(_argsString, dateStringGroup2);
					}

					return reducedArgsString;
				}
			} else if (isDateOnly(group2Date) && isDateOnly(group1Date)) {
				return _argsString; // can add handler
			} else {
				setTaskStartTime(group1LocalDate);
				setTaskEndTime(group2LocalDate);
				
				String reducedArgsString;
				
				if (PublicVariables.startTimeWords.contains(preceedingWordGroup1)) {
					reducedArgsString = PublicFunctions.reselectString(_argsString,
							preceedingWordGroup1 + " " + dateStringGroup1);
				} else {
					reducedArgsString = PublicFunctions.reselectString(_argsString, dateStringGroup2);
				}
				
				if (PublicVariables.endTimeWords.contains(preceedingWordGroup2)) {
					reducedArgsString = PublicFunctions.reselectString(_argsString,
							preceedingWordGroup2 + " " + dateStringGroup2);
				} else {
					reducedArgsString = PublicFunctions.reselectString(_argsString, dateStringGroup2);
				}

				return reducedArgsString;
			}
		} else if (numberOfDatesInGroup1 == 2 && numberOfDatesInGroup2 == 2) {
			return _argsString; // can add handler
		} else {
			return _argsString;
		} // can add handler
	}

	private static String handle3DateGroups() {
		return _argsString;
	}

	private static String handle4DateGroups() {
		return _argsString;
	}

	private static LocalDateTime getLocalDateTimeFromDate(Date date) {
		return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
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
}
