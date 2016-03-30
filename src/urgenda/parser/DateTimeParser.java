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

	public static String searchTaskTimes(String argsString) {
		_argsString = argsString;
		dateGroups = new PrettyTimeParser().parseSyntax(_argsString);
		dateWithoutTime = searchDateWithoutTime();
		int numberOfDateGroup = dateGroups.size();

		// System.out.print(numberOfDateGroup + "\n");
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
			timeInDate = dateGroups.get(0).getDates().get(0);
			LocalDateTime dateTime = getLocalDateTimeFromDate(timeInDate);
			
			if (PublicVariables.startTimeWords.contains(preceedingWord)) {
				if (isDateOnly(timeInDate)) {
					dateTime = adjustedDateEvent(dateTime);
				}
				setTaskStartTime(dateTime);
				setTaskEndTime(dateTime.plusHours(1));
				return PublicFunctions.reselectString(_argsString, preceedingWord + " " + dateString);
			} else if (PublicVariables.endTimeWords.contains(preceedingWord)) {
				if (isDateOnly(timeInDate)) {
					dateTime = adjustedDateDeadline(dateTime);
				}
				setTaskEndTime(dateTime);
				return PublicFunctions.reselectString(_argsString, preceedingWord + " " + dateString);
			} else {
				if (isDateOnly(timeInDate)) {
					dateTime = adjustedDateEvent(dateTime);
				}
				setTaskStartTime(dateTime);
				setTaskEndTime(dateTime.plusHours(1));
				return PublicFunctions.reselectString(_argsString, dateString);
			}
		case 2:
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
				Date minDate = getMin(time1, time3);
				Date maxDate = getMax(time1, time3);
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
			} else if (time2.equals(time3)) {
				Date minDate = getMin(time1, time2);
				Date maxDate = getMax(time1, time2);
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
			} else {
				Date minDate = getMin(time3, time2);
				Date maxDate = getMax(time3, time2);
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
//		 System.out.print(numberOfDatesInGroup1 + "\n");

		int numberOfDatesInGroup2 = dateGroups.get(1).getDates().size();
		int parsePositionGroup2 = dateGroups.get(1).getPosition();
		String dateStringGroup2 = dateGroups.get(1).getText();
		String preceedingWordGroup2 = PublicFunctions.getPreceedingWord(parsePositionGroup2, _argsString);
//		 System.out.print(numberOfDatesInGroup2 + "\n");

		if (numberOfDatesInGroup1 == 1 && numberOfDatesInGroup2 == 2) {
			// System.out.print("here" + "\n");
			Date group1Date = dateGroups.get(0).getDates().get(0);
			Date group2Date1 = dateGroups.get(1).getDates().get(0);
			Date group2Date2 = dateGroups.get(1).getDates().get(1);
			LocalDateTime group1LocalDate = getLocalDateTimeFromDate(group1Date);
			LocalDateTime group2LocalDate1 = getLocalDateTimeFromDate(group2Date1);
			LocalDateTime group2LocalDate2 = getLocalDateTimeFromDate(group2Date2);
			System.out.print(group1Date + "\n");
			System.out.print(group2Date1 + "\n");
			System.out.print(group2Date1 + "\n");

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
					// System.out.print(dateStringGroup1 + ". " + returnedString
					// + "\n");
				}

				if (PublicVariables.startTimeWords.contains(preceedingWordGroup2)
						|| PublicVariables.periodWords.contains(preceedingWordGroup2)) {
					returnedString = PublicFunctions.reselectString(returnedString,
							preceedingWordGroup2 + " " + dateStringGroup2);
				} else {
					returnedString = PublicFunctions.reselectString(returnedString, dateStringGroup2);
				}
				// System.out.print(dateStringGroup2 + ". " + returnedString +
				// "\n");
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
							preceedingWordGroup2 + " " + dateStringGroup2);
				} else {
					returnedString = PublicFunctions.reselectString(_argsString, dateStringGroup2);
				}

				if (PublicVariables.startTimeWords.contains(preceedingWordGroup1)
						|| PublicVariables.periodWords.contains(preceedingWordGroup1)) {
					returnedString = PublicFunctions.reselectString(returnedString,
							preceedingWordGroup1 + " " + dateStringGroup1);
				} else {
					returnedString = PublicFunctions.reselectString(returnedString, dateStringGroup1);
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
					setTaskEndTime(taskDateTime.plusHours(1));
					
					String reducedArgsString;

					if (PublicVariables.startTimeWords.contains(preceedingWordGroup1)) {
						reducedArgsString = PublicFunctions.reselectString(_argsString,
								preceedingWordGroup1 + " " + dateStringGroup1);
					} else {
						reducedArgsString = PublicFunctions.reselectString(_argsString, dateStringGroup1);
					}

					if (PublicVariables.startTimeWords.contains(preceedingWordGroup2)) {
						reducedArgsString = PublicFunctions.reselectString(reducedArgsString,
								preceedingWordGroup2 + " " + dateStringGroup2);
					} else {
						reducedArgsString = PublicFunctions.reselectString(reducedArgsString, dateStringGroup2);
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
					reducedArgsString = PublicFunctions.reselectString(reducedArgsString,
							preceedingWordGroup2 + " " + dateStringGroup2);
				} else {
					reducedArgsString = PublicFunctions.reselectString(reducedArgsString, dateStringGroup2);
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
		return _argsString; // can add handler
	}

	private static String handle4DateGroups() {
		return _argsString; // can add handler
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
		return date;
	}
	
	public static LocalDateTime adjustedDateEvent(LocalDateTime date) {
		date = date.withHour(date.getHour()+1);
		date = date.withMinute(0);
		date = date.withSecond(0);
		return date;
	}
	
	private static ArrayList<LocalDateTime> checkAndRoundUpEventDatePair(Date date1, Date date2) {
		return null;
	}
}
