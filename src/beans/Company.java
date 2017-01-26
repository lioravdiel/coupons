package beans;

import java.util.Collection;

public class Company {

	private int companyId;
	private String companyName;
	private String password;
	private String email;
	private Collection<Coupon> coupons;
	
	public Company() {
		// TODO Auto-generated constructor stub
	}
	
	public Company(String companyName, String password, String email) {
		this.companyName = companyName;
		this.password = password;
		this.email = email;
	}

	public int getCompanyId() {
		return companyId;
	}

	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Collection<Coupon> getCoupons() {
		return coupons;
	}

	public void setCoupons(Collection<Coupon> coupons) {
		this.coupons = coupons;
	}

	@Override
	public String toString() {
		return "Company [companyId=" + companyId + ", companyName=" + companyName + ", password=" + password
				+ ", email=" + email + ", coupons=" + coupons + "]";
	}
	
	
	
}
