package dollarsbankapp.model;

public class Client {
	
	private String accountNumber;
	private String firstName;
	private String lastName;
	private String username;
	private String password;
	private double balance;
	
	//Setting an account number;
	//made public so that it can be called when creating a user to determine
	//whether or not the accountNumber exists;
	public String createAccountNumber() {
		
		String header="USR";
		String characters = "abcdefghijklmnopqrstuvwxyz1234567890";
		
		String an = "";
		
		an += header;
		
		for(int i = 0; i < 7; i ++) {
			
			an += characters.charAt((int)Math.round(Math.random() * (characters.length() - 1)));
		}
		
		return an;
	}
	
	public Client() {}

	public Client(String firstName, String lastName, String username, String password,
			double balance, String accountNumber) {
		super();
		this.accountNumber = accountNumber;
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.password = password;
		this.balance = balance;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}
}
