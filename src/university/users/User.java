package university.users;

import university.communication.Language;
import university.communication.Languages;
import university.communication.Message;
import university.communication.News;
import university.database.DatabaseManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import static university.communication.Languages.EN;

public abstract class User implements Serializable {

	public String id;
	public String firstname;
	public String surname;
	public String email;
	private String password;
	public Languages preferredLanguage;
	private List<Message> receivedMessages;
    public List<News> newsList = new ArrayList<News>();

	public User(String id, String firstname, String surname, String email, String password) {
		this.id = id;
		this.firstname = firstname;
		this.surname = surname;
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
		news.addComment(this.firstname + " " + this.surname, comment);
		System.out.println(language.getLocalizedMessage(
				"Comment added to news by " + firstname,
				"Комментарий добавлен в новость пользователем " + firstname,
				firstname + " есімді қолданушы жаңалыққа пікір қосты"
		));
	}

	public String sendMessage(Scanner scanner, DatabaseManager db) {
		// Validate inputs
		System.out.print("Enter the recipient's email: ");
		String recipientEmail = scanner.nextLine();

		System.out.print("Enter the message content: ");
		String content = scanner.nextLine();

		if (recipientEmail == null || content == null || content.trim().isEmpty()) {
			throw new IllegalArgumentException("Recipient email and content cannot be null or empty.");
		}


		// Find the recipient (user) by email
		User recipient = db.getUsers().stream()
				.filter(u -> u.getEmail().equalsIgnoreCase(recipientEmail))
				.findFirst()
				.orElse(null);

		if (recipient == null) {
			throw new IllegalArgumentException("User with the given email not found.");
		}

		// Create the message
		Message newMessage = new Message(this, recipient, content);

		// Send the message to the recipient
		recipient.receiveMessage(newMessage);

		// Return confirmation
		return "Message sent successfully to " + recipient.getFirstName() + " " + recipient.getSurname() + ".";
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

	public String viewNews(Scanner scanner) {
		StringBuilder newsContent = new StringBuilder();


		if (newsList != null && !newsList.isEmpty()) {
			// Displaying the list of news to the user
			for (int i = 0; i < newsList.size(); i++) {
				News news = newsList.get(i);
				newsContent.append((i + 1)).append(". ")
						.append("Topic: ").append(news.getTopic()).append("\n")
						.append("Content: ").append(news.getContent()).append("\n");
			}

			// Asking the user to select a news item
			System.out.print("Select a news item to interact with (1 to " + newsList.size() + "): ");
			int newsChoice = scanner.nextInt() - 1;
			scanner.nextLine();  // Consume newline character

			if (newsChoice < 0 || newsChoice >= newsList.size()) {
				newsContent.append("Invalid choice.\n");
				return newsContent.toString();
			}

			News selectedNews = newsList.get(newsChoice);

			// Showing options to the user for the selected news
			System.out.println("1) View Comments");
			System.out.println("2) Add Comment");

			System.out.print("Choose an option (1 or 2): ");
			int option = scanner.nextInt();
			scanner.nextLine();  // Consume newline character

			if (option == 1) {
				// View Comments
				newsContent.append("Comments for '").append(selectedNews.getTopic()).append("':\n");
				for (String comment : selectedNews.getComments()) {
					newsContent.append(comment).append("\n");
				}
			} else if (option == 2) {
				// Add Comment
				System.out.print("Enter your comment: ");
				String commentText = scanner.nextLine();

				// Add comment to the selected news
				selectedNews.addComment(this.getFirstName() + " " + this.getSurname(), commentText);
				newsContent.append("Your comment has been added to '").append(selectedNews.getTopic()).append("'.\n");
			} else {
				newsContent.append("Invalid option.\n");
			}
		} else {
			newsContent.append("No news available.\n");
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

	@Override
	public String toString() {
		return "User{" +
				"id='" + id + '\'' +
				", firstname='" + firstname + '\'' +
				", surname='" + surname + '\'' +
				", email='" + email + '\'' +
				", password='" + password + '\'' +
				", preferredLanguage=" + preferredLanguage +
				", receivedMessages=" + receivedMessages +
				", newsList=" + newsList +
				'}';
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


