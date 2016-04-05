//@@author A0080436
package urgenda.logic;

import java.util.ArrayDeque;
import java.util.Deque;

import urgenda.command.Command;
import urgenda.command.Redo;
import urgenda.command.TaskCommand;
import urgenda.command.Undo;
import urgenda.util.LogicException;
import urgenda.util.UrgendaLogger;

/**
 * Logic Command class of the Logic component in Urgenda.
 * Responsible of accessing and storage of the different commands used by Urgenda.
 * 
 */
public class LogicCommand {

	private static UrgendaLogger logger = UrgendaLogger.getInstance();
	private static final String MESSAGE_EMPTY_UNDO = "Nothing to undo";
	private static final String MESSAGE_EMPTY_REDO = "Nothing to redo";

	private Deque<TaskCommand> _undos;
	private Deque<TaskCommand> _redos;

	/**
	 * Default constructor for LogicCommand.
	 */
	public LogicCommand() {
		_undos = new ArrayDeque<TaskCommand>();
		_redos = new ArrayDeque<TaskCommand>();
	}

	/**
	 * Processes the given command to execute changes on the tasks involved.
	 * 
	 * @param currCmd
	 *            Current command that requires to be carried out.
	 * @return Feedback message for user display on the processing of command.
	 */
	public String processCommand(Command currCmd) {
		logger.getLogger().warning("Exception can occur");
		String feedback;
		try {
			feedback = currCmd.execute();
			if (currCmd instanceof Undo) {
				feedback = undoCommand(feedback);
			} else if (currCmd instanceof Redo) {
				feedback = redoCommand(feedback);
			} else {
				addUndo(currCmd);
			}
		} catch (LogicException e) {
			logger.getLogger().severe("Exception occured" + e);
			return e.getMessage();
		}
		return feedback;
	}

	/**
	 * Adds the currCmd given to the undo stack if the command is a task command
	 * type which can be undone.
	 * 
	 * @param currCmd
	 *            Current command being executed by the program.
	 */
	public void addUndo(Command currCmd) {
		if (currCmd instanceof TaskCommand) {
			logger.getLogger().info("adding " + currCmd + " to undo stack");
			_undos.addFirst((TaskCommand) currCmd);
			_redos.clear();
		}
	}

	/**
	 * The undo command calls the undo method from the latest task command in
	 * the undo stack.
	 * 
	 * @param feedback
	 *            Current feedback string to the user.
	 * @return Updated feedback string with the specific task undone.
	 */
	private String undoCommand(String feedback) {
		if (!_undos.isEmpty()) {
			TaskCommand undoCommand = _undos.removeFirst();
			feedback += undoCommand.undo();
			_redos.addFirst(undoCommand);
			return feedback;
		} else {
			// replaces original feedback to undo empty message if there is
			// nothing to undo
			return MESSAGE_EMPTY_UNDO;
		}
	}

	/**
	 * The redo command calls the redo method from the latest task command in
	 * the redo stack.
	 * 
	 * @param feedback
	 *            Current feedback string to the user.
	 * @return Updated feedback string with the specific task done again.
	 */
	private String redoCommand(String feedback) {
		if (!_redos.isEmpty()) {
			TaskCommand redoCommand = _redos.removeFirst();
			feedback += redoCommand.redo();
			_undos.addFirst(redoCommand);
			return feedback;
		} else {
			// replaces original feedback to redo empty message if there is
			// nothing to redo
			return MESSAGE_EMPTY_REDO;
		}
	}
}
