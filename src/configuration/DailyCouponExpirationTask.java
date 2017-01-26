package configuration;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import beans.Company;
import beans.Coupon;
import beans.Customer;
import dao.CompanyDAO;
import dao.CompanyDBDAO;
import dao.CouponDAO;
import dao.CouponDBDAO;
import dao.CustomerDAO;
import dao.CustomerDBDAO;

public class DailyCouponExpirationTask implements Runnable{


	ConnectionPool pool = new ConnectionPool();
	ResultSet rs;
	DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
	Date date;
	String currentDate;
	Coupon coupon;
	CouponDAO couponDAO;
	CustomerDAO customerDAO;
	CompanyDAO companyDAO;
	boolean flag = true;
	
	public Statement preparedStatemt(){
		
		Connection connection = null;
		Statement statement = null;
		
		try {
			connection = pool.getConnection();
			statement = connection.createStatement();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
			
		return statement;
	}
	
	public void closeStatement(Statement statement){
		try {
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public DailyCouponExpirationTask(CouponDAO couponDAO, CompanyDAO companyDAO, CustomerDAO customerDAO, Coupon coupon) {
		date = new Date();
		this.currentDate = format.format(date);
		this.coupon = new Coupon();
		this.couponDAO = couponDAO;
		this.companyDAO = companyDAO;
		this.customerDAO = customerDAO;
	}
	
	
	public void setQuit(boolean flag){
		this.flag = flag;
	}
	
	public boolean getQuit() {
		return flag;
	}
	
	@Override
	public void run() {
		
		while (getQuit()) {
			
			try {
				rs = preparedStatemt().executeQuery("select * from coupon");
				while (rs.next()) {
					if(rs.getDate("end_date").after(date)){
						couponDAO.removeCoupon(coupon);
						customerDAO.updateCustomerCoupon("remove", coupon);
						companyDAO.updateCompanyCoupon("remove", coupon);
					}
				}
				
				try {
					Thread.sleep(5000);//24 hour: 86400000
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}

	
	
}
