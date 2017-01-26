package dao;

import java.util.Collection;

import beans.Company;
import beans.Coupon;

public interface CompanyDAO {

	public void createCompany(Company company);
	
	public void removeCompany(Company company);
	
	public void updtaeCompany(Company company);
	
	public void updateCompanyCoupon(String operation, Coupon coupon);
	
	public Company getCompany(String companyName);
	
	public Collection<Company> getAllCompanies();
	
	public Collection<Coupon> getCoupons();
	
	public boolean login(String companyName,String password);
	
}
