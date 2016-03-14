package urgenda.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TaskDetailsParser {
	public static String searchTaskLocation(String argsString) {
		String temp = argsString.trim();
		try {
			temp = temp.split("@")[1];
			PublicVariables.taskLocation = temp.trim();
			return argsString.replace("@" + temp.trim(), "");
		} catch (Exception e) {
			try {
				temp = temp.split(" at ")[1];
				PublicVariables.taskLocation = temp.trim();
				return argsString.replace(" at " + temp.trim(), "");
			}
			catch (Exception ex) {
				return argsString;
			}
		}
	}
	
	public static String searchTaskHashtags(String argsString) {
		if (argsString != null) {
			String temp = argsString;
			Matcher matcher = Pattern.compile("#\\S+").matcher(temp);
			while (matcher.find()) {
				PublicVariables.taskHashtags.add(matcher.group());
				argsString = argsString.replace(matcher.group(), "");
			}
		}
		return argsString;
	}
	
	public static void searchTaskDescription(String argsString) {
		
	}
	
	public static void searchTaskType() {
		
	}

}
