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
	public String firstname;
	public String surname;
	public String email;
	private String password;
	public Languages preferredLanguage;
	private List<Message> receivedMessages;
    public List<News> newsList;

	public User(String id, String firstname, String surname, String email, String password) {
		this.id = id;
		this.firstname = firstname;
		this.surname = surname;
		this.email = email;
		this.password = password;
        this.newsList = new ArrayList<News>() ;
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
		news.addComment(this.firstname + " " + this.surname, comment);
		System.out.println(language.getLocalizedMessage(
				"Comment added to news by " + firstname,
				"Комментарий добавлен в новость пользователем " + firstname,
				firstname + " есімді қолданушы жаңалыққа пікір қосты"
		));
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
		StringBuilder newsContent = new StringBuilder();

		if (newsList != null && !newsList.isEmpty()) {
			for (News news : newsList) {
				newsContent.append("Topic: ").append(news.getTopic()).append("\n")
						.append("Content: ").append(news.getContent()).append("\n")
						.append("Comments: ").append(news.getComments()).append("\n\n");
			}
		}
		else {
			newsContent.append("No news available.");
		}

		return newsContent.toString();
	}




	public abstract String getRole();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstname;
	}

	public void setFirstName(String name) {
		this.firstname = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String name) {
		this.surname = surname;
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

	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		User user = (User) o;
		return Objects.equals(id, user.id) &&
				Objects.equals(email, user.email) &&
				Objects.equals(password, user.password);
	}

	public int hashCode() {
		return Objects.hash(id, email, password);
	}
}


