package Model;

import java.sql.Time;

public class Movies {
	
	private String movieID;
	private String movieName;
	private Time movieLength;
	private String ratingID;
	public Movies() {
		
		movieID = "n/a";
		movieName = "n/a";
		ratingID = "n/a";
	}
	public Movies(String movieID, String movieName, Time movieLength, String ratingID) {
		super();
		this.movieID = movieID;
		this.movieName = movieName;
		this.movieLength = movieLength;
		this.ratingID = ratingID;
	}
	
	public String getMovieID() {
		return movieID;
	}
	
	public void setMovieID(String movieID) {
		this.movieID = movieID;
	}
	
	public String getMovieName() {
		return movieName;
	}
	
	public void setMovieName(String movieName) {
		this.movieName = movieName;
	}
	
	public Time getMovieLength() {
		return movieLength;
	}
	
	public void setMovieLength(Time time) {
		this.movieLength = time;
	}
	
	public String getRatingID() {
		return ratingID;
	}
	
	public void setRatingID(String ratingID) {
		this.ratingID = ratingID;
	}
	
	@Override
	public String toString() {
		return "Movies movieID=" + movieID + ", movieName=" + movieName + ", movieLength=" + movieLength + ", ratingID=" + ratingID;
	}
}
