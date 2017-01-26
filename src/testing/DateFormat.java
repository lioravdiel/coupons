package testing;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;

public class DateFormat {

	public static void main(String[] args) throws ParseException {
	
		Calendar calendar =Calendar.getInstance();
		Date date2 = new Date(calendar.getTimeInMillis());
		System.out.println(date2);
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		String str = dateFormat.format(new java.util.Date());
		System.out.println(str);
		java.util.Date dates = dateFormat.parse(str);
		System.out.println(dates);
		Date date = new Date(dates.getTime());
		System.out.println(date);
			
		
	}

}
