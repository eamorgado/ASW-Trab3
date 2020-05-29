package wwwordz.client;

import com.google.gwt.core.client.GWT;

public class Services {
	private final static ManagerServiceAsync managerService = GWT.create(ManagerService.class);
	
	public static ManagerServiceAsync getService() {
		return managerService;
	}
}
