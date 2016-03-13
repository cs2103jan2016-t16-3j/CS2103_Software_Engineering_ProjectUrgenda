package testParser;


import java.util.Date;
import java.util.List;

import org.junit.Test;

import org.ocpsoft.prettytime.nlp.PrettyTimeParser;

public class PtParser {

	@Test
	public void test() {
		PrettyTimeParser p = new PrettyTimeParser();

		String date = "23/03/2016";
		List<Date> dateTimes = p.parse(date);
		for (Date test : dateTimes) {
			System.out.println(test);
		}
	}

}
