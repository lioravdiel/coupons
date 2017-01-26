package configuration;

public class Configuration {

	protected int max_connection;
	protected String url;
	protected String driver;
	protected String username;
	protected String password;

	private Configuration() {
		max_connection = 5;
		driver = "oracle.jdbc.driver.OracleDriver";
		url = "jdbc:oracle:thin:@localhost:1521:orcl";
	    username = "c##coupons";
		password = "sys";
		
	}
	
	private static Configuration configuration = null;
	
	public synchronized static Configuration getInstance(){
		if(configuration == null){
			configuration = new Configuration();
		}
		return configuration;
	}
	
	
	
}
