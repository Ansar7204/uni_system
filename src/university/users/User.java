package university.users;

import university.communication.Message;

/**
 * <!-- begin-user-doc -->
 * <!--  end-user-doc  -->
 * @generated
 */

public abstract class User
{
	private String id;
	private String name;
	private String email;

	public User(){
		super();
	}


	
	public String login() {
		// TODO implement me
		return "";	
	}
	

	
	public Message sendMessage(User parameter, Message parameter2) {
		// TODO implement me
		return null;	
	}

	
	public String viewNews() {
		// TODO implement me
		return "";	
	}
	
}

