package urgenda.command;

/**
 * Command interface for implementation of subsequent command classes
 *
 */
public interface CommandInterface {
	
	// for execution of specific command
	String execute();
	
	// for filling in additional details required for the command
	void getDetails(String[] details);
	
	String undo();
	
	String redo();
}
