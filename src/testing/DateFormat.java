package testing;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateFormat {

	public static void main(String[] args) {
	
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		
		java.util.Date d = new java.util.Date();
		String ds = dateFormat.format(d);
		
		Date date = null;
		try {
			date = new Date(dateFormat.parse(ds).getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(date);
		
	}

}
