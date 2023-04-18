package MovieDAO;

import java.sql.SQLException;
import java.util.List;

import Model.Movies;
import Repository.MovieAppRepository;

//A class that focuses only on the movide container
public class MovieDAO {
	
	public static List<Movies> movieContainer;
	
	//Print all movies
	public static void getMoviesFromDatabase() throws SQLException {
		
		MovieAppRepository.movieList(); //adds the movies to the movie container
	}
	
	public static Movies findMovie(int index) {
		
		Movies movies = new Movies();
		if(movieContainer.get(index) != null) {
			
			return movies;
		} else {
			
			System.out.println("movie not found");
			return null;
		}
	}
	
	public static void removeMovie(int index) {
		
		if(movieContainer.remove(index) != null) {
			
			System.out.println("Movie Removed");
		}
	}
}
