package wwwordz.client.UserInfo;

public class UserInfo {
	private String nick;
	private String pass;
	private UserInfo instance = null;
	
	private UserInfo() {}
	
	public UserInfo getInstance() {
		if(instance == null)
			instance = new UserInfo();
		return instance;
	}
	
	public void setNick(String nick) {
		this.nick = nick;
	}
	
	public void setPass(String pass) {
		this.pass = pass;
	}
	
	public void setCredentials(String nick,String pass) {
		this.setNick(nick);
		this.setPass(pass);
	}
}
