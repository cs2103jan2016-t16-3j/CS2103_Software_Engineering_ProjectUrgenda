package urgenda.util;

import java.time.LocalDateTime;

public class DateTimePair {
	private LocalDateTime _start, _end;

	public DateTimePair() {
	}

	public DateTimePair(LocalDateTime start, LocalDateTime end) {
		_start = start;
		_end = end;
	}

	public void setStart(LocalDateTime input) {
		_start = input;
	}

	public void setEnd(LocalDateTime input) {
		_end = input;
	}

	public LocalDateTime getStart() {
		return _start;
	}

	public LocalDateTime getEnd() {
		return _end;
	}
}
