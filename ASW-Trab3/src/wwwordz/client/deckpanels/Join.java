package wwwordz.client.deckpanels;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Join extends Composite{
	public Join(final DeckPanel deck) {
		VerticalPanel vp = new VerticalPanel();
		vp.setWidth("50%"); vp.setHeight("50%");
		vp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		vp.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		
		FlexTable table = new FlexTable();
		table.addStyleName("tableWithRowsDelimiter");
		
		table.setText(0,0,"Nick:");
		table.setText(1,0,"Password:");
		
		TextBox tb = new TextBox();
	    tb.getElement().setPropertyString("placeholder", "Enter your nick name");
	    
	    PasswordTextBox pb = new PasswordTextBox();
	    pb.getElement().setPropertyString("placeholder", "Enter your password");
	    
	    
	    table.setWidget(0,1,tb);
	    table.setWidget(1,1,pb);
	    
	    table.getFlexCellFormatter().setColSpan(0,0,2);
	    table.getFlexCellFormatter().setColSpan(1,0,2);
	    
		vp.add(table);	
		
		
		Label lb = new Label();
		lb.setWidth("50%");
		lb.addStyleName("bottomBorder");
		
		vp.add(lb);
		
		Button log = new Button("Exit");
	    log.addClickHandler(new ClickHandler() {
	    	public void onClick(ClickEvent event) {
	    		deck.showWidget(0);
	    	}
	    });
	    
	    log.getElement().getStyle().setProperty("margin", "5%");
	    log.getElement().getStyle().setProperty("padding", "2% 5% 2% 5%");
	    
	    vp.add(log);
		
	    initWidget(vp);
	}
}
