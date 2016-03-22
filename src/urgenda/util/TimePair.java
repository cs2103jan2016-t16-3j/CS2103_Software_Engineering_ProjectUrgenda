package urgenda.util;

import java.time.LocalDateTime;

public class TimePair {

	private LocalDateTime _start;
	private LocalDateTime _end;
	
	public TimePair(LocalDateTime start, LocalDateTime end) {
		_start = start;
		_end = end;
	}
	
	public LocalDateTime getStart() {
		return _start;
	}
	
	public LocalDateTime getEnd() {
		return _end;
	}
}
