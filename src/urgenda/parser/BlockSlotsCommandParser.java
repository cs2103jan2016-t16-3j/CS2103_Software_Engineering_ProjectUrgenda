package urgenda.parser;

import urgenda.command.*;


public class BlockSlotsCommandParser {
	private String _argsString;
	private int _index;
	
	public BlockSlotsCommandParser(String argsString, int index) {
		_argsString = argsString;
		_index = index;
	}
	
	public static Command generateAndReturn() {
		return new Invalid();
	}
}
