package xml.src.xml;

import java.time.LocalDate;
import java.util.*;

public class PlayerProfile {

	// @author nsiebler
	// This class creates a new Player instance with all the values
	// that are saved in the xml file later
	private String name;
	private int highscore;
    private int playedGames;
    private int wins;
    private int losses;
    private LocalDate creation;
    
    // This functionality will be implemented by this week  
    // it is difficult because it works with changing values all the time
    private double averageScore;
    private int playtime;
    private LocalDate lastLogged;
    
    // ask also what last logged in means
    
    // Icon will be added, just have to clear which format will be excepted



	// Constructor which will be used very often in the xml files
	public PlayerProfile(String name, int highscore, int playtime, int playedGames, int wins, int looses,
						 double averageScore, LocalDate creation, LocalDate lastLogged) {
		super();
		this.name = name;
		this.highscore = highscore;
		this.playtime = playtime;
		this.playedGames = playedGames;
		this.wins = wins;
		this.losses = looses;
		this.averageScore = averageScore;
		this.creation = creation;
		this.lastLogged = lastLogged;
	}

	// Method that builds a String out of a Date value
	
	String dateToString (LocalDate date) {
		return date.getDayOfMonth() +  "/" + 
				date.getMonthValue() + "/" +
				date.getYear();
	}
	
	// the following methods will just contain all necessary getters and setters
	
		public String getName() {
			return name;
		}

	public void setName(String name) {
		this.name = name;
	}

	public int getHighscore() {
		return highscore;
	}

	public void setHighscore(int highscore) {
		this.highscore = highscore;
	}

	// This method generates the playtime for every session and increases every game 
			// session that you play
	public int getPlaytime() {
		// The functionality has to be implemented and will take some time
		
		return playtime;
	}

	public void setPlaytime(int playtime) {
		this.playtime = playtime;
	}

	public int getPlayedGames() {
		return playedGames;
	}

	public void setPlayedGames(int playedGames) {
		this.playedGames = playedGames;
	}

	public int getWins() {
		return wins;
	}

	public void setWins(int wins) {
		this.wins = wins;
	}

	public int getLooses() {
		return losses;
	}

	public void setLosses(int losses) {
		this.losses = losses;
	}

	public double getAverageScore() {
		return averageScore;
	}

	public void setAverageScore(double averageScore) {
		this.averageScore = averageScore;
	}

	public LocalDate getCreation() {
		return creation;
	}

	public void setCreation(LocalDate creation) {
		this.creation = creation;
	}

	public LocalDate getLastLogged() {
		return lastLogged;
	}

	public void setLastLogged(LocalDate lastLogged) {
		this.lastLogged = lastLogged;
	}


	public String toString(){
		return "Name: "+ this.getName()+"\nHighscore: "+ this.getHighscore()+ "\nPlaytime: " +
				this.getPlaytime()+ "\nPlayed Games: "+this.getPlayedGames()+"\nWins:"+this.getWins()+
				"\nLosses: "+this.getLooses() + "\nAverage Score: "+ this.getAverageScore()+
				"\nCreationDate: " + this.getCreation() + "\nLastLogged: "+this.getLastLogged();
	}
}
