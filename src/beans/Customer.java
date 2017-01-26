package beans;

import java.util.Collection;

public class Customer {

	private int customerId;
	private String customerName;
	private String password;
	private Collection<Coupon> coupons;
	
	public Customer() {
		// TODO Auto-generated constructor stub
	}
	
	public Customer(String customerName, String password) {
		super();
		this.customerName = customerName;
		this.password = password;
	}



	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Collection<Coupon> getCoupons() {
		return coupons;
	}

	public void setCoupons(Collection<Coupon> coupons) {
		this.coupons = coupons;
	}

	@Override
	public String toString() {
		return "Customer [customerId=" + customerId + ", customerName=" + customerName + ", password=" + password + "]";
	}

	
}
