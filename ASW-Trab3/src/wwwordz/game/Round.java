package wwwordz.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wwwordz.puzzle.Generator;
import wwwordz.shared.Configs;
import wwwordz.shared.Puzzle;
import wwwordz.shared.Rank;
import wwwordz.shared.WWWordzException;

/**
 * A round has 4 sequential stages
 *	join - client join the round
 *	play - client retrieves puzzle and player solve puzzle
 *	report - clients report points back to server
 *	ranking - clients ask for rankings
 *	
 *	Each stage has a specific duration and the round method can only be 
 *   executed within a limited time frame. 
 *  
 *  The duration of each stage can be checked or changed with static 
 *   setters and getters. 
 *   
 * The following method should be executed in the associated stages.
 *	register() - join
 *	getPuzzle() - play
 *	setPoints() - register
 *	getRanking() - ranking
 *	When executed outside their stages these methods raise a WWWordzException.
 * 
 * @author Eduardo Morgado (up201706894)
 * @author Ângelo Gomes (up201703990)
 * @since April 2020
 */
public class Round {
	private static final Generator generator = new Generator();
	private static final Players players = Players.getInstance();
	private static List<Rank> ranks = new ArrayList<>();
	private Date end, join, play, report,ranking;
	private final Puzzle puzzle = generator.generate();
	private Map<String,Player> roundPlayers = new HashMap<>();
	
	/**
	 * Enum storing stage durations
	 */
	private enum Durations{
		JOIN(Configs.JOIN_STAGE_DURATION),
		PLAY(Configs.PLAY_STAGE_DURATION),
		REPORT(Configs.REPORT_STAGE_DURATION),
		RANKING(Configs.RANKING_STAGE_DURATION);
		
		private long stageDuration;
		Durations(long duration){
			this.stageDuration = duration;
		}
		public long getStageDuration() {
			return stageDuration;
		}
		public void setStageDuration(long duration) {
			stageDuration = duration;
		}
	}
	
	static enum Relative{before, after};
	static enum Stage{join,play,report,ranking};
	
	/**
	 * Custom comparator for players
	 */
	private class PlayerComparator implements Comparator<Player>{
		@Override
		public int compare(Player p1, Player p2) {
			return p1.getPoints() > p2.getPoints() ? 1 
				 : p2.getPoints() > p1.getPoints() ? -1
				 : 0;
				
		}
		
	}
	
	Round(){
		//Initiate times sequentially
		//First join, then play, report, rank, and end
		//Each task depends on the time for task before and the duration of the task
		join = new Date();
		long time = join.getTime() + Durations.JOIN.getStageDuration();
		play = new Date(time);
		time = play.getTime() + Durations.PLAY.getStageDuration();
		report = new Date(time);
		time = report.getTime() + Durations.REPORT.getStageDuration();
		ranking = new Date(time);
		time = ranking.getTime() + Durations.RANKING.getStageDuration();
		end = new Date(time);
	}
	
	/**
	 * Get duration of join stage in milliseconds
	 * @return long time
	 */
	static long getJoinStageDuration() {
		return Durations.JOIN.getStageDuration();
	}
	
	/**
	 * Get duration of play stage in milliseconds
	 * @return time
	 */
	static long getPlayStageDuration() {
		return Durations.PLAY.getStageDuration();
	}
	
	/**
	 * Get table for this round
	 * @throws WWWordzException 
	 */
	Puzzle getPuzzle() throws WWWordzException {
		Date current = new Date();
		if(current.before(play))
			throw new WWWordzException("Action expected on " + Stage.play + " not " + Relative.before);
		else if(current.after(report))
			throw new WWWordzException("Action expected on " + Stage.play + " not " + Relative.after);
		return puzzle;
	}
	
	/**
	 * Get list of players in the round sorted by points
	 * @return List<Rank> sorted list of rankings
	 * @throws WWWordzException 
	 */
	List<Rank> getRanking() throws WWWordzException{
		Date current = new Date();
		if(current.before(ranking))
			throw new WWWordzException("Action expected on " + Stage.ranking + " not " + Relative.before);
		ArrayList<Player> sorted = new ArrayList<>(roundPlayers.values());
		Collections.sort(sorted,new PlayerComparator());
		if(!ranks.isEmpty()) return ranks;
		for(Player p : sorted) {
			Rank rnk = new Rank(p.getNick(),p.getPoints(),p.getAccumulated());
			ranks.add(rnk);
		}
		return ranks;		
	}
	
	/**
	 * Duration of ranking stage in milliseconds
	 */
	static long getRankingStageDuration() {
		return Durations.RANKING.getStageDuration();
	}
	
	/**
	 * Get duration of report stage in milliseconds
	 */
	static long getReportStageDuration() {
		return Durations.REPORT.getStageDuration();
	}
	
	/**
	 * Complete duration of round (all stages)
	 */
	static long getRoundDuration() {
		long roundDuration = 0L;
		for(Durations duration : Durations.values())
			roundDuration += duration.getStageDuration();
		return roundDuration;
	}
	
	/**
	 * Time in milliseconds to the next play stage
	 */
	long getTimetoNextPlay() {
		Date current = new Date();
		return current.before(play) ? play.getTime() - current.getTime()
			 : end.getTime() - current.getTime() + Durations.JOIN.getStageDuration();
	}
	
	/**
	 * Register user with nick and password for this round
	 * @param nick
	 * @param password
	 * @return
	 * @throws WWWordzException 
	 */
	long register(String nick,String password) throws WWWordzException {
		Date current = new Date();
		if(current.after(play))
			throw new WWWordzException("Action expected on " + Stage.join + " not " + Relative.after);
		else if(!players.verify(nick,password))
			throw new WWWordzException("User non existant");
		Player p = players.getPlayer(nick);
		roundPlayers.put(nick,p);
		return play.getTime() - current.getTime();		
	}
	
	/**
	 * Change join stage
	 */
	static void setJoinStageDuration(long joinStageDuration) {
		Durations.JOIN.setStageDuration(joinStageDuration);
	}
	
	/**
	 * Change play stage
	 */
	static void setPlayStageDuration(long playStageDuration) {
		Durations.PLAY.setStageDuration(playStageDuration);
	}
	
	/**
	 * Set number of points obtained by user in this round
	 * @throws WWWordzException 
	 */
	void setPoints(String nick, int points) throws WWWordzException {
		Date current = new Date();
		if(current.before(report))
			throw new WWWordzException("Action expected on " + Stage.report + " not " + Relative.before);
		else if(current.after(ranking))
			throw new WWWordzException("Action expected on " + Stage.report + " not " + Relative.after);
		else if(!roundPlayers.containsKey(nick))
			throw new WWWordzException("Player " + nick + " non existant");
		roundPlayers.get(nick).setPoints(points);
	}
	
	/**
	 * Change ranking stage
	 */
	static void setRankingStageSuration(long rankingStageDuration) {
		Durations.RANKING.setStageDuration(rankingStageDuration);
	}
	
	/**
	 * Change report stage
	 */
	static void setReportStageDuration(long reportStageDuration) {
		Durations.REPORT.setStageDuration(reportStageDuration);
	}
	
	
}