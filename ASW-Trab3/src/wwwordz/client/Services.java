package wwwordz.client;


public class Services {
	private static ManagerServiceAsync managerService;
	
	public static ManagerServiceAsync getService() {
		return managerService;
	}
	
	public static void addService(ManagerServiceAsync service) {
		managerService = service;
	}
}
