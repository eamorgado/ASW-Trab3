package wwwordz.game;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import wwwordz.client.ManagerService;
import wwwordz.shared.Puzzle;
import wwwordz.shared.Rank;
import wwwordz.shared.WWWordzException;

/**
 * Singleton, acts as a facade for other classes in this package.
 * 
 * Methods in this class are delegated in instances of these classes
 * 
 * @author Eduardo Morgado (up201706894)
 * @author Ângelo Gomes (up201703990)
 * @since April 2020
 */
public class Manager implements ManagerService {
	private static long INITIAL_TIME = 0;
	private static Manager manager;
	Round round = null;
	private static ScheduledExecutorService worker = Executors.newScheduledThreadPool(2);
	
	private Manager() {
		round = new Round();
		worker.schedule(new Runnable() {
			@Override
			public void run() {
				if(round == null)
					round = new Round();
			}
		},
				INITIAL_TIME,
				TimeUnit.MILLISECONDS
		);
	}
	
	
	/**
	 * Single instance of Manager
	 * @return Manager instance
	 */
	public static Manager getInstance() {
		if(manager == null) manager = new Manager();
		return manager;
	}
	
	/**
	 * Get table of current round;
	 * @return
	 */
	public Puzzle getPuzzle() throws WWWordzException {
		return round.getPuzzle();
	}
	
	/**
	 * List of players in current round sorted by points
	 * @return List
	 */
	public List<Rank> getRanking() throws WWWordzException{
		return round.getRanking();
	}
	
	/**
	 * Register user with nick and password for current round
	 * @param nick
	 * @param password
	 * @return
	 */
	public long register(String nick, String password) throws WWWordzException{
		return round.register(nick,password);
	}
	
	/**
	 * Set number of points obtained by user in current round
	 * @param nick
	 * @param points
	 */
	public void setPoints(String nick, int points) throws WWWordzException{
		round.setPoints(nick,points);
	}
	
	public long timeToNextPlay() {
		return round.getTimetoNextPlay();
	}
}
