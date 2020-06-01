package wwwordz.server;

import java.util.List;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;


import wwwordz.client.ManagerService;
import wwwordz.game.Manager;
import wwwordz.game.Round;
import wwwordz.shared.Configs;
import wwwordz.shared.Puzzle;
import wwwordz.shared.Puzzle.Solution;
import wwwordz.shared.Rank;
import wwwordz.shared.WWWordzException;

/**
 * The manager service implementation
 * 
 * @author Eduardo Morgado (up201706894)
 * @author Ângelo Gomes (up201703990)
 * @since May 2020
 */
@SuppressWarnings("serial")
public class ManagerServiceImpl extends RemoteServiceServlet implements ManagerService {
	static {
		Round.setJoinStageDuration(Configs.JOIN_STAGE_DURATION);
		Round.setPlayStageDuration(Configs.PLAY_STAGE_DURATION);
		Round.setRankingStageSuration(Configs.RANKING_STAGE_DURATION);
		Round.setReportStageDuration(Configs.REPORT_STAGE_DURATION);		
	}
	
	private Manager manager = Manager.getInstance();
	
	@Override
	public Puzzle getPuzzle() throws WWWordzException {
		Puzzle p = manager.getPuzzle();
		System.out.print(p.getTable().toString());
		System.out.println("Solutions:");
		for(Solution s : p.getSolutions())
			System.out.println(s.getWord());
		return p;
	}

	@Override
	public List<Rank> getRanking() throws WWWordzException {
		return manager.getRanking();
	}

	@Override
	public long register(String nick, String password) throws WWWordzException {
		return manager.register(nick,password);
	}

	@Override
	public void setPoints(String nick, int points) throws WWWordzException {
		manager.setPoints(nick,points);
	}

	@Override
	public long timeToNextPlay() {
		return manager.timeToNextPlay();
	}

}
