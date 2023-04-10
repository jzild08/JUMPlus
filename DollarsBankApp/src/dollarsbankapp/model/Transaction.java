package dollarsbankapp.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Transaction {
	
	private String transactionID;
	private String accountNumber;
	private String transactionType;
	private double amount;
	private String localDate;
	private String sender;
	
	public String createTransactionNumber() {
		
		String header="TRS-";
		String characters = "abcdefghijklmnopqrstuvwxyz1234567890";
		String trans = "";
		
		trans += header;
		
		for(int i = 0; i < 6; i ++) {
			
			trans += characters.charAt((int)Math.round(Math.random() * (characters.length() - 1)));
		}
		
		return trans.toUpperCase();
	}
	
	public Transaction() {}

	public Transaction(String accountNumber, String transactionType, double amount, String sender) {
		super();
		this.transactionID = createTransactionNumber();
		this.accountNumber = accountNumber;
		this.transactionType = transactionType;
		this.amount = amount;
		this.localDate = new SimpleDateFormat("MM/dd/yy").format(new Date());
		this.sender = sender;
	}

	public String getTransactionID() {
		return transactionID;
	}

	public void setTransactionID(String transactionID) {
		this.transactionID = transactionID;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public double getAmount() {
		return amount;
	}
	
	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getLocalDate() {
		return localDate;
	}

	public void setLocalDate(String localDate) {
		this.localDate = localDate;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	@Override
	public String toString() {
		return "Transaction [transactionID=" + transactionID + ", transactionType=" + transactionType + ", amount="
				+ amount + ", sender=" + sender + "]";
	}
}
