package dao;

import java.util.Collection;

import beans.Coupon;
import beans.Customer;

public interface CustomerDAO {

	public void createCustomer(Customer customer);
	
	public void removeCustomer(Customer customer);
	
	public void updateCustomer(Customer customer);
	
	public void updateCustomerCoupon(String operation, Coupon coupon);
	
	public Customer getCustomer(String customerName);
	
	public Collection<Customer> getAllCustomers();
	
	public Collection<Coupon> getCoupons();
	
	public boolean login(String customerName, String password);
	
}
