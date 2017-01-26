package facade;

import java.util.Collection;

import beans.Company;
import beans.Coupon;
import beans.Customer;
import dao.CompanyDAO;
import dao.CustomerDAO;


public class AdminFacade implements CouponClientFacade{

	String adminUserName = "admin";
	String password = "1234";
		
	CompanyDAO companyDAO;
	CustomerDAO customerDAO;
	Company company;
	Customer customer;
	Coupon coupon;
	
	public AdminFacade(CompanyDAO companyDAO, CustomerDAO customerDAO) {
		this.companyDAO = companyDAO;
		this.customerDAO = customerDAO;
		company = new Company();
		customer = new Customer();
		coupon = new Coupon();
	}

	
	public void createCompany(Company company){
		companyDAO.createCompany(company);
	}
	
	public void removeCompany(Company company){
		companyDAO.removeCompany(company);
		companyDAO.updateCompanyCoupon("removeByCompany", coupon);
	}
	
	public void updateCompany(Company company) {
		companyDAO.updtaeCompany(company);
	}
	
	public Company getCompany(String companyName){
		return companyDAO.getCompany(companyName);
	}
	
	public Collection<Company> getAllCompanies(){
		return companyDAO.getAllCompanies();
	}
	
	public void createCustomer(Customer customer) {
		customerDAO.createCustomer(customer);
	}
	
	public void removeCustomer(Customer customer) {
		customerDAO.removeCustomer(customer);
	}
	
	public void updateCustomer(Customer customer) {
		customerDAO.updateCustomer(customer);
	}
	
	public Customer getCustomer(String customerName) {
		return customerDAO.getCustomer(customerName);
	}
	
	public Collection<Customer> getAllCustomers() {
		return customerDAO.getAllCustomers();
	}
	
	@Override
	public CouponClientFacade login(String name, String password, String clientType) {
		AdminFacade adminFacade = null;
		if (clientType == "Admin" && adminUserName == "admin" && password == "1234"){
			return adminFacade;
		}else{
			System.out.println("User Name and Password are incorrect!!! Please try again...");
		}
		return adminFacade;
	}
	
}
