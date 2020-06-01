package wwwordz.client.files.binder.table;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

import wwwordz.client.GameMechanics;

/**
 * This class represents a single button and all its functions
 * 
 * @author Eduardo Morgado (up201706894)
 * @author Ângelo Gomes (up201703990)
 * @since May 2020
 */
public class TableButton extends Composite {

	private static TableButtonUiBinder uiBinder = GWT.create(TableButtonUiBinder.class);

	@UiTemplate("wwwordz.client.files.xml.table.TableButton.ui.xml")
	interface TableButtonUiBinder extends UiBinder<Widget, TableButton> {
	}

	@UiField (provided=true)Button button;
	
	public TableButton(String letter, int r, int c) {
		addButton(letter,r,c);
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	private void addButton(String letter, int r, int c) {
		button = new Button(letter);
		button.getElement().setId(r+"_"+c);
		button.addClickHandler(new SequenceHandler(letter,r,c));
	}
	
	/**
	 * This class is responsible for handling the click events
	 * it will check if the user click can be considered a doube click or not
	 * (if it is in the time threshold)
	 * 
	 * Depending on each type of click it will either add a new letter to the sequence or
	 * submit the sequence
	 * 
	 * This actions are supported by GameMechanics
	 * 
	 * @author Eduardo Morgado (up201706894)
	 * @author Ângelo Gomes (up201703990)
	 * @since May 2020
	 */
	public static class SequenceHandler implements ClickHandler{
		private static String words = "";
		private int row,col;
		private String letter;
		private Date time = new Date();
		
		SequenceHandler(String l, int r, int c){
			letter = l;
			row = r;
			col = c;
		}

		@Override
		public void onClick(ClickEvent event) {
			Date now = new Date();
			if((now.getTime() - time.getTime()) < 500L) //double click => submit sequence
				submit();
			else //normar click => just add the letter
				selectLetter();	
			time = now;			
		}
		
		private void selectLetter() {
			words += letter;
			//add letter to sequence observers
			GameMechanics.getInstance().addClicked(letter,row,col);
			//Append word to live sequence
			if(RootPanel.get("sequence").getWidgetCount() >= 1)
				RootPanel.get("sequence").clear();
			RootPanel.get("sequence").add(new Label(words));
		}
		
		/**
		 * Method to submit a word, it will retrieve all selected words
		 * and will try to submit, depending on the result it will give an error message
		 * or add the word to the submitted ones
		 */
		private void submit() {
			//get all selected letters
			String word = GameMechanics.getInstance().getWord();
			words = "";
			int status = GameMechanics.getInstance().submitSolution();
			RootPanel.get("sequence").clear();
			if(status == -1) //fail => solution not exist/already selected
				RootPanel.get("messages").add(new Label("Word ["+word+"] invalid or repeated"));
			else //append to selected
				RootPanel.get("words").add(new Label(word));		
		}
	}
}
