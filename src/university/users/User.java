package university.users;

import university.communication.*;
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
		Language language = Language.getInstance();

		System.out.print(language.getLocalizedMessage("Enter the recipient's email: ",
				"Введите электронную почту получателя",
				"Қабылдаушының почтасын енгізіңіз"));
		String recipientEmail = scanner.nextLine();

		System.out.print(language.getLocalizedMessage("Enter the message content: ",
				"Напишите сообщение",
				"Жолдаманы жазыңыз"));
		String content = scanner.nextLine();

		if (recipientEmail == null || content == null || content.trim().isEmpty()) {
			throw new IllegalArgumentException("Recipient email and content cannot be null or empty.");
		}


		User recipient = db.getUsers().stream()
				.filter(u -> u.getEmail().equalsIgnoreCase(recipientEmail))
				.findFirst()
				.orElse(null);

		if (recipient == null) {
			throw new IllegalArgumentException("User with the given email not found.");
		}

		Message newMessage = new Message(this, recipient, content);

		recipient.receiveMessage(newMessage);
		Log log = new Log(this.getFirstName() + " " + this.getSurname(),"SEND MESSAGE");
		DatabaseManager.getInstance().addLog(log);

		return language.getLocalizedMessage("Message sent successfully to " + recipient.getFirstName() + " " + recipient.getSurname() + ".",
				"Сообщение успешно отправлено к" + recipient.getFirstName() + " " + recipient.getSurname(),
				"Жолдама" + recipient.getFirstName() + " " + recipient.getSurname() + "-ға сәтті жіберілді");
	}


	public void receiveMessage(Message message) {
		if (message != null) {
			receivedMessages.add(message);
		}
	}

	public String viewMessages() {
		Language language = Language.getInstance();
		if (receivedMessages.isEmpty()) {
			return language.getLocalizedMessage("No messages.",
					"Сообщений нет",
					"Жолдама жоқ");
		}
		return receivedMessages.toString();
	}

	public String viewNews(Scanner scanner) {
		Language language = Language.getInstance();
		StringBuilder newsContent = new StringBuilder();


		if (newsList != null && !newsList.isEmpty()) {

			for (int i = 0; i < newsList.size(); i++) {
				News news = newsList.get(i);
				newsContent.append((i + 1)).append(". ")
						.append(language.getLocalizedMessage("Topic: ",
								"Тема: ","Тақырып: ")).append(news.getTopic()).append("\n")
						.append(language.getLocalizedMessage("Content: ",
								"Содержание: ","Мазмұны: ")).append(news.getContent()).append("\n");
			}

			System.out.print(language.getLocalizedMessage("Select a news item to interact with (1 to " + newsList.size() + ")",
					"Выберите новость от 1 до " + newsList.size(),"Жаңалықты таңдаңыз (1 ден " + newsList.size() + ")"));
			int newsChoice = scanner.nextInt() - 1;
			scanner.nextLine();

			if (newsChoice < 0 || newsChoice >= newsList.size()) {
				newsContent.append(language.getLocalizedMessage("Invalid choice. \n","Неверный выбор. \n","Қате таңдау. \n"));
				return newsContent.toString();
			}

			News selectedNews = newsList.get(newsChoice);

			System.out.println(language.getLocalizedMessage("1) View Comments","1) Смотреть комментарий","1) Пікірлерді қарау "));
			System.out.println(language.getLocalizedMessage("2) Add Comment","2) Добавить комментарий ","2) Пікір қосу "));

			System.out.print(language.getLocalizedMessage("Choose an option (1 or 2 )","Выберите опцию (1 или 2)","Таңдау жасаңыз (1 немесе 2)"));
			int option = scanner.nextInt();
			scanner.nextLine();

			if (option == 1) {
				newsContent.append(language.getLocalizedMessage("Comments for ","Комментарий к","Пікірлер ")).append(selectedNews.getTopic()).append("':\n");
				for (String comment : selectedNews.getComments()) {
					newsContent.append(comment).append("\n");
				}
			} else if (option == 2) {
				System.out.print(language.getLocalizedMessage("Enter your comment: ","Введите комментарий: ","Пікір қосыңыз: "));
				String commentText = scanner.nextLine();

				selectedNews.addComment(this.getFirstName() + " " + this.getSurname(), commentText);
				Log log = new Log(this.getFirstName() + " " + this.getSurname(),"COMMENTED NEWS");
				DatabaseManager.getInstance().addLog(log);
				newsContent.append(language.getLocalizedMessage("Your comment has been added to ","Ваш комментарий добавлен к","Сіздік пікіріңіз қосылды")).append(selectedNews.getTopic()).append("'.\n");
			} else {
				newsContent.append(language.getLocalizedMessage("Invalid option.\n","Неверный выбор \n","Қате таңдау \n"));
			}
		} else {
			newsContent.append(language.getLocalizedMessage("No news available.\n","Нет доступных новостей \n","Жаңалық жоқ \n"));
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
		if (this.preferredLanguage != preferredLanguage) {
			this.preferredLanguage = preferredLanguage;
			Language.getInstance().setCurrentLanguage(preferredLanguage);
			System.out.println(Language.getInstance().getLocalizedMessage(
					"Preferred language changed to: " + preferredLanguage,
					"Язык изменен на: " + preferredLanguage,
					preferredLanguage + " тіліне ауыстырылды"
			));
		}
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


