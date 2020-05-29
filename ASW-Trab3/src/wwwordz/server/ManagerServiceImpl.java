package wwwordz.server;

import java.util.List;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;


import wwwordz.client.ManagerService;
import wwwordz.game.Manager;
import wwwordz.game.Round;
import wwwordz.shared.Configs;
import wwwordz.shared.Puzzle;
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
	
	
	@Override
	public Puzzle getPuzzle() throws WWWordzException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Rank> getRanking() throws WWWordzException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long register(String nick, String password) throws WWWordzException {
		return Manager.getInstance().register(nick,password);
	}

	@Override
	public void setPoints(String nick, int points) throws WWWordzException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public long timeToNextPlay() {
		// TODO Auto-generated method stub
		return 0;
	}

}
