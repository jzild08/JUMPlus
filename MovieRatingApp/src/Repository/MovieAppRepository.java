package Repository;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import ColorDesign.ColorPrompts;
import Model.Customers;
import Model.Movies;
import MovieDAO.MovieDAO;

public class MovieAppRepository {
	private static Connection conn;
	private static PreparedStatement preparedStatement;
	private static ResultSet resultSet;
	
	public MovieAppRepository() {
		
		conn = null;
		preparedStatement = null;
		resultSet = null;
	}
	
	public static void createConnection() throws FileNotFoundException, IOException, SQLException, ClassNotFoundException {
		
		conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/movie", "root", "!smalZep0p0");
	}
	
	public static void closeConnections() throws SQLException {
		
		preparedStatement.close();
		resultSet.close();
		preparedStatement.close();
	}
	
//	<------------------------------------------CUSTOMERS---------------------------------------------------------------------->
	public static Customers login(String query, String email, String password) throws SQLException {
		
		Customers customers = new Customers();
		customers.setCustomersList(new ArrayList<Movies>());
		preparedStatement =conn.prepareStatement(query);
		preparedStatement.setString(1, email);
		preparedStatement.setString(2,  password);
		resultSet = preparedStatement.executeQuery();
		
		while(resultSet.next()) {
			
			customers.setEmail(resultSet.getString("email"));
			customers.setPassword(resultSet.getString("password"));
			customers.setFirstName(resultSet.getString("firstName"));
			customers.setLastName(resultSet.getString("lastName"));
			customers.setUsername(resultSet.getString("username"));
			return customers;
		}
		
		customers = null;
		return customers;
	}
	
	public static void saveNewCustomers(String query, Customers customers) throws SQLException {
		
		preparedStatement = conn.prepareStatement(query);
		preparedStatement.setString(1, customers.getEmail());
		preparedStatement.setString(2, customers.getPassword());
		preparedStatement.setString(3, customers.getFirstName());
		preparedStatement.setString(4, customers.getLastName());
		preparedStatement.setString(5, customers.getUsername());
		
		int row = preparedStatement.executeUpdate();
		
		if(row > 0) {
			
			ColorPrompts.greenPrompt("\nRegistration successful\n");
		}
	}
	
	public static boolean checkEmail(String doesEmailExist) throws SQLException {
		
		boolean found = false;
		preparedStatement = conn.prepareStatement("select email from customers where email = ?");
		preparedStatement.setString(1, doesEmailExist);
		resultSet = preparedStatement.executeQuery();
		
		while(resultSet.next()) {
			
			if(resultSet.getString("email").equals(doesEmailExist)) {
				
				found = true;
				return found;
			}
		}
		
		return found;
	}
	
	public static boolean checkMovieInUsersList(String mID, String email) throws SQLException {
		
		boolean found = false;
		preparedStatement = conn.prepareStatement("select movieID, email from movieapp where movieID = ? and email = ?");
		preparedStatement.setString(1, mID);
		preparedStatement.setString(2, email);
		resultSet = preparedStatement.executeQuery();
		
		while (resultSet.next()) {
			
			if(resultSet.getString("movieID").equals(mID) && resultSet.getString("email").equals(email)) {
				
				return true;
			}
		}
		
		return found;
	}
//	<------------------------------------------MOVIES------------------------------------------------------------------------->
	
	public static void movieList() throws SQLException {
		
		MovieDAO.movieContainer = new ArrayList<Movies>();
		preparedStatement = conn.prepareStatement("select * from movie");
		resultSet = preparedStatement.executeQuery();
		while (resultSet.next()) {
			
			Movies movies = new Movies();
			movies.setMovieID(resultSet.getString("movieID"));
			movies.setMovieName(resultSet.getString("movieName"));
			movies.setMovieLength(resultSet.getTime("movieLength"));
			movies.setRatingID(resultSet.getString("ratingID"));
			MovieDAO.movieContainer.add(movies);
		}
	}
	
