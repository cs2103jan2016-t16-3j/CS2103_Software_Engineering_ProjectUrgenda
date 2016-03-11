package urgenda.logic;

import java.util.ArrayDeque;
import java.util.Deque;

import urgenda.command.Command;
import urgenda.command.Redo;
import urgenda.command.Undo;
import urgenda.command.Undoable;

public class LogicCommand {	

	private static final String MESSAGE_EMPTY_UNDO = "Nothing to undo";
	private static final String MESSAGE_EMPTY_REDO = "Nothing to redo";
	
	private LogicData _logicData;

	private Deque<Undoable> _undos;
	private Deque<Undoable> _redos;

	public LogicCommand(LogicData logicData) {
		_logicData = logicData;
		_undos = new ArrayDeque<Undoable>();
		_redos = new ArrayDeque<Undoable>();
	}	

	public String processCommand(Command currCmd) {
		String feedback;
		try {
			if (currCmd instanceof Undo){ // TODO merge if doesnt compromise on readability
				feedback = currCmd.execute(_logicData);
				feedback = undoCommand(feedback);
			} else if (currCmd instanceof Redo) {
				feedback = currCmd.execute(_logicData);
				feedback = redoCommand(feedback);
			} else {
				feedback = currCmd.execute(_logicData);
				addUndo(currCmd);
			}
		} catch (Exception e) { // TODO might need to upgrade exceptions without affecting the command execute header
			return e.getMessage();
		}
		return feedback;
	}
	
	public void addUndo(Command currCmd) {
		if (currCmd instanceof Undoable) {
			_undos.addFirst((Undoable) currCmd);
			_redos.clear();
		}
	}
	
	public String undoCommand(String feedback) {
		if (!_undos.isEmpty()) {
			Undoable undoCommand = _undos.removeFirst();
			feedback = feedback + undoCommand.undo();
			_redos.addFirst(undoCommand);
			return feedback;
		} else { // replaces original feedback to undo empty message if there is nothing to undo
			return MESSAGE_EMPTY_UNDO;
		}

	}

	public String redoCommand(String feedback) {
		if (!_redos.isEmpty()) {
			Undoable redoCommand = _redos.removeFirst();
			feedback = feedback + redoCommand.redo();
			_undos.addFirst(redoCommand);
			return feedback;
		} else { // replaces original feedback to redo empty message if there is nothing to redo
			return MESSAGE_EMPTY_REDO;
		}

	}
}
