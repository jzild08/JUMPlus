package dollarsbankapp.repository;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dollarsbankapp.model.Client;
import dollarsbankapp.model.Transaction;

public class MainBankDao {
	
	private Connection conn;
	private PreparedStatement ps;
	private ResultSet rs;
	private Transaction transaction;
	
	public MainBankDao() {
		this.conn = null;
		this.ps = null;
		this.rs = null;
	}
	
	public void createConnection() throws FileNotFoundException, IOException, SQLException, ClassNotFoundException {
		
		this.conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank", "root", "!smalZep0p0");
		
		if(conn != null) {
			
			System.out.println("Connected to the database \n");
			
		} else {
			
			System.out.println("Not connected");
		}
	}
	
	public void terminateConnection() throws SQLException {
		
		this.conn.close();
	}
	
	public Client userLogin(String username, String password) throws SQLException {
		
		Client login = new Client();
		this.ps = this.conn.prepareStatement("select * from user where username = ? and password = ?");
		this.ps.setString(1, username);
		this.ps.setString(2, password);
		this.rs = this.ps.executeQuery();
		
		if(this.rs.next()) {
			
			login.setAccountNumber(this.rs.getString("accountNumber"));
			login.setUsername(this.rs.getString("username"));
			login.setPassword(this.rs.getString("password"));
			login.setBalance(this.rs.getDouble("accountBalance"));
			login.setFirstName(this.rs.getString("firstName"));
			login.setLastName(this.rs.getString("lastName"));
		} else {
			
			login = null;
			System.err.println("\nUsername/Password is invalid");
		}
		
		return login;
	}
	
	public boolean checkUsername(String username) throws SQLException {
		String check = "";
		this.ps = conn.prepareStatement("select username from user where username = ?");
		this.ps.setString(1, username);
		
		this.rs = ps.executeQuery();
		
		if(this.rs.next()) {
			
			check = this.rs.getString("username");
		}
		
		if(check.equals(username)) {
			
			return true;
		} else {
			
			return false;
		}
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
		
		this.ps = this.conn.prepareStatement("insert into transaction values(?, ?, ?, ?, now(), ?)");
		this.ps.setString(1, this.transaction.getTransactionID());
		this.ps.setString(2, this.transaction.getAccountNumber());
		this.ps.setString(3, this.transaction.getTransactionType());
		this.ps.setDouble(4, this.transaction.getAmount());
		this.ps.setString(5, this.transaction.getSender());
		this.ps.executeUpdate();
	}
	
	//deposit
	public void deposit(Client client, double deposit) throws SQLException {
		
		this.ps = this.conn.prepareStatement("update user set accountBalance = accountBalance + ? where accountNumber = ?");
		this.ps.setDouble(1, deposit);
		this.ps.setString(2, client.getAccountNumber());
		int row = this.ps.executeUpdate();
		
		if(row > 0) {
			
			client.setBalance(client.getBalance() + deposit);
		}
		
		this.transaction = new Transaction(client.getAccountNumber(), "Deposit", deposit, "");
		
		while(checkTransactionID(this.transaction.getTransactionID()) == true) {
			
			this.transaction.setTransactionID(this.transaction.createTransactionNumber());
		}
		
		this.ps = this.conn.prepareStatement("insert into transaction values(?, ?, ?, ?, now(), ?)");
		this.ps.setString(1, this.transaction.getTransactionID());
		this.ps.setString(2, this.transaction.getAccountNumber());
		this.ps.setString(3, this.transaction.getTransactionType());
		this.ps.setDouble(4, this.transaction.getAmount());
		this.ps.setString(5, this.transaction.getSender());
		this.ps.executeUpdate();
	}
	
	//Tranfer funds
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
		
