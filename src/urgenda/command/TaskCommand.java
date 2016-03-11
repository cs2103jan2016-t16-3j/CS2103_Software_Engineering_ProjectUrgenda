package urgenda.command;

/**
 * TaskCommand class for commands dealing with tasks and are able to be undone/redone
 *
 */
public abstract class TaskCommand extends Command{

	public abstract String undo();
	public abstract String redo();
}
