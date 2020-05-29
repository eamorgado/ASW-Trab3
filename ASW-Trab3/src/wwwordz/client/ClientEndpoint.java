package wwwordz.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.RootPanel;

import wwwordz.client.files.binder.JoinPage;
import wwwordz.client.files.binder.LoginForm;


/**
 * The client entry point.
 * 
 * @author Eduardo Morgado (up201706894)
 * @author Ângelo Gomes (up201703990)
 * @since May 2020
 */
public class ClientEndpoint  implements EntryPoint{
	
	@Override
	public void onModuleLoad() {
		DeckPanel deck = new DeckPanel();
		addPanelsDeck(deck);
		RootPanel.get("deck_panel").add(deck);
	}
	
	/**
	 * This method builds all the deck panels used for the app, at startup enables the login panel
	 * @param deck
	 */
	private void addPanelsDeck(DeckPanel deck) {
		deck.add(new LoginForm(deck));
		deck.add(new JoinPage(deck));
		deck.showWidget(0);
	}
}
