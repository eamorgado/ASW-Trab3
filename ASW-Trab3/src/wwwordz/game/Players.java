package wwwordz.game;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import wwwordz.shared.WWWordzException;

/**
 * Persistent collection of players indexed by nick (map)
 * Each player has nick, password points and accumulated points
 * 
 * Data is persisted using serialization and backup each time a new user is 
 * 	created or points are updated
 * 
 * This class is a singleton
 * @author Eduardo Morgado (up201706894)
 * @author Ângelo Gomes (up201703990)
 * @since April 2020
 */
public class Players implements Serializable{
	private static final long serialVersionUID = 1L; 
	private static Players instance = new Players();
	private HashMap<String,Player> players;
	private static File home, file;
	
	private Players() {
		players = new HashMap<>();
		//get cwd
		home = new File(System.getProperty("user.dir"));
		//create serializable file (ser extension)
		file = new File(home,"players.ser");
	}
	
	/**
	 * Method to save/persist data
	 */
	private static void backup(){
		try (
			FileOutputStream stream = new FileOutputStream(file);
			ObjectOutputStream serializer = new ObjectOutputStream(stream);
			){
			serializer.writeObject(instance);
		}catch(IOException cause) {
			cause.printStackTrace();
		}
	}
	
	/**
	 * Method to return PLayers either by
	 * 	reading file by serialization
	 *  generating new instance	
	 * @return
	 */
	private static Players restore(){
		Players players = null;
		if(file.canRead()) {
			try(
				FileInputStream stream = new FileInputStream(file);
				ObjectInputStream deserializer = new ObjectInputStream(stream);
				){
				players = (Players) deserializer.readObject();
			}catch(IOException | ClassNotFoundException cause) {
				cause.printStackTrace();
			}
		}
		else {
			players = new Players();
		}
		return players;
	}
	
	/**
	 * Add points to player
	 * 
	 * throws exception if player is unknown, if not exists in map
	 * @param nick
	 * @param points
	 */
	void addPoints(String nick, int points) throws WWWordzException{
		instance = restore();
		if(!instance.players.containsKey(nick))
			throw new WWWordzException("Player non existant");
		instance.players.get(nick).setPoints(points);	
		//Data updated => persist
		backup();
	}
	
	/**
	 * Clears all players stored in this instance
	 */
	void cleanup() {
		instance = restore();
		Collection<String> nicks = new ArrayList<>(instance.players.keySet());
		for(String nick: nicks)
			instance.players.remove(nick);
		backup();
	}
	
	static File getHome() {
		return home;
	}
	
	/**
	 * Get single instance of this class
	 * @return
	 */
	static Players getInstance() {
		if(instance == null) {
			if(restore() == null) instance = new Players();
			else instance = restore();
		}
		return instance;
	}
	
	/**
	 * Get player of respective nick
	 * @param nick
	 * @return Player
	 */
	Player getPlayer(String nick) {
		instance = restore();
		return instance.players.get(nick);
	}
	
	/**
	 * Resets points of current round while keeping accumulated points
	 * Data update
	 * @param nick
	 */
	void resetPoints(String nick) throws WWWordzException{
		addPoints(nick,0);
	}
	
	static void setHome(File home) {
		Players.home = home;
		Players.file = new File(home,"players.ser");
	}
	
	boolean verify(String nick, String password) {
		instance = restore();
		if(instance.players.containsKey(nick))
			return instance.players.get(nick).getPassword().equals(password);
		instance.players.put(nick,new Player(nick,password));
		backup();
		return true;
	}
}
