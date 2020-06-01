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
	
	private void addSequencePanel() {
		sequence = new HorizontalPanel();
		sequence.getElement().setId("sequence");
	}
	
	private void addSumbittedWords() {
		words = new VerticalPanel();
		words.getElement().setId("words");
	}
	
	private void addSubmitErrors() {
		messages = new VerticalPanel();
		messages.getElement().setId("messages");
	}
	
	private void addTablePanel() {
		panel = new VerticalPanel();
		panel.getElement().setId("game_panel");
	}
		
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
	
	private void buildPuzzle() {
		Table game_table = GameMechanics.getInstance().getTable();
		panel.add(new TableLayout(game_table));
	}
	
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
	
	private void report() throws WWWordzException {
		String nick = Services.getNick();
		int points = GameMechanics.getInstance().getTotalPoints();
		Services.getService().setPoints(nick,points, new ReportCallBack(new Date()));
	}
	
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
