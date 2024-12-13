package university.users;

import university.communication.Language;
import university.communication.Languages;
import university.communication.Message;
import university.communication.News;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static university.communication.Languages.EN;

public abstract class User {

	public String id;
	public String name;
	public String email;
	private String password;
	public Languages preferredLanguage;
	private List<Message> receivedMessages;

	public User(String id, String name, String email, String password) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.preferredLanguage = EN;
		this.receivedMessages = new ArrayList<>();
	}



	public boolean logIn(String email, String password) {
		Language language = Language.getInstance();
		if(this.email.equals(email) && this.password.equals(password)) {
			System.out.println(language.getLocalizedMessage(
					"Login successful!",
					"Вход выполнен успешно!",
					"Сіз сәтті кірдіңіз!"
			));
			return true;
		}
		
		System.out.println(language.getLocalizedMessage(
				"Login failed!",
				"Вход не выполнен!",
				"Кіру сәтсіз аяқталды!"
		));
		return false;

	}

	public void logOut() {
		Language language = Language.getInstance();
		System.out.println(language.getLocalizedMessage(
				"Logged out.",
				"Вы вышли из системы.",
				"Сіз жүйеден шықтыңыз."
		));
	}

	public void commentNews(News news, String comment) {
		Language language = Language.getInstance();
		news.addComment(this, comment);
		System.out.println(language.getLocalizedMessage(
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Languages getPreferredLanguage() {
		return preferredLanguage;
	}

	public void setPreferredLanguage(Languages preferredLanguage) {
		this.preferredLanguage = preferredLanguage;
		System.out.println("Preferred language changed to: " + preferredLanguage);
	}

	public List<Message> getReceivedMessages() {
		return receivedMessages;
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

	public boolean equals(Object o) {
		if (this == o) return true; // Сравнение с самим собой
		if (o == null || getClass() != o.getClass()) return false; // Проверка на null и совпадение классов
		User user = (User) o;
		return Objects.equals(id, user.id) && // Сравнение по id
				Objects.equals(email, user.email) && // Сравнение по email
				Objects.equals(password, user.password); // Сравнение по password
	}

	public int hashCode() {
		return Objects.hash(id, email, password);
	}
}


