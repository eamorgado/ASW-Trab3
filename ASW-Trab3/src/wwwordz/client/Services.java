package wwwordz.client;


public class Services {
	private static ManagerServiceAsync managerService;
	private static String nick;
	public static ManagerServiceAsync getService() {
		return managerService;
	}
	
	public static void addService(ManagerServiceAsync service) {
		managerService = service;
	}
	
	public static void addNick(String n) {
		nick = n;
	}
	
	public static String getNick() {
		return nick;
	}
	
	public static void reportStage() {
		
	}
}
