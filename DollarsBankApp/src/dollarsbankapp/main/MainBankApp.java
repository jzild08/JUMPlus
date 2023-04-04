package dollarsbankapp.main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

import dollarsbankapp.model.Client;
import dollarsbankapp.repository.MainBankDao;

public class MainBankApp {

	public static void main(String[] args) throws FileNotFoundException, IOException, SQLException {
		
		MainBankDao bank = null;
		Scanner input = new Scanner(System.in);
		
		String username = "";
		String password = "";
		
		while (bank.createConnection() != null) {
			
			System.out.println("\nWelcome to Dollars Bank App\n");
			
			System.out.println("\nConsider the following options: \n1 - Login\n2 - Create User \n3- Exit App");
			int selection = 0;
			selection = input.nextInt();
			
			switch(selection) {
			
				case 1:
					System.out.println("Please enter username: ");
					username = input.next();
					
					System.out.println("Please enter password: ");
					password = input.next();
					
					Client client = new Client();
					client = bank.userLogin(username, password);
					
					while(client != null) {
						
						int option = 0;
						System.out.println("\1 - ");
					}
					
			}
		}
	}
}
