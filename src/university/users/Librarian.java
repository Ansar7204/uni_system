package university.users;

import university.communication.Language;
import university.communication.Log;
import university.communication.Message;
import university.database.DatabaseManager;
import university.library.Book;
import university.library.Request;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Librarian extends Employee {
	private static Librarian instance;

	public String name;
	private List<Request> incomingRequests;
	public List<Book> books;
	public LocalDate currentDate;

	public Librarian(String id, String name, String surName, String email, String password, DepartmentsOfEmployees department, int salary) {
		super(id, name, surName, email, password, department, salary);
		this.books = new ArrayList<>();
		this.incomingRequests = new ArrayList<>();
	}

	public static Librarian getInstance(String id, String name, String surName, String email, String password, DepartmentsOfEmployees department, int salary) {
		if (instance == null) {
			instance = new Librarian(id, name, surName, email, password, department, salary);
		}
		return instance;
	}

	// Optional: Method to reset the instance (useful if you need to reset the librarian for some reason)
	public static void resetInstance() {
		instance = null;
	}

	// 1. Add a Book
	public void addBook(String id, String title, String author, int numberOfPages) {
		Language lang = Language.getInstance();
		Book newBook = new Book(id, title, author, numberOfPages);
		books.add(newBook);
		System.out.println(lang.getLocalizedMessage(
				"Book '" + title + "' by " + author + " (ID: " + id + ", Pages: " + numberOfPages + ") added to the library.",
				"Книга '" + title + "' авторства " + author + " (ID: " + id + ", Страниц: " + numberOfPages + ") добавлена в библиотеку.",
				"Кітап '" + title + "' авторы " + author + " (ID: " + id + ", Беттер саны: " + numberOfPages + ") кітапханаға қосылды."
		));
		Log log = new Log(this.getFirstName() + " " + this.getSurname(),"ADDED BOOK");
		DatabaseManager.getInstance().addLog(log);
	}


	// 2. Remove a Book
	public void removeBook(String title) {
		Language lang = Language.getInstance();
		Book bookToRemove = null;
		for (Book book : books) {
			if (book.getTitle().equals(title)) {
				bookToRemove = book;
				break;
			}
		}

		if (bookToRemove != null) {
			books.remove(bookToRemove);
			System.out.println(lang.getLocalizedMessage(
					"Book '" + title + "' removed from the library.",
					"Книга '" + title + "' удалена из библиотеки.",
					"Кітап '" + title + "' кітапханадан алынды."
			));
			Log log = new Log(this.getFirstName() + " " + this.getSurname(),"REMOVED BOOK");
			DatabaseManager.getInstance().addLog(log);
		} else {
			System.out.println(lang.getLocalizedMessage(
					"Book '" + title + "' not found in the library.",
					"Книга '" + title + "' не найдена в библиотеке.",
					"Кітап '" + title + "' кітапханадан табылмады."
			));
		}
	}



	private Student findStudentByEmail(String email, DatabaseManager db) {
		for (User user : db.getUsers()) {
			if (user instanceof Student && user.getEmail().equals(email)) {
				return (Student) user;
			}
		}
		return null;
	}

	// 4. View Borrowed Books
	public String viewBorrowedBooks() {
		Language lang = Language.getInstance();
		StringBuilder sb = new StringBuilder(lang.getLocalizedMessage(
				"Borrowed Books:\n",
				"Взятые книги:\n",
				"Алынған кітаптар:\n"
		));
		for (Book book : books) {
			sb.append(" - ").append(book.getTitle()).append("\n");
		}
		return sb.isEmpty() ? lang.getLocalizedMessage(
				"No books are currently borrowed.",
				"На данный момент книги не взяты.",
				"Қазір кітап алынған жоқ."
		) : sb.toString();
	}

	public void receiveRequest(Student student, Book book) {
		Language lang = Language.getInstance();
		Request request = new Request(student, book);
		incomingRequests.add(request);
		System.out.println(lang.getLocalizedMessage(
				"Request received from " + student.getFirstName() + " for the book '" + book.getTitle() + "'.",
				"Запрос от " + student.getFirstName() + " на книгу '" + book.getTitle() + "'.",
				student.getFirstName() + " атты студенттің '" + book.getTitle() + "' кітабына өтінімі қабылданды."
		));
	}

	// Method to view incoming borrow requests
	public String viewIncomingRequests() {
		Language lang = Language.getInstance();
		StringBuilder sb = new StringBuilder(lang.getLocalizedMessage(
				"Incoming Borrow Requests:\n",
				"Входящие запросы на книги:\n",
				"Кітап алу өтінімдері:\n"
		));
		if (incomingRequests.isEmpty()) {
			sb.append(lang.getLocalizedMessage(
					"No new requests.",
					"Нет новых запросов.",
					"Жаңа өтінімдер жоқ."
			));
		} else {
			for (int i = 0; i < incomingRequests.size(); i++) {
				Request request = incomingRequests.get(i);
				if (!request.isProcessed()) {
					sb.append(lang.getLocalizedMessage(
							(i + 1) + ". Student: " + request.getStudent().getFirstName() + " " + request.getStudent().getSurname() +
									", Book: " + request.getBook().getTitle() + "\n",
							(i + 1) + ". Студент: " + request.getStudent().getFirstName() + " " + request.getStudent().getSurname() +
									", Книга: " + request.getBook().getTitle() + "\n",
							(i + 1) + ". Студент: " + request.getStudent().getFirstName() + " " + request.getStudent().getSurname() +
									", Кітап: " + request.getBook().getTitle() + "\n"
					));

				}
			}
		}
		return sb.toString();
	}

	// Method to handle a borrow request (approve/decline)
	public void handleBorrowRequest(int requestIndex, boolean approve) {
		Language lang = Language.getInstance();
		if (requestIndex < 1 || requestIndex > incomingRequests.size()) {
			System.out.println(lang.getLocalizedMessage("Invalid request selection.", "Неверный выбор запроса.", "Қате сұраныс таңдауы."));
			return;
		}

		Request request = incomingRequests.get(requestIndex - 1);
		Student student = request.getStudent();
		Book book = request.getBook();

		// If the request is already processed, return early
		if (request.isProcessed()) {
			System.out.println(lang.getLocalizedMessage("This request has already been processed.", "Этот запрос уже обработан.", "Бұл сұраныс бұрын өңделген."));
			return;
		}

		// Process the request: approve or decline
		if (approve) {
			if (!books.contains(book)) {
				sendMessageToStudent(student, "The book '" + book.getTitle() + "' is not available in the library.", false);
				System.out.println(lang.getLocalizedMessage("Book is not available.", "Книга недоступна.", "Кітап қолжетімсіз."));
				return;
			}

			// Lend the book
			LocalDate dueDate = currentDate.plusMonths(6);
			books.remove(book);
			student.getBorrowedBooks().add(book);
			book.setDueDate(dueDate);

			sendMessageToStudent(student, "Your request to borrow the book '" + book.getTitle() + "' has been approved. Due date: " + dueDate, true);
			System.out.println(lang.getLocalizedMessage(
					"Book '" + book.getTitle() + "' lent to " + student.getFirstName() + ".",
					"Книга '" + book.getTitle() + "' передана " + student.getFirstName() + ".",
					"Кітап '" + book.getTitle() + "' " + student.getFirstName() + "-ға берілді."
			));
			Log log = new Log(this.getFirstName() + " " + this.getSurname(),"APPROVED BOOK REQUEST");
			DatabaseManager.getInstance().addLog(log);
		} else {
			sendMessageToStudent(student, "Your request to borrow the book '" + book.getTitle() + "' has been declined.", false);
			System.out.println(lang.getLocalizedMessage("Book borrowing request declined for student " + student.getFirstName(),
					"Запрос на заем книги отклонен для студента " + student.getFirstName(),
					"Кітап алу сұранысы " + student.getFirstName() + " студенті үшін қабылданбады."
			));
			Log log = new Log(this.getFirstName() + " " + this.getSurname(),"DECLINED BOOK REQUEST");
			DatabaseManager.getInstance().addLog(log);
		}

		// Mark the request as processed
		request.setProcessed(true);
	}

	// Helper method to send a message to the student
	private void sendMessageToStudent(Student student, String content, boolean approved) {
		Language lang = Language.getInstance();
		Message message = new Message(this,student,content);
		message.setDate(LocalDateTime.now());
		student.receiveMessage(message);

		if (approved) {
			System.out.println(lang.getLocalizedMessage("Message sent to student: " + content,
					"Сообщение отправлено студенту: " + content,
					"Хабарлама студентке жіберілді: " + content
			));
		} else {
			System.out.println(lang.getLocalizedMessage("Message (Decline) sent to student: " + content,
					"Сообщение (Отклонение) отправлено студенту: " + content,
					"Хабарлама (Қабылдамау) студентке жіберілді: " + content
			));
		}
	}

	// Role Getter
	public String getRole() {
		Language lang = Language.getInstance();
		return lang.getLocalizedMessage("Librarian","Библиотекарь","Кітапханашы");
	}

	public List<Book> getBooks() {
		return books;
	}

	public String viewBooks() {
		Language lang = Language.getInstance();
		StringBuilder booksInfo = new StringBuilder();

		if (books.isEmpty()) {
			booksInfo.append(lang.getLocalizedMessage("There are no books in your library.",
					"В вашей библиотеке нет книг.",
					"Сіздің кітапханаңызда кітаптар жоқ."
			));
		} else {
			booksInfo.append(lang.getLocalizedMessage("Books currently in the library:\n",
					"Книги, доступные в библиотеке:\n",
					"Кітапханаңыздағы кітаптар:\n"
			));
			for (Book book : books) {
				// Assuming the Book class has getTitle() and getAuthor() methods
				booksInfo.append(lang.getLocalizedMessage(
						"Title: " + book.getTitle() + ", Author: " + book.getAuthor() + "\n",
						"Название: " + book.getTitle() + ", Автор: " + book.getAuthor() + "\n",
						"Атауы: " + book.getTitle() + ", Авторы: " + book.getAuthor() + "\n"
				));
			}
		}

		// Return the string containing the book information
		return booksInfo.toString();
	}
}

