package wwwordz.game;

import java.io.Serializable;

/**
 * A player of WWWordz, including authentication data(nick pass),
 * 	current round and accumulated points
 * 
 * @author Eduardo Morgado (up201706894)
 * @author Ângelo Gomes (up201703990)
 * @since April 2020
 */
public class Player implements Serializable{
	private static final long serialVersionUID = 1L;
	private int accumulated;
	private String nick;
	private String password;
	private int points;
	
	Player(String nick,String password){
		this.nick = nick;
		this.password = password;
		accumulated = points = 0;
	}
	
	/**
	 * Getter for nick
	 * @return string nick
	 */
	String getNick() {
		return nick;
	}
	
	/**
	 * Getter for password
	 * @return String password
	 */
	String getPassword() {
		return password;
	}
	
	/**
	 * Getter for points
	 * @return int points
	 */
	int getPoints() {
		return points;
	}
	
	/**
	 * Getter for accumulated points
	 * @return int accumulated
	 */
	int getAccumulated() {
		return accumulated;
	}
	
	/**
	 * Setter for nick
	 * @param nick
	 */
	void setNick(String nick) {
		this.nick = nick;
	}
	
	/**
	 * Setter for password
	 * @param password
	 */
	void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * Setter for accumulated
	 * @param accumulated
	 */
	void setAccumulated(int accumulated) {
		this.accumulated = accumulated;
	}
	
	/**
	 * Setter for points
	 * When updating points accumulate new points
	 * @param points
	 */
	void setPoints(int points) {
		this.accumulated += points;
		this.points = points;
	}
}
