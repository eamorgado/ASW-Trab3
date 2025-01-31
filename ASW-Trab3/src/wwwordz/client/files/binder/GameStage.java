package wwwordz.client.files.binder;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import wwwordz.client.GameMechanics;
import wwwordz.client.Services;
import wwwordz.client.files.binder.table.TableLayout;
import wwwordz.shared.Configs;
import wwwordz.shared.Puzzle;
import wwwordz.shared.Table;
import wwwordz.shared.WWWordzException;


/**
 * This class handles the game stage, on start it will load/init all the panels
 * 
 * 	Performs RPC request to retrieve the puzzle which will then pass to 
 * 		TableLayout to generate the interactible table
 * 
 * @author Eduardo Morgado (up201706894)
 * @author �ngelo Gomes (up201703990)
 * @since May 2020
 */
public class GameStage extends Composite {

	private static GameStageUiBinder uiBinder = GWT.create(GameStageUiBinder.class);

	@UiTemplate("wwwordz.client.files.xml.GameStage.ui.xml")
	interface GameStageUiBinder extends UiBinder<Widget, GameStage> {
	}
	
	@UiField (provided=true)HorizontalPanel sequence;
	@UiField (provided=true)VerticalPanel words;
	@UiField (provided=true)VerticalPanel panel;
	@UiField (provided=true)VerticalPanel messages;
	
	
	public GameStage() {
		RootPanel.get("deck_panel").remove(0);
		initPanels();
		addReport();
		try {
			getPuzzle();
		} catch (WWWordzException e) {
			e.printStackTrace();
		}
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	
	private void initPanels() {
		addSequencePanel();
		addSumbittedWords();
		addSubmitErrors();
		addTablePanel();
	}
	
	/**
	 * This method initiates the hz panel to display current selected sequence of letters
	 */
	private void addSequencePanel() {
		sequence = new HorizontalPanel();
		sequence.getElement().setId("sequence");
	}
	
	/**
	 * Initiate vp to display all submited words (error: first does not show)
	 */
	private void addSumbittedWords() {
		words = new VerticalPanel();
		words.getElement().setId("words");
	}
	
	/**
	 * Displays all submission errors (either solution does not exist or was already given)
	 */
	private void addSubmitErrors() {
		messages = new VerticalPanel();
		messages.getElement().setId("messages");
	}
	
	private void addTablePanel() {
		panel = new VerticalPanel();
		panel.getElement().setId("game_panel");
	}
		
	/**
	 * Performs RPC request to get the puzzle
	 * @throws WWWordzException
	 */
	private void getPuzzle() throws WWWordzException {
		Services.getService().getPuzzle(new AsyncCallback<Puzzle>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("ERROR:\n"+caught.getMessage());
			}
			
			@Override
			public void onSuccess(Puzzle puzzle) {
				boolean status = GameMechanics.getInstance().addPuzzle(puzzle);
				if(status) buildPuzzle();
				else {
					Window.alert("ERROR:\nError building puzzle");
				}
			}
		});
	}
	
	/**
	 * After getting the puzzle this method calls the interactive build
	 */
	private void buildPuzzle() {
		Table game_table = GameMechanics.getInstance().getTable();
		panel.add(new TableLayout(game_table));
	}
	
	/**
	 * This method handles the report functionality and timming
	 */
	private void addReport() {
		Timer t = new Timer() {
			@Override
			public void run() {
				//Stop game
				RootPanel.get("deck_panel").remove(0);
				try {
					report();
				} catch (WWWordzException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}					
		};
		t.schedule((int)Configs.PLAY_STAGE_DURATION);
	}
	
	/**
	 * This method performs the report action sending all the user points for this session
	 * @throws WWWordzException
	 */
	private void report() throws WWWordzException {
		String nick = Services.getNick();
		int points = GameMechanics.getInstance().getTotalPoints();
		Services.getService().setPoints(nick,points, new ReportCallBack(new Date()));
	}
	
	/**
	 * Handler class for report request, on success it will launch ranking stage
	 * 
	 * @author Eduardo Morgado (up201706894)
	 * @author �ngelo Gomes (up201703990)
	 * @since May 2020
	 */
	class ReportCallBack implements AsyncCallback<Void>{
		private Date start_callback;
		
		ReportCallBack(Date st){
			start_callback = st;
		}
		
		@Override
		public void onFailure(Throwable caught) {
			Window.alert("ERROR:\n"+caught.toString());				
		}

		@Override
		public void onSuccess(Void result) {
			Date now = new Date();
			long duration = now.getTime() - start_callback.getTime();
			duration = Configs.REPORT_STAGE_DURATION - duration;
			Timer t = new Timer() {
				@Override
				public void run() {
					//Move to rank
					RootPanel.get("deck_panel").add(new RankingStage());
				}
			};
			t.schedule((int)duration);
		}
	}
}
