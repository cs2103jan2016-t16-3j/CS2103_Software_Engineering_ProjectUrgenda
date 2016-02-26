package urgenda.command;

/**
 * Undoable interface for commands that are able to be undone/redone
 *
 */
public interface Undoable extends Command {
	
	String undo();
	
	String redo();

}
