package CustomerDAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ColorDesign.ColorPrompts;
import Model.Customers;
import Model.Movies;
import MovieDAO.MovieDAO;
import Repository.MovieAppRepository;

//Business Logic. This class contains the business logic of the MovieApp
public class CustomerDAO {
	
	public static Customers login(Scanner input) throws SQLException {
		String email, password;
		System.out.println("\nPlease provide email and password");
		ColorPrompts.input("Email: ");
		email = input.next();
		ColorPrompts.input("Password: ");
		password = input.next();
		
		String query = "select * from customers where email = ? and password = ?";
		
		Customers login;
		login = MovieAppRepository.login(query, email, password);
		
		if(login != null) {
			
			query = "select ma.movieID, m.movieName, m.movieLength, m.ratingID from movieapp ma, movie m where ma.movieID = m.movieID and ma.email = ?";
			MovieAppRepository.getUserMovieList(query, login);
			return login;
		} else {
			
			ColorPrompts.errorMessage("\nUser not found\n");
			return null;
		}
	}
	
	public static void register(Scanner input) throws SQLException {
		
		String emailPattern = "([a-zA-Z0-9]+)([@?])([a-zA-Z-0-9]+)([.])(com+?)";
		Pattern pattern = Pattern.compile(emailPattern);
		
		ColorPrompts.titles("\nMOVIE APP REGISTRATION\n");
		System.out.println("Please enter information below");
		
		ColorPrompts.input("Enter Email: ");
		String email = input.next(); //check if there's an '@' or a '.com' on the string.
		
		while (MovieAppRepository.checkEmail(email) != false) {
			
			ColorPrompts.errorMessage("\nEmail exists. Please enter a different email: ");
			email = input.next();
		}
		Matcher matcher = pattern.matcher(email);
		
		while (matcher.find() != true) {
			
			ColorPrompts.errorMessage("\nInvalid email. Please enter a valid email address: ");
			email = input.next(); //check if there's an '@' or a '.com' on the string.
			matcher = pattern.matcher(email);
		}
		
		ColorPrompts.input("Enter Username: ");
		String username = input.next(); //no shorter than 6
		
		ColorPrompts.input("Password: "); //try to use one capital and lower case letters, a digit, and a special character
		String password = input.next();
		
		ColorPrompts.input("First Name: ");
		String firstName = input.next();
		
		ColorPrompts.input("Last Name: ");
		String lastName = input.next();
		
		Customers customers = new Customers(email, username, password, firstName, lastName);
		
		String query = "insert into customers values(?, ?, ? ,?, ?)";
		MovieAppRepository.saveNewCustomers(query, customers);
	}
	
	public static void showAllMovies() throws SQLException {
		
		MovieDAO.getMoviesFromDatabase();
	}
	
	public static void addMovies(Scanner input, Customers customers) throws SQLException {
		
		ColorPrompts.greenPrompt("\nPrinting all available movies\n");
		
		for(int i = 0; i < MovieDAO.movieContainer.size(); i++) {
			
			ColorPrompts.greenPrompt(MovieDAO.movieContainer.get(i).getMovieID() + ", Title: " + MovieDAO.movieContainer.get(i).getMovieName() + ", Duration: " + MovieDAO.movieContainer.get(i).getMovieLength() + " || Rated " + MovieDAO.movieContainer.get(i).getRatingID());
		}
		
		boolean add = true;
		char reply = 'y';
		
		while (add == true) {
			
			ColorPrompts.input("\nSelect the movie to add to your list: ");
			String mov = input.next().toUpperCase();
			
			while (mov.length() > 5 || mov.length() < 1 || mov.length() < 5) {
				
				ColorPrompts.errorMessage("\nUnvalid code...");
				ColorPrompts.input("\nSelect the movie to add to your list: ");
				mov = input.next().toUpperCase();
			}
			
			for(Movies m: MovieDAO.movieContainer) {
				
				if (m.getMovieID().equals(mov)) {
					
					customers.insertMoviesToPersonalList(m);
				}
			}
			
			ColorPrompts.input("\nAdd more movies?: ");
			reply = input.next().charAt(0);
				
			if(reply == 'n') {
				
				add = false;
				break;
			} else if (reply == 'y') {
				
			} else {
				
				ColorPrompts.errorMessage("\nInvalid Selection...\n");
			}
			
		}
		
		String query = "insert into movieapp values(?, ?, ?, ?, ?)";
		MovieAppRepository.addMovieInUserList(query, customers);
		
		ColorPrompts.greenPrompt("\nAll movies selected were added into your list\n");
	}
	
	public static void removeMovies(Scanner input, Customers customers) throws SQLException {
		
		List <Movies> movieList = new ArrayList<>();
		movieList = customers.getCustomersList();
		
		for(int i = 0; i < movieList.size(); i++) {
			
			ColorPrompts.greenPrompt(movieList.get(i).getMovieID() + " " + movieList.get(i).getMovieName());
		}
		
		char removeMovie = 'y';
		while(removeMovie != 'n') {
			
			ColorPrompts.input("\nEnter the movie's id to be remove from the list: ");
			String select = input.next().toUpperCase();
			
			for(int i = 0; i < movieList.size(); i++) {
				
				if(movieList.get(i).getMovieID().equals(select)) {
					
					String query = "delete from movieapp where email = ? and movieID = ?";
					MovieAppRepository.removeMovieInUserList(query, customers, movieList.get(i));
					if(customers.getCustomersList().get(i).getMovieID().equals(select)) {
						
						customers.getCustomersList().remove(i);
					}
				}
			}
			
			ColorPrompts.input("\nRemove more movies ('y' or 'n')?: ");
			removeMovie = input.next().charAt(0);
		}
	}
	
	public static void showMovieListWithRating(Scanner input) throws SQLException {
		
		ColorPrompts.titles("\nWhat they say about a movie\n");
		ColorPrompts.input("Select movie: ");
		String movieID = input.next();
		String query = "select distinct ma.movieID, m.movieName, m.movieLength, avg(ma.userRating) as userRating from movie m, movieapp ma where m.movieID = ma.movieID and ma.movieID = ? group by ma.movieID";
		
		MovieAppRepository.getMovieRatingFromApp(query, movieID);
	}
	
	public static void editCustomersRating(Scanner input, Customers customers) throws SQLException {
		
		for (Movies m: customers.getCustomersList()) {
			
			ColorPrompts.greenPrompt(m.getMovieID() + " " + m.getMovieName());
		}
		
		int customerRating = 0;
		String comments = "";
		String query = "update movieapp set userRating = ? where email = ? and movieID = ?";
		ColorPrompts.input("\nPlease Enter Movie ID to be rated\n");
		String mID = input.next().toUpperCase();
		
		for(Movies m: customers.getCustomersList()) {
			
			if(m.getMovieID().equals(mID)) {

				ColorPrompts.input("\n" + m.getMovieID() + ": Movie Name: " + m.getMovieName() + "\nRate this movie (1 (very poor) - 5 (excellent)): ");
				customerRating = input.nextInt();
				MovieAppRepository.editMovieRatingFromUser(query, customerRating, customers, m);
				
				ColorPrompts.input("\nAny Comments: ");
				comments = input.nextLine();
				input.nextLine();
				
				query = "update movieapp set comments = ? where email = ? and movieID = ?";
				MovieAppRepository.editMovieCommentsFromUser(query, comments, customers, m);
			}
		}
	}
}
