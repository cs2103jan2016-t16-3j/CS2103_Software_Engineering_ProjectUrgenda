package urgenda.util;

public class LogicException extends Exception {

	private String _message;

	public LogicException(String message) {
		_message = message;
	}

	@Override
	public String getMessage() {
		return _message;
	}

}
