import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.Vector;

import org.ocpsoft.prettytime.PrettyTime;
import org.ocpsoft.prettytime.nlp.PrettyTimeParser;
import org.ocpsoft.prettytime.nlp.parse.DateGroup;

import urgenda.command.AddTask;
import urgenda.command.Command;
import urgenda.command.DeleteTask;
import urgenda.util.Task;
//import com.joestelmach.natty.*;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class test {
	private static final Scanner scanner = new Scanner(System.in);

	public static void main(String[] args) {
		while (true) {
			String input = getUserInput();
			
			PrettyTimeParser p = new PrettyTimeParser();
			List<Date> dateTimes = p.parse(input);
			for (Date test : dateTimes) {
				System.out.println(test + "\nEnd\n");
			}
			
			List<DateGroup> parse = new PrettyTimeParser().parseSyntax(input);
			for (int i = 0; i < parse.size(); i++) {
				for (int j = 0; j < parse.get(i).getDates().size(); j++) {
					String formatted = new PrettyTime().format(parse.get(i).getDates().get(j));
					System.out.print(parse.get(i).getDates().get(j) + "\n");
					System.out.print(formatted + "\n");
				}
				System.out.print(parse.get(i).getText() + "\n");
				System.out.print(parse.get(i).getPosition() + "\n");
			}
		}
	}

	// while (true) {
	// String test = getUserInput();
	// Parser parser = new Parser();
	// List<DateGroup> groups = parser.parse(test);
	// for (DateGroup group : groups) {
	// List<Date> dates = group.getDates();
	// for (int i = 0; i < dates.size(); i++) {
	// System.out.print(dates.get(i).toString() + "\n");
	// }
	// int line = group.getLine();
	// System.out.print(line + "\n");
	// int column = group.getPosition();
	// System.out.print(column + "\n");
	// String matchingValue = group.getText();
	// System.out.print(matchingValue + "\n");
	// Map<String, List<ParseLocation>> parseMap = group.getParseLocations();
	// for (int i = 0; i < parseMap.size(); i++) {
	// System.out.print(parseMap.get(i).toString() + "\n");
	// }
	// boolean isRecurreing = group.isRecurring();
	// System.out.print(isRecurreing + "\n");
	// Date recursUntil = group.getRecursUntil();
	// System.out.print(recursUntil + "\n");
	// }
	// }

	private static String getUserInput() {
		System.out.print("Type smth: ");
		String line = scanner.nextLine();
		return line.trim();
	}
}
