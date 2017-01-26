package testing;


import java.sql.Date;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;

import beans.Coupon;
import beans.CouponType;
import facade.CompanyFacade;
import system.CouponSystem;

public class Test {

	
	public static void main(String[] args) throws ParseException {
		
		Date date = new Date(new java.util.Date().getTime());
		
		
		CouponSystem couponSystem = CouponSystem.getInstance();
		
		CompanyFacade companyFacade = (CompanyFacade) couponSystem.login("tootkale", "1111", "Company");
		
		Coupon coupon = new Coupon("One Coupon", date, date, 10 , CouponType.CAMPING, "great coupon", 150, "fff");
		
		companyFacade.createCoupon(coupon);
		sleeping();
		companyFacade.removeCoupon(coupon);
	}
	
	private static void sleeping() {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
