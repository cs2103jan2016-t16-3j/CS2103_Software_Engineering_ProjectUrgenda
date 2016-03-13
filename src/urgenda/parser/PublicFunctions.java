package urgenda.parser;

public class PublicFunctions {
	public static String getFirstWord(String commandString) {
		return commandString.split("\\s+")[0];
	}
	
	public static String removeFirstWord(String commandString) {
		try {
			return commandString.split("\\s+", 2)[1];
		} catch (Exception e) {
			return null;
		}
	}
}
