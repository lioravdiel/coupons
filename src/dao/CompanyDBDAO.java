package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;

import beans.Company;
import beans.Coupon;
import configuration.ConnectionPool;

public class CompanyDBDAO implements CompanyDAO{


	ConnectionPool pool = new ConnectionPool();
	private int counter = 0;
	ResultSet rs = null;
	Connection connection;
	DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
	Statement statement;
	boolean flag = false;
	Company company;
	Coupon coupon;
	
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
	
	public CompanyDBDAO(Coupon coupon) {
		company = new Company();
		this.coupon = coupon;
	}

	@Override
	public void createCompany(Company company) {
		try {
			if (!checkIfExists(company)){
				preparedStatemt().executeUpdate("insert into company(company_name,password,email) values ('"+company.getCompanyName()+"' , '"+company.getPassword()+"', '"+company.getEmail()+"')");
				System.out.println("Company : " + company.getCompanyName() + " created successfully!");
			}else{
				System.out.println("Company: " + company.getCompanyName() + " Already exists...");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			closeStatement(statement);
			pool.returnConnection(connection);
		}
		
	}

	@Override
	public void removeCompany(Company company) {
		try {
			if(checkIfExists(company)){
				preparedStatemt().executeUpdate("delete from company where company_name = '" + company.getCompanyName() + "'");
				System.out.println("Company " + company.getCompanyName() + " removed successfully!!!");
			}else{
				System.out.println("The company : " + company.getCompanyName() + " doesn't exists...");
			}
			
		} catch (SQLException e) {

			e.printStackTrace();
		}finally {
			closeStatement(statement);
			pool.returnConnection(connection);
		}
		
	}
	
	@Override
	public void updtaeCompany(Company company) {
	
		try {
			if (checkIfExists(company)){
				rs = preparedStatemt().executeQuery("select * from company where company_name = '" + company.getCompanyName() + "'");
				
					while (rs.next()) {
						try {
							if(!company.getPassword().equalsIgnoreCase(rs.getString("password")) && company.getPassword() != null && company.getPassword() != "" && company.getPassword() != "NULL"){
								preparedStatemt().executeUpdate("update company set password = '" + company.getPassword() + "'");
								System.out.println("Company : " + company.getCompanyName() + " password successfully update!!!");
							}else if(!company.getEmail().equalsIgnoreCase(rs.getString("email")) && company.getEmail() != null && company.getEmail() != "" && company.getEmail() != "NULL"){ 
								preparedStatemt().executeUpdate("update company set email = '" + company.getEmail() +"'");
								System.out.println("Company : " + company.getCompanyName() + " email successfully update!!!");
							}else{
								System.out.println("Company values are identical and is nothing to update...");
							}
						}catch (SQLException e) {
							e.printStackTrace();
						}
					}
				
				}else{
					System.out.println("Company " + company.getCompanyName() + " doesn't exists...");
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
	public void updateCompanyCoupon(String operation, Coupon coupon){
		int companyId = company.getCompanyId();
		int couponId = coupon.getCouponId();
		
		try {
			if(operation == "create"){
				preparedStatemt().executeUpdate("insert into company_coupon values("+companyId+","+couponId+")");
			}else if(operation == "remove"){
				preparedStatemt().executeUpdate("delete from company_coupon where company_id = " + companyId + " and coupon_id = " + couponId);
			}else if(operation == "removeByCompany"){
				preparedStatemt().executeUpdate("delete from company_coupon where company_id = " + companyId);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			closeStatement(statement);
			pool.returnConnection(connection);
		}
	}
	
	@Override
	public Company getCompany(String companyName) {
		ResultSet innerRs = null;
		try {
			innerRs = preparedStatemt().executeQuery("select * from company where company_name = '" + companyName + "'");
			while (innerRs.next()) {
				company.setCompanyId(innerRs.getInt("company_id"));
				company.setCompanyName(innerRs.getString("company_name"));
				company.setPassword(innerRs.getString("password"));
				company.setEmail(innerRs.getString("email"));
			}
			
		} catch (SQLException e) {

			e.printStackTrace();
		}finally {
			try {
				innerRs.close();
			} catch (SQLException e) {

				e.printStackTrace();
			}
			closeStatement(statement);
			pool.returnConnection(connection);
		}
		
		return company;
	}

	@Override
	public Collection<Company> getAllCompanies() {
		Collection<Company> companies = new ArrayList<Company>();
		rs = null;
		
		try {
			rs = preparedStatemt().executeQuery("select * from company");
			while (rs.next()) {
				companies.add(getCompany(rs.getString("company_name")));
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
		
		
		return companies;
		
	}

	@Override
	public Collection<Coupon> getCoupons() {
		Collection<Coupon> coupons = new ArrayList<Coupon>();
		Coupon coupon = null;
		
		ResultSet rs = null;
		
		try {
			rs = preparedStatemt().executeQuery("select from coupon");
			while (rs.next()) {
				coupon.setCouponId(rs.getInt("coupon_id"));
				coupon.setTitle(rs.getString("title"));
				coupon.setStartDate(rs.getDate("start_date"));
				coupon.setEndDate(rs.getDate("end_date"));
				coupon.setAmount(rs.getInt("amount"));
				//coupon.setType(rs.getObject("type"));
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

	private boolean checkIfExists(Company company){
		if(company.getCompanyName() != null){
			getAllCompanies().forEach((comp) -> {
				if(company.getCompanyName().equalsIgnoreCase(comp.getCompanyName())){
					flag = true;
				}
			});
		}
		return flag;
	}
	
	@Override
	public boolean login(String companyName, String password) {
		boolean flag = false;
		ResultSet rs = null;
		
		try {
			rs = preparedStatemt().executeQuery("select * from company where company_name = '" + companyName + "'");
			
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
	
}