	public static void addMovieInUserList(String query, Customers customers) throws SQLException {
		
		for (Movies m: customers.getCustomersList()) {
			
			boolean found = checkMovieInUsersList(m.getMovieID(), customers.getEmail());
			System.out.println("\n" + found);
			
			if(found == false) {
				
				preparedStatement = conn.prepareStatement(query);
				preparedStatement.setString(1, customers.getEmail());
				preparedStatement.setString(2, m.getMovieID());
				preparedStatement.setInt(3, 0);
				preparedStatement.setString(4, "");
				preparedStatement.setDate(5, Date.valueOf(LocalDate.now()));
				preparedStatement.executeUpdate();
			} else {
				
				ColorPrompts.greenPrompt(m.getMovieID() + " is already in your movie list");
			}
		}
	}
	
	public static void removeMovieInUserList(String query, Customers customers, Movies movies) throws SQLException {
		
		preparedStatement = conn.prepareStatement(query);
		preparedStatement.setString(1, customers.getEmail());
		preparedStatement.setString(2, movies.getMovieID());
		int row = preparedStatement.executeUpdate();
		
		if(row != 0) {
			
			ColorPrompts.errorMessage("\nTHE SELECTED MOVIE WAS REMOVE FROM THE LIST\n");
		} else {
			
			ColorPrompts.errorMessage("\nLIST IS EMPTY\n");
		}
	}
	
	public static void getUserMovieList(String query, Customers customers) throws SQLException{
		
		preparedStatement = conn.prepareStatement(query);
		preparedStatement.setString(1, customers.getEmail());
		resultSet = preparedStatement.executeQuery();
		customers.setCustomersList(new ArrayList<Movies>());
		
		while(resultSet.next()) {
			
			Movies movies = new Movies();
			movies.setMovieID(resultSet.getString("ma.movieID"));
			movies.setMovieName(resultSet.getString("m.movieName"));
			movies.setMovieLength(resultSet.getTime("m.movieLength"));
			movies.setRatingID(resultSet.getString("m.ratingID"));
			customers.insertMoviesToPersonalList(movies);
		}
		
		for(Movies m: customers.getCustomersList()) {
			
			ColorPrompts.greenPrompt(m.getMovieID() + " " + m.getMovieName());
		}
	}
	
	public static void getMovieRatingFromApp(String query, String movieID) throws SQLException {
		
		preparedStatement = conn.prepareStatement(query);
		preparedStatement.setString(1, movieID);
		resultSet = preparedStatement.executeQuery();
		
		while(resultSet.next()) {
			
			ColorPrompts.greenPrompt(resultSet.getString("ma.movieID") + " - " + resultSet.getString("m.movieName") + ", Duration: " + resultSet.getTime("m.movieLength") + ", Rating: " + resultSet.getDouble("userRating"));
			
		}
	}
	
	public static void editMovieRatingFromUser(String query, int newRating, Customers customers,  Movies movies) throws SQLException {
		
		preparedStatement = conn.prepareStatement(query);
		preparedStatement.setInt(1, newRating);
		preparedStatement.setString(2, customers.getEmail());
		preparedStatement.setString(3, movies.getMovieID());
		int row = preparedStatement.executeUpdate();
		
		if(row != 0) {
			
			ColorPrompts.greenPrompt("\nThe rating was updated\n");
		}
	}
	
	public static void editMovieCommentsFromUser(String query, String newComments, Customers customers, Movies movies) throws SQLException {
		
		preparedStatement = conn.prepareStatement(query);
		preparedStatement.setString(1, newComments);
		preparedStatement.setString(2, customers.getEmail());
		preparedStatement.setString(3, movies.getMovieID());
		int row = preparedStatement.executeUpdate();
		
		if(row != 0) {
			
			ColorPrompts.greenPrompt("\nThe comments were updated\n");
		}
	}
}
