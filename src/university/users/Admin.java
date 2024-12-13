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
		List<News> newsList = new ArrayList<>();
		super(id, name, surName, email, password, newsList);
	}

	public void addUser(User user) {
		Language language = Language.getInstance();
		db.addUser(user);
		System.out.println(language.getLocalizedMessage(
				"User " +  user.getName() + " added.",
				"Пользователь " + user.getName() + " добавлен.",
				"Қолданушы " + user.getName() + " қосылды."
		));
	}

	public void removeUser(User user) {
		Language language = Language.getInstance();
		if (db.getUsers().remove(user)) {
			System.out.println(language.getLocalizedMessage(
					"User " +  user.getName() + " removed.",
					"Пользователь " + user.getName() + " удален.",
					"Қолданушы " + user.getName() + " жойылды."
			));
		}
		else {
			System.out.println("User " + user.getName() + " not found.");
			System.out.println(language.getLocalizedMessage(
					"User " +  user.getName() + " not found.",
					"Пользователь " + user.getName() + " не найден.",
					"Қолданушы " + user.getName() + " табылмады."
			));
		}
	}

	public void updateUser(String userId, String newName, String newEmail, String newPassword) {
		Language language = Language.getInstance();
		for (User user : db.getUsers()) {
			if (user.getId().equals(userId)) {
				user.setName(newName);
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
						"User " +  user.getName() + " not found.",
						"Пользователь " + user.getName() + " не найден.",
						"Қолданушы " + user.getName() + " табылмады."
				));
			}
		}
			}


	public String getRole() {
		return "Admin";
	}
}

