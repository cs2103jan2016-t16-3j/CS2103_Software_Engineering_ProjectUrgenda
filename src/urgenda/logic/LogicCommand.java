package urgenda.logic;

import java.util.ArrayDeque;
import java.util.Deque;
//import java.util.logging.Level;
//import java.util.logging.Logger;

import urgenda.command.Command;
import urgenda.command.Redo;
import urgenda.command.Undo;
import urgenda.util.MyLogger;
import urgenda.command.TaskCommand;

public class LogicCommand {	
	
	private static MyLogger logger = MyLogger.getInstance();
	private static final String MESSAGE_EMPTY_UNDO = "Nothing to undo";
	private static final String MESSAGE_EMPTY_REDO = "Nothing to redo";
	
	private Deque<TaskCommand> _undos;
	private Deque<TaskCommand> _redos;

	// default constructor for logic command
	public LogicCommand() {
		_undos = new ArrayDeque<TaskCommand>();
		_redos = new ArrayDeque<TaskCommand>();
	}	

	public String processCommand(Command currCmd) {
		
		logger.getLogger().warning("Exception can occur");
		
		String feedback;
		try {
			if (currCmd instanceof Undo){ // TODO merge if doesnt compromise on readability
				feedback = currCmd.execute();
				feedback = undoCommand(feedback);
			} else if (currCmd instanceof Redo) {
				feedback = currCmd.execute();
				feedback = redoCommand(feedback);
			} else {
				feedback = currCmd.execute();
				addUndo(currCmd);
			}
		} catch (Exception e) { // TODO might need to upgrade exceptions without affecting the command execute header
			logger.getLogger().severe("Exception occured"+ e);
			return e.getMessage();
		}
		return feedback;
	}
	

	public void addUndo(Command currCmd) {
		if (currCmd instanceof TaskCommand) {
			logger.getLogger().info("adding " + currCmd + " to undo stack");
			
			_undos.addFirst((TaskCommand) currCmd);
			_redos.clear();
		}
	}
	
	public String undoCommand(String feedback) {
		if (!_undos.isEmpty()) {
			TaskCommand undoCommand = _undos.removeFirst();
			feedback = feedback + undoCommand.undo();
			_redos.addFirst(undoCommand);
			return feedback;
		} else { // replaces original feedback to undo empty message if there is nothing to undo
			return MESSAGE_EMPTY_UNDO;
		}

	}

	public String redoCommand(String feedback) {
		if (!_redos.isEmpty()) {
			TaskCommand redoCommand = _redos.removeFirst();
			feedback = feedback + redoCommand.redo();
			_undos.addFirst(redoCommand);
			return feedback;
		} else { // replaces original feedback to redo empty message if there is nothing to redo
			return MESSAGE_EMPTY_REDO;
		}

	}
}
