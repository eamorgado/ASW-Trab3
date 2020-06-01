package wwwordz.client.files.binder;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import wwwordz.client.Services;
import wwwordz.shared.WWWordzException;

public class LoginForm extends Composite {

	private static LoginFormUiBinder uiBinder = GWT.create(LoginFormUiBinder.class);
	
	@UiTemplate("wwwordz.client.files.xml.LoginForm.ui.xml")
	interface LoginFormUiBinder extends UiBinder<Widget, LoginForm> {
	}
	
	
	@UiField (provided=true)FlexTable table;	
	@UiField (provided=true)VerticalPanel vp;
	@UiField (provided=true)Button login_b;
	private TextBox tb;
	private PasswordTextBox pb;
	
	public LoginForm() {
		setupVerticalPanel();
		setupTable();
		setupButton();
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	private void setupVerticalPanel() {
		vp = new VerticalPanel();
		vp.setWidth("50%"); vp.setHeight("50%");
		vp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		vp.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
	}
	
	private void setupTable() {
		table = new FlexTable();
		table.setText(0,0,"Nick:");
		table.setText(1,0,"Password:");
		
	    
		tb = new TextBox();
	    tb.getElement().setPropertyString("placeholder", "Enter your nick name");
	    tb.getElement().setId("login-nick");
	    
	    pb = new PasswordTextBox();
	    pb.getElement().setPropertyString("placeholder", "Enter your password");
	    pb.getElement().setId("login-pass");
	    
	    
	    table.setWidget(0,1,tb);
	    table.setWidget(1,1,pb);
	    
	    table.getFlexCellFormatter().setColSpan(0,0,2);
	    table.getFlexCellFormatter().setColSpan(1,0,2);
	}
	
	private void setupButton() {
		login_b = new Button();
		login_b.addClickHandler(new ClickHandler() {
	    	public void onClick(ClickEvent event) {
	    		//Check for form completion
	    		String nick = tb.getText();
	    		String pass = pb.getText();
	    		boolean cond = nick.equals("") || pass.equals("");
	    		if(cond)
	    			Window.alert("All fields must be filled");
	    		else {
	    			//Send request for register
	    			try {
						serverRegister(nick,pass);
					} catch (WWWordzException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	    		}
	    	}
	    });
	}
	
	private void serverRegister(final String nick, String pass) throws WWWordzException {
		Services.getService().register(nick,pass, new AsyncCallback<Long>() {
			public void onFailure(Throwable caught) {
				Services.getService().timeToNextPlay(new AsyncCallback<Long>() {
					@Override
					public void onFailure(Throwable c) {
						Window.alert("ERROR:\n"+c.getMessage());
					}
					@Override
					public void onSuccess(Long result) {
						login_b.setEnabled(true);
						Window.alert("ERROR:\n Try again in "+ ((double)result/1000.0) + " seconds");
					}
					
				});
			}

			public void onSuccess(Long result) {
				//result is time for next round
				login_b.setEnabled(false);
				Timer t = new Timer() {
					@Override
					public void run() {
						Services.addNick(nick);
						RootPanel.get("user_info").add(new UserInfo(nick));
						RootPanel.get("deck_panel").add(new GameStage());
					}
				};
				t.schedule(result.intValue());
			}
		});
	}
}
