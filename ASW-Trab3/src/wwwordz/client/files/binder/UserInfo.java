package wwwordz.client.files.binder;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class UserInfo extends Composite {

	private static UserInfoUiBinder uiBinder = GWT.create(UserInfoUiBinder.class);

	@UiTemplate("wwwordz.client.files.xml.UserInfo.ui.xml")
	interface UserInfoUiBinder extends UiBinder<Widget, UserInfo> {
	}
	
	@UiField (provided=true)Label user;
	
	public UserInfo(String nick) {
		setupUser(nick);
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	private void setupUser(String nick) {
		user = new Label("Nick: " + nick);
	}

}