		this.ps = this.conn.prepareStatement("insert into transaction values(?, ?, ?, ?, now(), ?)");
		this.ps.setString(1, this.transaction.getTransactionID());
		this.ps.setString(2, this.transaction.getAccountNumber());
		this.ps.setString(3, this.transaction.getTransactionType());
		this.ps.setDouble(4, this.transaction.getAmount());
		this.ps.setString(5, this.transaction.getSender());
		
	}
	
	//Transaction History
	public void getTransactionHistory(Client client) throws SQLException {
		
		this.ps = this.conn.prepareStatement("select * from transaction where accountNumber = ? or username = ? order by transactionDate desc limit 5");
		this.ps.setString(1, client.getAccountNumber());
		this.ps.setString(2, client.getUsername());
		this.rs = this.ps.executeQuery();
		
		while(this.rs.next()) {
						
			System.out.println("TransactionID: " + this.rs.getString("transactionID") + "  TransactionType: " + this.rs.getString("transactionType") + "  Amount: " + this.rs.getDouble("amount") + "  Date and Time: " + this.rs.getTimestamp("transactionDate"));
		}
	}
	
	public void getClientInformation(Client client) {
		
		System.out.println(client.toString());
	}
	
	public void closeAllConnections() throws SQLException {
		
		this.ps.close();
		this.rs.close();
		this.conn.close();
	}
	
	//Registration
	public void registerClient(Scanner input) throws SQLException {
		
		System.out.println("\nWelcome to the bank registration form. Please enter values for the following fields:\n");
		Client newClient = new Client();
		
		newClient.createAccountNumber();
		System.out.println("First Name: ");
		newClient.setFirstName(input.next());
		System.out.println("Last Name: ");
		newClient.setLastName(input.next());
		System.out.println("Enter a username: ");
		newClient.setUsername(input.next());
		
		while(checkUsername(newClient.getUsername()) == true) {
				
			System.err.println("\nUsername does not match requirements... Enter a username: ");
			newClient.setUsername(input.next());
		}
		
		System.out.println("Enter a password: ");
		newClient.setPassword(input.next());
		
		
		while(newClient.getPassword().length() < 6) {
			
			System.err.println("\nPassword does not match requirements... Enter a password: ");
			newClient.setPassword(input.next());
		}
		
		System.out.println("Please enter initial deposit: ");
		newClient.setBalance(input.nextDouble());
		
		while(newClient.getBalance() < 0.01) {
			
			System.out.println("\nMust be above zero... Enter initial deposit: ");
			newClient.setBalance(input.nextDouble());
		}
		
		while(this.checkAccountNumber(newClient.getAccountNumber()) != false){
			
			newClient.createAccountNumber();
		}
		
		this.ps = this.conn.prepareStatement("insert into user values(?, ?, ?, ?, ?, ?)");
		this.ps.setString(1, newClient.getAccountNumber());
		this.ps.setString(2, newClient.getUsername());
		this.ps.setString(3, newClient.getPassword());
		this.ps.setDouble(4, newClient.getBalance());
		this.ps.setString(5, newClient.getFirstName());
		this.ps.setString(6, newClient.getLastName());
		
		int row = this.ps.executeUpdate();
		
		if(row > 0) {
			
			System.out.println("\n" + row + " row/s has been added\n");
		}
	}
	
	//A function that checks whether it is true or not there's an existing transactionID
	public boolean checkTransactionID(String transactionID) throws SQLException {
		
		String check = "";
		this.ps = conn.prepareStatement("select transactionID from transaction where transactionID = ?");
		this.ps.setString(1, transactionID);
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
	
	public boolean checkAccountNumber(String accountNumber) throws SQLException {
		
		String check = "";
		this.ps = conn.prepareStatement("select accountNumber from user where accountNumber = ?");
		this.ps.setString(1, accountNumber);
		this.rs = this.ps.executeQuery();
		while(this.rs.next()) {
			
			check = this.rs.getString("accountNumber");
		}
		
		if (check.equals("transactionID")) {
			
			return true;
		} else {
			
			return false;
		}
	}
}
