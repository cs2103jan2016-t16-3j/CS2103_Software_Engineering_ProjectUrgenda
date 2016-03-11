package urgenda.command;

/**
 * Undoable class for commands that are able to be undone/redone
 *
 */
public abstract class Undoable extends Command{

	public abstract String undo();
	public abstract String redo();
}
