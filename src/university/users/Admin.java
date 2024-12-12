package university.users;

import university.communication.Message;
import university.logs.Log;

import java.util.List;


public class Admin extends Employee {
	public Log logs;
	public Admin(){
		super();
	}

	public static User createUser(String userType, String id, String name, String email, String password) {
		switch (userType.toLowerCase()) {
			case "student":
				return new Student(id, name, email, password);
			case "teacher":
				return new Teacher(id, name, email, password);
			case "librarian":
				return new Librarian(id, name, email, password);
			default:
				throw new IllegalArgumentException("Invalid user type: " + userType);
		}


	public void addUser() {
	}
	

	public void removeUser() {
	}

	
	public void updateUser() {
	}

	public void viewLogs() {
	}
	
}

