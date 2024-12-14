package university.users;

import university.communication.Language;
import university.communication.Message;
import university.communication.News;
import university.database.DatabaseManager;

import java.util.ArrayList;
import java.util.List;


public class Admin extends User {

	DatabaseManager db = DatabaseManager.getInstance();

	public Admin(String id, String name, String surName,String email, String password) {
		super(id, name, surName, email, password);

	}

	public void addUser(User user) {
		Language language = Language.getInstance();
		db.addUser(user);
		System.out.println(language.getLocalizedMessage(
				"User " +  user.getFirstName() + " " + user.getSurname() + " added.",
				"Пользователь " + user.getFirstName() + " " + user.getSurname() + " добавлен.",
				"Қолданушы " + user.getFirstName() + " " + user.getSurname() + " қосылды."
		));
	}

	public void removeUser(User user) {
		Language language = Language.getInstance();
		if (db.getUsers().remove(user)) {
			System.out.println(language.getLocalizedMessage(
					"User " +  user.getFirstName() + " " + user.getSurname() + " removed.",
					"Пользователь " + user.getFirstName() + " " + user.getSurname() + " удален.",
					"Қолданушы " + user.getFirstName() + " " + user.getSurname() + " жойылды."
			));
		}
		else {
			System.out.println("User " + user.getFirstName() + " " + user.getSurname() + " not found.");
			System.out.println(language.getLocalizedMessage(
					"User " +  user.getFirstName() + " " + user.getSurname() + " not found.",
					"Пользователь " + user.getFirstName() + " " + user.getSurname() + " не найден.",
					"Қолданушы " + user.getFirstName() + " " + user.getSurname() + " табылмады."
			));
		}
	}

	public void updateUser(String userId, String newFirstName, String newSurName, String newEmail, String newPassword) {
		Language language = Language.getInstance();
		for (User user : db.getUsers()) {
			if (user.getId().equals(userId)) {
				user.setFirstName(newFirstName);
				user.setSurname(newSurName);
				user.setEmail(newEmail);
				user.setPassword(newPassword);
				System.out.println(language.getLocalizedMessage(
						"User " +  userId + " updated.",
						"Пользователь " + userId + " обновлен.",
						"Қолданушы " + userId + " өзгертіліді."
				));
				return;
			}
			else{
				System.out.println(language.getLocalizedMessage(
						"User " +  user.getFirstName() + " " + user.getSurname() + " not found.",
						"Пользователь " + user.getFirstName() + " " + user.getSurname() + " не найден.",
						"Қолданушы " + user.getFirstName() + " " + user.getSurname() + " табылмады."
				));
			}
		}
			}


	public String getRole() {
		return "Admin";
	}
}

