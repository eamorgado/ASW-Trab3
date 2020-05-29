package wwwordz.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import wwwordz.shared.Puzzle;
import wwwordz.shared.Rank;
import wwwordz.shared.WWWordzException;

/**
 * The async counterpart of <code>ManagerService</code>.
 * 
 * @author Eduardo Morgado (up201706894)
 * @author Ângelo Gomes (up201703990)
 * @since May 2020
 */
@RemoteServiceRelativePath("game")
public interface ManagerServiceAsync {
	void getPuzzle(AsyncCallback<Puzzle> callback) throws WWWordzException;
	void getRanking(AsyncCallback<List<Rank>> callback) throws WWWordzException;
	void register(String nick, String password,AsyncCallback<Long> callback) throws WWWordzException;
	void setPoints(String nick, int points,AsyncCallback<Void> callback) throws WWWordzException;
	void timeToNextPlay(AsyncCallback<Long> callback);
}