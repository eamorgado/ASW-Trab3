package wwwordz.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.DeckLayoutPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;

public class ClientEndpoint  implements EntryPoint{
	@Override
	public void onModuleLoad() {
		Panel root = RootPanel.get();
		DeckLayoutPanel deck = new DeckLayoutPanel();
		Login log = new Login();
		deck.add(log);
		root.add(deck);
	}

}
