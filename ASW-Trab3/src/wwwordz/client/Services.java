package wwwordz.client;

/**
 * A basic auxiliar class to store our ManagerServiceAsync instance and
 * 	user nick info
 * 
 * @author Eduardo Morgado (up201706894)
 * @author Ângelo Gomes (up201703990)
 * @since May 2020
 */
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
}
