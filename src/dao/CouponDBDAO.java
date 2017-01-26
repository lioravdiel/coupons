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

public class CouponDBDAO implements CouponDAO{

	ConnectionPool pool = new ConnectionPool();
	ResultSet rs;
	Connection connection;
	DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
	String currentDate;
	Date date;
	Statement statement;
	Coupon coupon;
	boolean flag = false;
	int counter; 
	
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
	
	public CouponDBDAO(Coupon coupon) {
		this.coupon = coupon;
		date = new Date();
		this.currentDate = format.format(date);
	}

	@Override
	public void createCoupon(Coupon coupon) {
		
		try {
			if(!checkIfExists(coupon)){
				preparedStatemt().executeUpdate("insert into coupon (title, start_date, end_date, amount, type, message, price, image) values ('"+coupon.getTitle()+"' ,TO_DATE('"+coupon.getStartDate()+" 00:00:00','YYYY-MM-DD HH24:MI:SS'),TO_DATE('"+coupon.getEndDate()+" 00:00:00','YYYY-MM-DD HH24:MI:SS') ,'"+coupon.getAmount()+"' ,'"+coupon.getType()+"' ,'"+coupon.getMessage()+"' ,'"+coupon.getPrice()+"','"+coupon.getImage()+"')");
				System.out.println("Coupon " + coupon.getTitle() + " created successfully!!!");
			}else{
				System.out.println("Coupon " + coupon.getTitle() + " already exists...");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			closeStatement(statement);
			pool.returnConnection(connection);
		}
			
		}
		
	

	@Override
	public void removeCoupon(Coupon coupon) {
		try {
			if(checkIfExists(coupon)){
				preparedStatemt().executeUpdate("delete from coupon where title = '" + coupon.getTitle() + "'");
				System.out.println("Coupon : " + coupon.getTitle() + " successfully removed!!!");
			}else{
				System.out.println("Coupon : " + coupon.getTitle() + " not exists...");
			}
			
		} catch (SQLException e) {

			e.printStackTrace();
		}finally {
			closeStatement(statement);
			pool.returnConnection(connection);
		}
		
	}

	@Override
	public void updateCoupon(Coupon coupon) {
		
		try {
			rs = preparedStatemt().executeQuery("select * from coupon where coupon_id = " + coupon.getCouponId());
		
			while (rs.next()) {
				
				if(coupon.getEndDate() != rs.getDate("end_date") && coupon.getEndDate() != null){
					try {
						preparedStatemt().executeUpdate("updtae coupon set end_date = '" + coupon.getEndDate() + "'");
					} catch (SQLException e) {
						e.printStackTrace();
					}finally {
						closeStatement(statement);
					}
				}
				if(coupon.getPrice() != rs.getDouble("price") && coupon.getPrice() > 0){
					try {
						preparedStatemt().executeUpdate("updtae coupon set price = '" + coupon.getPrice() + "'");
					} catch (SQLException e) {
						e.printStackTrace();
					}finally {
						closeStatement(statement);
						pool.returnConnection(connection);
					}
				}
			}
		} catch (SQLException e1) {

			e1.printStackTrace();
		}
	}

	@Override
	public Coupon getCoupon(int couponId) {
		ResultSet innerRs = null; 
		try {
			innerRs = preparedStatemt().executeQuery("select * from coupon where coupon_id = '" + couponId + "'");
		
			while (innerRs.next()) {
				coupon.setCouponId(innerRs.getInt("coupon_id"));
				coupon.setTitle(innerRs.getString("title"));
				coupon.setStartDate(innerRs.getDate("start_date"));
				coupon.setEndDate(innerRs.getDate("end_date"));
				coupon.setAmount(innerRs.getInt("amount"));
				coupon.setType(CouponType.CAMPING);
				coupon.setMessage(innerRs.getString("message"));
				coupon.setPrice(innerRs.getInt("price"));
				coupon.setImage(innerRs.getString("image"));
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
		
		return coupon;
	}

	@Override
	public Collection<Coupon> getAllCoupons() {
		Collection<Coupon> coupons = new ArrayList<Coupon>();
		rs = null;
		try {
			rs = preparedStatemt().executeQuery("select * from coupon");
			while (rs.next()) {
				coupons.add(getCoupon(rs.getInt("coupon_id")));
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
	public Collection<Coupon> getCouponByType(CouponType type) {
		Collection<Coupon> coupons = new ArrayList<Coupon>();
		
		try {
			rs = preparedStatemt().executeQuery("select coupon_id from coupon where type = '" + type + "'");
			
			while (rs.next()) {
				coupons.add(getCoupon(rs.getInt("coupon_id")));
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
	public Collection<Coupon> getCouponByPrice(double price) {
		Collection<Coupon> coupons = new ArrayList<Coupon>();
		
		try {
			rs = preparedStatemt().executeQuery("select coupon_id from coupon where price = '" + price + "'");
			
			while (rs.next()) {
				coupons.add(getCoupon(rs.getInt("coupon_id")));
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
	
	private boolean checkIfExists(Coupon coupon){
			getAllCoupons().forEach((coup) -> {
				if(coupon.getTitle().equalsIgnoreCase(coup.getTitle())){
					flag = true;
			}
		});
		return flag;	
	}
}
