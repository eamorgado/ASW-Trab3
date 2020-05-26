package wwwordz.shared;

import java.io.Serializable;

/**
 * Class representing a row in the ranking table.
 * 
 * All the player data except player's password.
 * 
 * @author Eduardo Morgado (up201706894)
 * @author Ângelo Gomes (up201703990)
 * @since April 2020
 **/
public class Rank implements Serializable{
	private static final long serialVersionUID = 1L;
	private int points;
	private String nick;
	private int accumulated;
	
	/**
	 * Empty constructor
	 * @param void
	 */
	public Rank(){}
	
	/** 
	 * Constructor with all fields initialized
	 * @param String nick - nickname of player
	 * @param int points - points of player
	 * @param int accumulated - accumulated points of player
	 */
	public Rank(String nick, int points, int accumulated){
		this.nick = nick;
		this.points = points;
		this.accumulated = accumulated;
	}
	
	/**
	 * Method that returns the current nick in this rank
	 * @param void
	 * @return String
	 */
	public String getNick(){return nick;}
	
	/**
	 * Method that returns the current points in this rank
	 * @param void
	 * @return int
	 */
	public int getPoints(){return points;}
	
	/**
	 * Method that returns current accumulated points in rank
	 * @param void
	 * @return int
	 */
	public int getAccumulated(){return accumulated;}
	
	
	/**
	 * Method that changes the nick in this rank
	 * @param String nick - new nick
	 * @return void
	 */
	public void setNick(String nick) {this.nick = nick;}
	
	/**
	 * Method that changes the points for this rank
	 * @param int points - new points
	 * @return void
	 */
	public void setPoints(int points) {this.points = points;}
	
	/**
	 * Method that changes the accumulated points for this rank
	 * @param int accumulated - the new accumulated points
	 * @return void
	 */
	public void setAccumulated(int accumulated) {this.accumulated = accumulated;}	
}
