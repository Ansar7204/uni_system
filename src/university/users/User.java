package university.users;

import university.communication.Message;

import java.io.Serializable;

public abstract class User implements Serializable {
	private String id;
	private String name;
	private String email;

	public User(){
		super();
	}


	
	public String login() {
		return "";
	}
	

	
	public Message sendMessage(User parameter, Message parameter2) {
		return null;
	}

	
	public String viewNews() {
		return "";
	}
	
}

