package wwwordz.client.files.binder.table;

import java.util.Iterator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Widget;

import wwwordz.shared.Table;
import wwwordz.shared.Table.Cell;

public class TableLayout extends Composite {

	private static TableLayoutUiBinder uiBinder = GWT.create(TableLayoutUiBinder.class);

	@UiTemplate("wwwordz.client.files.xml.table.TableLayout.ui.xml")
	interface TableLayoutUiBinder extends UiBinder<Widget, TableLayout> {
	}
	
	@UiField (provided=true)FlexTable table;
	public TableLayout(Table tb) {
		setupTable(tb);
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	private void setupTable(Table tb) {
		table = new FlexTable();
		table.getElement().setId("game_table");
		Iterator<Cell> iterator = tb.iterator();
		
		Cell curr = null;
		String letter; int r,c;
		
		while(iterator.hasNext()) {
			curr = iterator.next();
			letter = String.valueOf(curr.getLetter());
			r = curr.getRow();
			c = curr.getColumn();
			table.setWidget(r,c,new TableButton(letter,r,c));
		}
	}

}
