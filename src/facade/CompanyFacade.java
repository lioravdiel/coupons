package facade;

import java.util.Collection;

import beans.Company;
import beans.Coupon;
import beans.CouponType;
import dao.CompanyDAO;
import dao.CouponDAO;

public class CompanyFacade implements CouponClientFacade{

	CouponDAO couponDAO;
	CompanyDAO companyDAO;
	Company company;
	
	public CompanyFacade(CouponDAO couponDAO, CompanyDAO companyDAO, Company company) {
		this.couponDAO = couponDAO;
		this.companyDAO = companyDAO;
		this.company = company;
	}
	
	public void createCoupon(Coupon coupon) {
		couponDAO.createCoupon(coupon);
		//companyDAO.updateCompanyCoupon("create", coupon);
	}
	
	public void removeCoupon(Coupon coupon){
		couponDAO.removeCoupon(coupon);
		companyDAO.updateCompanyCoupon("remove", coupon);
	}
	
	public void updateCoupon(Coupon coupon){
		couponDAO.updateCoupon(coupon);
	}
	
	public Coupon getCoupon(int couponId) {
		return couponDAO.getCoupon(couponId);
	}
	
	public Collection<Coupon> getAllCoupons(){
		return couponDAO.getAllCoupons();
	}
	
	public Collection<Coupon> getCouponByType(CouponType type){
		return couponDAO.getCouponByType(type);
	}
	
	@Override
	public CouponClientFacade login(String name, String password, String clientType) {
		
		boolean checkCompanyLogin = companyDAO.login(name, password); 
		CouponClientFacade clientFacade = null;
		
		if(clientType == "company" && (checkCompanyLogin)){
			return clientFacade;
		}
		
		return clientFacade;
	}

}
