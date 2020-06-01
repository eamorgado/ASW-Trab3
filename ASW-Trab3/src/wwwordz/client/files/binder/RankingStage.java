package wwwordz.client.files.binder;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

import wwwordz.client.Services;
import wwwordz.shared.Configs;
import wwwordz.shared.Rank;
import wwwordz.shared.WWWordzException;

public class RankingStage extends Composite{

	private static RankingStageUiBinder uiBinder = GWT.create(RankingStageUiBinder.class);

	@UiTemplate("wwwordz.client.files.xml.RankingStage.ui.xml")
	interface RankingStageUiBinder extends UiBinder<Widget, RankingStage> {
	}

	@UiField (provided=true)FlexTable table;
	
	public RankingStage() {
		table = new FlexTable();
		end();
		try {
			getRanking();
		} catch (WWWordzException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	private void end() {
		Timer t = new Timer() {
			@Override
			public void run() {
				RootPanel.get("user_info").clear();
				RootPanel.get("deck_panel").remove(0);
				RootPanel.get("deck_panel").add(new LoginForm());
			}
		};
		t.schedule((int)Configs.RANKING_STAGE_DURATION);
	}
	
	private void getRanking() throws WWWordzException {
		Services.getService().getRanking(new RankingCallBack());
	}
	
	class RankingCallBack implements AsyncCallback<List<Rank>>{

		@Override
		public void onFailure(Throwable caught) {
			Window.alert("ERROR:\n"+caught.toString());			
		}

		@Override
		public void onSuccess(List<Rank> result) {
			table.setText(0,0,"Nick");
			table.setText(0,1,"Points");
			table.setText(0,2,"Accumulated points");
			
			table.getFlexCellFormatter().setColSpan(0,0,1);
			table.getFlexCellFormatter().setColSpan(0,1,1);
			int i = 1;
			for(Rank rank : result) {
				table.setText(i,0,rank.getNick());
				table.setText(i,1,""+rank.getPoints());
				table.getFlexCellFormatter().setColSpan(i,0,1);
				table.getFlexCellFormatter().setColSpan(i,1,1);
				table.setText(i++,2,""+rank.getAccumulated());				
			}			
		}
	}
}
