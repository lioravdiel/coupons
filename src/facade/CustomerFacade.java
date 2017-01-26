package facade;

import java.util.Collection;

import beans.Coupon;
import beans.CouponType;
import beans.Customer;
import dao.CompanyDAO;
import dao.CouponDAO;
import dao.CustomerDAO;

public class CustomerFacade implements CouponClientFacade{

	CustomerDAO customerDAO;
	CompanyDAO companyDAO;
	CouponDAO couponDAO;
	private Customer customer;
	
	public CustomerFacade(CustomerDAO customerDAO, CouponDAO couponDAO, CompanyDAO companyDAO, Customer customer) {
		this.customerDAO = customerDAO;
		this.couponDAO = couponDAO;
		this.companyDAO = companyDAO;
		this.customer = customer;
	}

	
	public void purchaseCoupon(Coupon coupon) {
		couponDAO.updateCoupon(coupon);
		customerDAO.updateCustomerCoupon("create", coupon);
	}
	
	public Collection<Coupon> getAllPurchasedCouons() {
		return customerDAO.getCoupons();
	}
	
	public Collection<Coupon> getAllPurchasedCouponsByType(CouponType type){
		return couponDAO.getCouponByType(type);
	}
	
	public Collection<Coupon> getAllPurchasedCouponsByPrice(double price){
		return couponDAO.getCouponByPrice(price);
	}
	
	@Override
	public CouponClientFacade login(String name, String password, String clientType) {
		
		boolean checkCustomerLogin = customerDAO.login(name, password);
		CouponClientFacade clientFacade = null;
		
		if (clientType == "customer" && (checkCustomerLogin)){
			return clientFacade;
		}
		
		return clientFacade;
	}
	
}
