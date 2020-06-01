package wwwordz.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootPanel;
import wwwordz.client.files.binder.LoginForm;


/**
 * The client entry point.
 * 
 * @author Eduardo Morgado (up201706894)
 * @author Ângelo Gomes (up201703990)
 * @since May 2020
 */
public class ClientEndpoint  implements EntryPoint{
	private final ManagerServiceAsync managerService = GWT.create(ManagerService.class);
	
	@Override
	public void onModuleLoad() {
		Services.addService(managerService);
		RootPanel.get("deck_panel").add(new LoginForm());
	}
}
