package dollarsbankapp.repository;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import dollarsbankapp.model.Client;
import dollarsbankapp.model.Transaction;

public class MainBankDao {
	
	private Connection conn;
	private PreparedStatement ps;
	private ResultSet rs;
	private Transaction transaction;
	public MainBankDao() {}
	
	public Connection createConnection() throws FileNotFoundException, IOException, SQLException {
		
		Properties prop = new Properties();
		prop.load(new FileInputStream("Resources/config.properties"));
		
		final String URL = prop.getProperty("url");
		final String USERNAME = prop.getProperty("username");
		final String PASSWORD = prop.getProperty("passwrod");
		
		this.conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
		
		if(conn != null) {
			
			System.out.println("Connected to the database \n");
			return this.conn;
		} else {
			
			conn = null;
			return conn;
		}
	}
	
	public Client userLogin(String username, String password) throws SQLException {
		
		Client login = new Client();
		this.ps = this.conn.prepareStatement("select * from user where username = ? and password = ?");
		this.ps.setString(1, username);
		this.ps.setString(2, password);
		this.rs = this.ps.executeQuery();
		
		while(this.rs.next()) {
			
			login.setAccountNumber(this.rs.getString("accountNumber"));
			login.setUsername(this.rs.getString("username"));
			login.setPassword(this.rs.getString("password"));
			login.setBalance(this.rs.getDouble("accountBalance"));
			login.setFirstName(this.rs.getString("userFirstName"));
			login.setLastName(this.rs.getString("userLastName"));
		}
		
		return login;
	}
	
	public boolean checkUsername(String username) throws SQLException {
		
		boolean found = false;
		this.ps = conn.prepareStatement("select username from user where username = ?");
		this.ps.setString(1, username);
		
		this.rs = ps.executeQuery();
		
		while(this.rs.next()) {
			
			if(this.rs.getString("username") == username) {
				
				found = true;
			}
		}
		
		return found;
	}
	
	//withdraw method takes in client data to retrieve both the account number and balance of the client's account
	public void withdraw(Client client, double withdraw) throws SQLException{
		
		this.ps = this.conn.prepareStatement("update user set accountBalance = accountBalance - ? where accountNumber = ?");
		this.ps.setDouble(1, withdraw);
		this.ps.setString(2, client.getAccountNumber());
		int row = this.ps.executeUpdate();
		
		if(row > 0) {
			double currentBalance = client.getBalance() - withdraw;
			client.setBalance(currentBalance);
		}
		
		this.transaction = new Transaction(client.getAccountNumber(), "Withdrawal", withdraw, null);
		
		//check the transaction first and make sure it doesn't have a duplicate
		while(checkTransactionID(this.transaction.getTransactionID()) == true) {
			
			this.transaction.setTransactionID(this.transaction.createTransactionNumber());
		}
		
		this.ps = this.conn.prepareStatement("insert into transaction values(?, ?, ?, ?, ?, ?");
		this.ps.setString(1, this.transaction.getTransactionID());
		this.ps.setString(2, this.transaction.getAccountNumber());
		this.ps.setString(3, this.transaction.getTransactionType());
		this.ps.setDouble(4, this.transaction.getAmount());
		this.ps.setString(5, this.transaction.getLocalDate());
		this.ps.setString(6, this.transaction.getSender());
		this.ps.executeUpdate();
	}
	
	public void deposit(Client client, double deposit) throws SQLException {
		
		this.ps = this.conn.prepareStatement("update user set accountBalance = accountBalance + ? where accountNumber = ?");
		this.ps.setDouble(1, deposit);
		this.ps.setString(2, client.getAccountNumber());
		int row = this.ps.executeUpdate();
		
		if(row > 0) {
			
			client.setBalance(client.getBalance() - deposit);
		}
		
		this.transaction = new Transaction(client.getAccountNumber(), "Deposit", deposit, "");
		
		while(checkTransactionID(this.transaction.getTransactionID()) == true) {
			
			this.transaction.setTransactionID(this.transaction.createTransactionNumber());
		}
		
		this.ps = this.conn.prepareStatement("insert into transaction values(?, ?, ?, ?, ?, ?");
		this.ps.setString(1, this.transaction.getTransactionID());
		this.ps.setString(2, this.transaction.getAccountNumber());
		this.ps.setString(3, this.transaction.getTransactionType());
		this.ps.setDouble(4, this.transaction.getAmount());
		this.ps.setString(5, this.transaction.getLocalDate());
		this.ps.setString(6, this.transaction.getSender());
		this.ps.executeUpdate();
	}
	
	public void transferFunds(Client client, double transfer, String recipient) throws SQLException {
		
		this.ps = this.conn.prepareStatement("update user set accountBalance = accountBalance - ? where accountNumber = ?");
		this.ps.setDouble(1, transfer);
		this.ps.setString(2, client.getAccountNumber());
		int row = this.ps.executeUpdate();
		
		if(row > 0) {
			
			client.setBalance(client.getBalance() - transfer);
		}
		
		this.transaction = new Transaction(client.getAccountNumber(), "Transfer", transfer, client.getUsername());

		while(checkTransactionID(this.transaction.getTransactionID()) == true) {
			
			this.transaction.setTransactionID(this.transaction.createTransactionNumber());
		}
		
		this.ps = this.conn.prepareStatement("insert into transaction values(?, ?, ?, ?, ?, ?)");
		this.ps.setString(1, this.transaction.getTransactionID());
		this.ps.setString(2, this.transaction.getAccountNumber());
		this.ps.setString(3, this.transaction.getTransactionType());
		this.ps.setDouble(4, this.transaction.getAmount());
		this.ps.setString(5, this.transaction.getLocalDate());
		this.ps.setString(6, this.transaction.getSender());
		
	}
	
	public void transactionHistory(Client client) {
		
		client.toString();
	}
	
	public void closeAllConnections() throws SQLException {
		
		this.ps.close();
		this.rs.close();
		this.conn.close();
	}
	
	//A function that checks whether it is true or not there's an existing transactionID
	public boolean checkTransactionID(String transactionID) throws SQLException {
		
		String check = "";
		this.ps = conn.prepareStatement("select transactionID from transaction where transactionID = ?");
		this.rs = this.ps.executeQuery();
		while(this.rs.next()) {
			
			check = this.rs.getString("transactionID");
		}
		
		if (check.equals("transactionID")) {
			
			return true;
		} else {
			
			return false;
		}
	}
}
