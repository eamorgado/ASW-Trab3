package wwwordz.client.files.binder;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.Widget;

import wwwordz.client.GameMechanics;
import wwwordz.client.Services;
import wwwordz.shared.Puzzle;
import wwwordz.shared.Table;
import wwwordz.shared.WWWordzException;

public class GameStage extends Composite {

	private static GameStageUiBinder uiBinder = GWT.create(GameStageUiBinder.class);

	@UiTemplate("wwwordz.client.files.xml.GameStage.ui.xml")
	interface GameStageUiBinder extends UiBinder<Widget, GameStage> {
	}
	
	public GameStage(final DeckPanel deck) {
		//get puzzle
		try {
			//build puzzle ui
			//add functionality
			getPuzzle(deck);
			
		} catch (WWWordzException e) {
			e.printStackTrace();
		}
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	private void getPuzzle(final DeckPanel deck) throws WWWordzException {
		Services.getService().getPuzzle(new AsyncCallback<Puzzle>() {
			public void onFailure(Throwable caught) {
				Window.alert("ERROR:\n"+caught.getMessage());
				deck.showWidget(0);
			}

			public void onSuccess(Puzzle puzzle) {
				boolean status = GameMechanics.getInstance().addPuzzle(puzzle);
				if(status) buildPuzzle(deck);
				else {
					Window.alert("ERROR:\nError building puzzle");
					deck.showWidget(0);
				}
			}
		});
	}
	
	private void buildPuzzle(final DeckPanel deck) {
		Table game_table = GameMechanics.getInstance().getTable();
	}

}
