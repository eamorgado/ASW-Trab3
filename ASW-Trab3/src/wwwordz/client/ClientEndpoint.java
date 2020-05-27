package wwwordz.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.RootPanel;

import wwwordz.client.deckpanels.Join;
import wwwordz.client.deckpanels.Login;


public class ClientEndpoint  implements EntryPoint{
	@Override
	public void onModuleLoad() {
		DeckPanel deck = new DeckPanel();
		addPanelsDeck(deck);
		RootPanel.get("login").add(deck);
	}
	
	private void addPanelsDeck(DeckPanel deck) {
		Login login = new Login(deck);
		Join join = new Join(deck);
		deck.add(login);
		deck.add(join);
		deck.showWidget(0);
	}

}
