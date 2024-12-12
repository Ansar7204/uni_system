package university.users;

import university.communication.Message;
import university.communication.News;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;

public abstract class User {
	private String id;
	private String name;
	private String email;
	private String password;
	private List<Message> receivedMessages;

	public User(String id, String name, String email, String password) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password ;
		this.receivedMessages = new ArrayList<>();
	}

	public boolean logIn(String email, String password) {
		LanguageManager languageManager = LanguageManager.getInstance();
		if(this.email.equals(email) && this.password.equals(password)) {
			System.out.println(languageManager.getLocalizedMessage(
					"Login successful for user: " + name
			));
			return true;
		}
		System.out.println(languageManager.getLocalizedMessage(
				"Login failed for user: " + name
		return false;

	}

	public void logOut() {
		LanguageManager languageManager = LanguageManager.getInstance();
		System.out.println(languageManager.getLocalizedMessage(
				"User " + name + " logged out successfully"
		));
	}

	public void commentNews(News news, String comment) {
		LanguageManager languageManager = LanguageManager.getInstance();
		news.addComment(this, comment);
		System.out.println(languageManager.getLocalizedMessage(
				"Comment addded to news by " + name,
				"Комментарий добавлен в новость пользователем " + name,
				name + " есімді қолданушы жаңалыққа пікір қосты"
		));
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<Message> getReceivedMessages() {
		return receivedMessages;
	}

	public String login() {
		return "User " + name + " has logged in successfully.";
	}

	public Message sendMessage(User recipient, Message message) {
		if (recipient == null || message == null) {
			throw new IllegalArgumentException("Recipient or message cannot be null.");
		}
		recipient.receiveMessage(message);
		return message;
	}

	public void receiveMessage(Message message) {
		if (message != null) {
			receivedMessages.add(message);
		}
	}

	public String viewMessages() {
		if (receivedMessages.isEmpty()) {
			return "No messages.";
		}
		return receivedMessages.toString();
	}

	public String viewNews() {
		return "Displaying the latest news...";
	}

	public abstract String getRole();

	public String toString() {
		return "User{id='" + id + "', name='" + name + "', email='" + email + "'}";
	}
}


