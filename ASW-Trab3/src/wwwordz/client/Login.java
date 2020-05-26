package wwwordz.client;

import java.util.Iterator;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DeckLayoutPanel;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class Login extends DecoratorPanel{
	private DecoratorPanel panel;
	Login(){
		genPanel();
	    
	    /*Button log = new Button("Login");
	    log.addClickHandler(new ClickHandler() {
	    	public void onClick(ClickEvent event) {
	    		deck.showWidget(1);
	    	}
	    });
	    
	    layout.setWidget(3,1,log);*/
	}
	
	public DecoratorPanel getPanel() {return panel;}
	
	private void genPanel() {
		panel = new DecoratorPanel();
		FlexTable layout = new FlexTable();
	    layout.setCellSpacing(6);
	    FlexCellFormatter cellFormatter = layout.getFlexCellFormatter();

	    // Add a title to the form
	    layout.setHTML(0, 0, "Fill to login");
	    cellFormatter.setColSpan(0, 0, 2);
	    cellFormatter.setHorizontalAlignment(
	        0, 0, HasHorizontalAlignment.ALIGN_CENTER);

	    // Add some standard form options
	    layout.setHTML(1, 0, "Nick:");
	    layout.setWidget(1, 1, new TextBox());
	    layout.setHTML(2, 0, "Password:");
	    layout.setWidget(2, 1, new PasswordTextBox());
	    panel.add(layout);
	}
}
