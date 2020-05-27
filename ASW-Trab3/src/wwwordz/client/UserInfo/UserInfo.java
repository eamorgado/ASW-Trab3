package wwwordz.client.UserInfo;

public class UserInfo {
	private static String nick = "";
	private static String pass = "";
	
	public static String getNick() {
		return nick;
	}
	
	public static String getPass() {
		return pass;
	}
	
	public static void setNick(String n) {
		nick = n;
	}
	
	public static void setPass(String p) {
		pass = p;
	}
	
	public static void setCredentials(String n,String p) {
		nick = "" + n;
		pass = "" + p;
	}
}
