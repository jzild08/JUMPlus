package Controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

import ColorDesign.ColorPrompts;
import CustomerDAO.CustomerDAO;
import Model.Customers;
import Model.Movies;
import MovieDAO.MovieDAO;
import Repository.MovieAppRepository;

public class MovieController {

	public static void main(String[] args) throws SQLException, FileNotFoundException, IOException, ClassNotFoundException {
		
		ColorPrompts.titles("WELCOME TO JUMPLUS MOVIES");
		
		Scanner input = new Scanner(System.in);
		int option = 0;
		
		char runApp = 'y';
		while(runApp != 'n') {
			
			MovieAppRepository.createConnection();
			MovieDAO.getMoviesFromDatabase();
			ColorPrompts.menu("\n1. Login \n2. New User \n3. Exit");
			ColorPrompts.input("Option: ");
			option = input.nextInt();
			System.out.println();
			
			switch(option) {
			
			case 1:
				ColorPrompts.titles("\nLogin\n");
				Customers login = new Customers();
				
				login = CustomerDAO.login(input);
				System.out.println();
				System.out.println();
				
				while(login!=null) {
					
					System.out.println();
					int selection = 0;
					
					ColorPrompts.menu("\n1. Add Movies \n2. Remove Movies \n3. Currently Watching \n4. Rate Movie from List \n5. Rating of Movies by Customers \n6. Show All Movies \n7. Logout");
					ColorPrompts.input("Option Selected: ");
					selection = input.nextInt();
		
					switch(selection) {
					
					case 1:
						ColorPrompts.titles("\nAdd Movies to User List");
						CustomerDAO.addMovies(input, login);
						break;
						
					case 2:
						ColorPrompts.titles("\nRemove Movies\n");
						CustomerDAO.removeMovies(input, login);
						break;
						
					case 3:
						ColorPrompts.titles("\nCurrently Watching\n");
						for(Movies m: login.getCustomersList()) {
							
							ColorPrompts.greenPrompt(m.getMovieID() + " " + m.getMovieName());
						}
						break;
					
					case 4:
						ColorPrompts.titles("\nRate Movie from List\n");
						CustomerDAO.editCustomersRating(input, login);
						System.out.println();
						break;
						
					case 5:
						ColorPrompts.titles("\nRating of movies by other customers\n");
						for(Movies m: MovieDAO.movieContainer) {
							
							System.out.println(m.getMovieID() + ", Title: " + m.getMovieName() + ", Duration: " + m.getMovieLength() + " || Rated " +m.getRatingID());
						}
						
						CustomerDAO.showMovieListWithRating(input);
						break;
						
					case 6:
						ColorPrompts.titles("\nShow all Movies\n");
						for(Movies m: MovieDAO.movieContainer) {
							
							ColorPrompts.greenPrompt(m.getMovieID() + ", Title: " + m.getMovieName() + ", Duration: " + m.getMovieLength() + " || Rated " +m.getRatingID());
						}
						break;
						
					case 7:
						ColorPrompts.titles("\nLogging out...\n");
						login = null;
						break;
						
					default:
						ColorPrompts.errorMessage("\nINVALID... Please enter the correct selection\n");
						break;
					}
				}
				
				break;
			
			case 2:
				ColorPrompts.titles("\nJUMPlus Movie App Registration From\n");
				CustomerDAO.register(input);
				break;
				
			case 3:
				ColorPrompts.titles("\nExit App...\n");
				runApp = 'n';
				input.close();
				MovieAppRepository.closeConnections();
				break;
				
			default:
				ColorPrompts.errorMessage("\nIncorrect input\n");
				break;
			}
		}
	}
}
