package system;


import beans.Company;
import beans.Coupon;
import beans.Customer;
import configuration.ConnectionPool;
import configuration.DailyCouponExpirationTask;
import dao.CompanyDAO;
import dao.CompanyDBDAO;
import dao.CouponDAO;
import dao.CouponDBDAO;
import dao.CustomerDAO;
import dao.CustomerDBDAO;
import facade.AdminFacade;
import facade.CompanyFacade;
import facade.CouponClientFacade;
import facade.CustomerFacade;

public class CouponSystem implements CouponClientFacade{

	ConnectionPool pool = new ConnectionPool();
	CouponDAO couponDAO;
	CustomerDAO customerDAO;
	CompanyDAO companyDAO;
	Customer customer;
	Coupon coupon;
	Company company;
	AdminFacade adminFacade;
	CompanyFacade companyFacade;
	CustomerFacade customerFacade;
	
	DailyCouponExpirationTask dailyCouponExpirationTask;
	
	private static CouponSystem couponSystem;
	
	private CouponSystem() {
		coupon = new Coupon();
		this.companyDAO = new CompanyDBDAO(coupon);
		this.couponDAO = new CouponDBDAO(coupon);
		this.customerDAO = new CustomerDBDAO(coupon);
		/*dailyCouponExpirationTask = new DailyCouponExpirationTask(couponDAO, companyDAO, customerDAO, coupon);
		new Thread(dailyCouponExpirationTask).start();*/
				
	}

	public synchronized static CouponSystem getInstance() {
		if (couponSystem == null)
			couponSystem = new CouponSystem();
		return couponSystem;
	}
	
	@Override
	public CouponClientFacade login(String name, String password, String clientType) {
		CouponClientFacade clientFacade = null;
		
		switch (clientType) {
		case "Admin":
			clientFacade = new AdminFacade(companyDAO, customerDAO);
			clientFacade.login(name, password, clientType);
			break;
		case "Company":
			clientFacade = new CompanyFacade(couponDAO, companyDAO, company);
			clientFacade.login(name, password, clientType);
			break;
		case "Customer":
			clientFacade = new CustomerFacade(customerDAO, couponDAO, companyDAO, customer);
			clientFacade.login(name, password, clientType);
			break;
		}
		
		return clientFacade;
		
	}
	
	public void shutdown(){
		dailyCouponExpirationTask.setQuit(false);
		pool.closeAllConnections();
	}
	
	
	
}
