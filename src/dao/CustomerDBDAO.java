package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import beans.Company;
import beans.Coupon;
import beans.CouponType;
import beans.Customer;
import configuration.ConnectionPool;

public class CustomerDBDAO implements CustomerDAO{

	ConnectionPool pool = new ConnectionPool();
	ResultSet rs = null;
	DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
	Connection connection;
	Statement statement;
	Date date;
	Coupon coupon;
	Customer customer;
	String currentDate;
	int counter;
	boolean flag = false;
	
	public Statement preparedStatemt(){
		
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
	
	public CustomerDBDAO(Coupon coupon) {
		this.coupon = coupon;
		customer = new Customer();
		date = new Date();
		this.currentDate = format.format(date);
	}

	@Override
	public void createCustomer(Customer customer) {
	
		try {
			if(!checkExisting(customer)){
				preparedStatemt().executeUpdate("insert into customer(customer_name,password) values ('"+customer.getCustomerName()+"' , '"+customer.getPassword()+"')");
				System.out.println("Customer " + customer.getCustomerName() + " created successfully!!!");
			}else{
				System.out.println("Customer " + customer.getCustomerName() + " already exists...");
			}
			
		} catch (SQLException e) {

			e.printStackTrace();
		}finally {
			closeStatement(statement);
			pool.returnConnection(connection);
		}
	}

	@Override
	public void removeCustomer(Customer customer) {
		try {
			if(checkExisting(customer)){
				preparedStatemt().executeUpdate("delete from customer where customer_name = '" + customer.getCustomerName() + "'");
				System.out.println("Customer " + customer.getCustomerName() + " remove successfully!!!");
			}else{
				System.out.println("Customer " + customer.getCustomerName() + " not existing...");
			}
			
		} catch (SQLException e) {

			e.printStackTrace();
		}finally {
			closeStatement(statement);
			pool.returnConnection(connection);
		}
		
	}

	@Override
	public void updateCustomer(Customer customer) {
		try {
			if (checkExisting(customer)){
				rs = preparedStatemt().executeQuery("select * from customer where customer_name = '" + customer.getCustomerName() + "'");
				
					while (rs.next()) {
						try {
							if(!customer.getPassword().equalsIgnoreCase(rs.getString("password")) && customer.getPassword() != null && customer.getPassword() != "" && customer.getPassword() != "NULL"){
								preparedStatemt().executeUpdate("update customer set password = '" + customer.getPassword() + "'");
								System.out.println("Customer : " + customer.getCustomerName() + " password successfully update!!!");
							}else{
								System.out.println("Customer values are identical and is nothing to update...");
							}
						}catch (SQLException e) {
							e.printStackTrace();
						}
					}
				
				}else{
					System.out.println("Customer " + customer.getCustomerName() + " doesn't exists...");
				}
	
		} catch (SQLException e1) {

			e1.printStackTrace();
		}finally {
			try {
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			closeStatement(statement);
			pool.returnConnection(connection);
		}
		
	}

	@Override
	public void updateCustomerCoupon(String operation, Coupon coupon){
		int customeId = customer.getCustomerId();
		int couponId = coupon.getCouponId();
		
		try {
			rs = preparedStatemt().executeQuery("select * from customer_coupon");
			while(rs.next()){
				if(rs.getInt("coupon_id") == couponId){
					counter++;
				}
			}
			if(coupon.getAmount() > 0 && coupon.getEndDate().before(date) && counter == 0){	
				if(operation == "create"){
					preparedStatemt().executeUpdate("insert into custoemr_coupon values("+customeId+","+couponId+")");
				}
			}	
			if(operation == "remove"){
				preparedStatemt().executeUpdate("delete from customer_coupon where customer_id = " + customeId + " and coupon_id = " + couponId);
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			closeStatement(statement);
			pool.returnConnection(connection);
		}
	}
	
	@Override
	public Customer getCustomer(String customerName) {
		ResultSet innerRs = null;
		try {
			innerRs = preparedStatemt().executeQuery("select * from customer where customer_name = '" + customerName + "'");
			while (innerRs.next()) {
				customer.setCustomerId(innerRs.getInt("customer_id"));
				customer.setCustomerName(innerRs.getString("customer_name"));
				customer.setPassword(innerRs.getString("password"));
			}
			
		} catch (SQLException e) {

			e.printStackTrace();
		}finally {
			try {
				innerRs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			closeStatement(statement);
			pool.returnConnection(connection);
		}
		
		return customer;
	}

	@Override
	public Collection<Customer> getAllCustomers() {
		Collection<Customer> customers = new ArrayList<Customer>();
		rs = null;
		try {
			rs = preparedStatemt().executeQuery("select * from customer");
			while (rs.next()) {
				customers.add(getCustomer(rs.getString("customer_name")));
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}finally {
			try {
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			closeStatement(statement);
			pool.returnConnection(connection);
		}

		return customers;
	}

	@Override
	public Collection<Coupon> getCoupons() {
		Collection<Coupon> coupons = new ArrayList<Coupon>();
		Coupon coupon = null;
		
		try {
			rs = preparedStatemt().executeQuery("select * from coupon");
			while (rs.next()) {
				coupon.setCouponId(rs.getInt("coupon_id"));
				coupon.setTitle(rs.getString("title"));
				coupon.setStartDate(rs.getDate("start_date"));
				coupon.setEndDate(rs.getDate("end_date"));
				coupon.setAmount(rs.getInt("amount"));
				//coupon.setType(rs.getString("type"));
				coupon.setMessage(rs.getString("message"));
				coupon.setPrice(rs.getInt("price"));
				coupon.setImage(rs.getString("image"));
				
				coupons.add(coupon);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			closeStatement(statement);
			pool.returnConnection(connection);
		}
		return coupons;
	}

	@Override
	public boolean login(String customerName, String password) {
		boolean flag = false;
		try {
			rs = preparedStatemt().executeQuery("select * from customer where customer_name = '" + customerName + "'");
			while (rs.next()) {
				if(password.equalsIgnoreCase(rs.getString("password"))){
					flag = true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			closeStatement(statement);
			pool.returnConnection(connection);
		}
		return flag;
	}
	
	private boolean checkExisting(Customer customer){
		if (customer.getCustomerName() != null){
			getAllCustomers().forEach((cust) -> {
				if (customer.getCustomerName().equalsIgnoreCase(cust.getCustomerName())){
					flag = true;
				}
			});
		}
		return flag;
	}
	
	
}
