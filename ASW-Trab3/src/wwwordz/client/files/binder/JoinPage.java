package wwwordz.client.files.binder;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;


public class JoinPage extends Composite {

	private static JoinPageUiBinder uiBinder = GWT.create(JoinPageUiBinder.class);

	@UiTemplate("wwwordz.client.files.xml.JoinPage.ui.xml")
	interface JoinPageUiBinder extends UiBinder<Widget, JoinPage> {
	}
	
	@UiField (provided=true)Button join_leave;
	
	public JoinPage(final DeckPanel deck) {
		setupLeave(deck);
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	private void setupLeave(final DeckPanel deck) {
		join_leave = new Button();
		join_leave.addClickHandler(new ClickHandler() {
	    	public void onClick(ClickEvent event) {
	    		RootPanel.get("user_info").clear();
	    		deck.showWidget(0);
	    	}
	    });
	}
}
