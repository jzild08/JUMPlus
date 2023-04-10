package dollarsbankapp.main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

import dollarsbankapp.model.Client;
import dollarsbankapp.repository.MainBankDao;

public class MainBankApp {

	public static void main(String[] args) throws FileNotFoundException, IOException, SQLException, ClassNotFoundException {
		
		Scanner input = new Scanner(System.in);
		MainBankDao bank = new MainBankDao();
		bank.createConnection();
		
		boolean runApp = true;
		while (runApp != false) {
			
			System.out.println("\nWelcome to Dollars Bank App\n");
			
			System.out.println("\nConsider the following options: \n1 - Login\n2 - Create User \n3 - Exit App");
			int selection = 0;
			selection = input.nextInt();
			
			switch(selection) {
				
				case 1:
					
					String username = "";
					String password = "";
					System.out.println("\nPlease enter username: ");
					username = input.next();
					
					System.out.println("Please enter password: ");
					password = input.next();
					
					Client client = new Client();
					client = bank.userLogin(username, password);
					
					while(client != null) {
						
						int option = 0;
						System.out.println("\n1 - Withdraw \n2 - Deposit \n3 - Transfer \n4 - Transaction History \n5 - Account Information \n6 - Exit App");
						System.out.println("option:  ");
						option = input.nextInt();
						
						switch(option) {
						
							case 1:
								
								System.out.println("\nWithdraw\n");
								double withdraw = 0;
								
								System.out.println("\nPlease enter amount: $");
								withdraw = input.nextDouble();

								while (withdraw < 0.01) {
									
									System.err.println("\nEnter a numnber greater than 0\n");
									System.out.println("Please enter amount: $");
									withdraw = input.nextDouble();
								}
								
								bank.withdraw(client, withdraw);
								System.out.println("\nUsername:  " + client.getUsername() + " Account Number:  " + client.getAccountNumber() + " Balance:  " + client.getBalance() + "\n");
								break;
								
							case 2:
								
								System.out.println("\nDeposit\n");
								double deposit = 0;
								
								System.out.println("\nPlease enter deposit amount"
										+ ":");
								deposit = input.nextDouble();

								while (deposit < 0.01) {
									
									System.err.println("\nEnter a numnber greater than 0\n");
									System.out.println("Please enter amount: $");
									deposit = input.nextDouble();
								}
								
								bank.deposit(client, deposit);
								System.out.println("\nUsername:  " + client.getUsername() + " Account Number:  " + client.getAccountNumber() + " Balance:  " + client.getBalance() + "\n");
								break;
								
							case 3:
								
								System.out.println("\nTransfer\n");
								double transfer = 0;
								
								System.out.println("\nPlease enter amoun3t: $");
								transfer = input.nextDouble();

								while (transfer< 0.01) {
									
									System.err.println("\nEnter a numnber greater than 0\n");
									System.out.println("Please enter amount: $");
									transfer = input.nextDouble();
								}
								
								String recipient = "";
								System.out.println("\nPlease Enter the recipient's username: ");
								recipient = input.next();
								
								bank.transferFunds(client, transfer, recipient);
								System.out.println("\nUsername:  " + client.getUsername() + " Account Number:  " + client.getAccountNumber() + " Balance:  " + client.getBalance() + "\n");
								break;
								
							case 4:
								
								System.out.println("\nTransaction History\n");
								bank.getTransactionHistory(client);
								break;
							case 5:
								
								System.out.println("\nAccount Information\n");
								bank.getClientInformation(client);
							
								break;
							
							case 6:
								
								System.out.println("\nLogging out\n");
								client = null;
								break;
								
							default:
								
								System.err.println("\nInvalid Option\n");
								break;
						}
					}
					break;
				case 2:
					
					bank.registerClient(input);
					break;
					
				case 3:
					
					System.out.println("Exiting App...");
					bank.terminateConnection();
					runApp = false;
					break;
				
				default:
					
					System.err.println("\nPlease Enter a valid sellection\n");
					break;
			}
		}
	}
}
