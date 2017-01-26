package configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Stack;

public class ConnectionPool {

	Stack<Connection> availibleConnections = new Stack<Connection>();
	
	public ConnectionPool() {
		initializeConnectionPool(); 
	}
	
	private void initializeConnectionPool(){
		while(checkIfConnectionPoolIsFull()){
			availibleConnections.add(cretaeNewConnectionPool());
		}
	}
	
	private boolean checkIfConnectionPoolIsFull(){
		final int max_connection_size= Configuration.getInstance().max_connection;
		if (availibleConnections.size() < max_connection_size)
			return true;
		return false;
	}
	
	private Connection  cretaeNewConnectionPool(){
		Configuration configuration = Configuration.getInstance();
		Connection connection = null;
		
		try {
			Class.forName(configuration.driver);
			connection = DriverManager.getConnection(configuration.url, configuration.username, configuration.password);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return connection;
	}
	
	public synchronized Connection getConnection() throws InterruptedException{
		Connection connection = null;
		if(availibleConnections.size() > 0){
			connection = availibleConnections.pop();
		}else{
			wait();
			
		}
		return connection;
	}
	
	public synchronized void returnConnection(Connection connection){
		availibleConnections.add(connection);
		notify();
	}
		
	public void closeAllConnections(){
		for(Connection con : availibleConnections){
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
