package dao;

import java.util.Collection;

import beans.Company;
import beans.Coupon;
import beans.CouponType;

public interface CouponDAO {

	
	public void createCoupon(Coupon coupon);
	
	public void removeCoupon(Coupon coupon);
	
	public void updateCoupon(Coupon coupon);
		
	public Coupon getCoupon(int couponId);
	
	public Collection<Coupon> getAllCoupons();
	
	public Collection<Coupon> getCouponByType(CouponType type);
	
	public Collection<Coupon> getCouponByPrice(double price);

	
}
