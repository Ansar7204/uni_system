package university.users;

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
	private String id;
	public String name;
	private String email;
	private String password;
	private List<Message> receivedMessages;
	private List<Request> incomingRequests;
	public List<Book> books;
	public LocalDate currentDate;

	public Librarian(String id, String name, String surName, String email, String password, DepartmentsOfEmployees department, int salary) {
		super(id, name, surName, email, password, department, salary);
		this.receivedMessages = new ArrayList<>();
		this.books = new ArrayList<>();
		this.incomingRequests = new ArrayList<>();
		this.currentDate = LocalDate.now();
	}

	// 1. Add a Book
	public void addBook(String id, String title, String author, int numberOfPages) {
		Book newBook = new Book(id, title, author, numberOfPages);
		books.add(newBook);
		System.out.println("Book '" + title + "' by " + author + " (ID: " + id + ", Pages: " + numberOfPages + ") added to the library.");
	}


	// 2. Remove a Book
	public void removeBook(String title) {
		Book bookToRemove = null;
		for (Book book : books) {
			if (book.getTitle().equals(title)) {
				bookToRemove = book;
				break;
			}
		}

		if (bookToRemove != null) {
			books.remove(bookToRemove);
			System.out.println("Book '" + title + "' removed from the library.");
		} else {
			System.out.println("Book '" + title + "' not found in the library.");
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
		StringBuilder sb = new StringBuilder("Borrowed Books:\n");
		for (Book book : books) {
			sb.append(" - ").append(book.getTitle()).append("\n");
		}
		return sb.isEmpty() ? "No books are currently borrowed." : sb.toString();
	}

	// 5. Send a Message


	// 6. View Messages
	public String viewMessages() {
		if (receivedMessages.isEmpty()) {
			return "No messages.";
		}

		StringBuilder sb = new StringBuilder("Messages:\n");
		for (Message msg : receivedMessages) {
			sb.append(" - ").append(msg.getContent()).append(" (Received on: ").append(msg.getDate()).append(")\n");
		}
		return sb.toString();
	}

	// 7. Change Language
	public void changeLanguage(String newLanguage) {
		System.out.println("Language has been changed to: " + newLanguage);
	}

	public void receiveRequest(Student student, Book book) {
		Request request = new Request(student, book);
		incomingRequests.add(request);
		System.out.println("Request received from " + student.getFirstName() + " for the book '" + book.getTitle() + "'.");
	}

	// Method to view incoming borrow requests
	public String viewIncomingRequests() {
		StringBuilder sb = new StringBuilder("Incoming Borrow Requests:\n");
		if (incomingRequests.isEmpty()) {
			sb.append("No new requests.");
		} else {
			for (int i = 0; i < incomingRequests.size(); i++) {
				Request request = incomingRequests.get(i);
				if (!request.isProcessed()) {
					sb.append((i + 1) + ". Student: " + request.getStudent().getFirstName() + " " + request.getStudent().getSurname() +
							", Book: " + request.getBook().getTitle() + "\n");
				}
			}
		}
		return sb.toString();
	}

	// Method to handle a borrow request (approve/decline)
	public void handleBorrowRequest(int requestIndex, boolean approve) {
		if (requestIndex < 1 || requestIndex > incomingRequests.size()) {
			System.out.println("Invalid request selection.");
			return;
		}

		Request request = incomingRequests.get(requestIndex - 1);
		Student student = request.getStudent();
		Book book = request.getBook();

		// If the request is already processed, return early
		if (request.isProcessed()) {
			System.out.println("This request has already been processed.");
			return;
		}

		// Process the request: approve or decline
		if (approve) {
			if (!books.contains(book)) {
				sendMessageToStudent(student, "The book '" + book.getTitle() + "' is not available in the library.", false);
				System.out.println("Book is not available.");
				return;
			}

			// Lend the book
			LocalDate dueDate = currentDate.plusMonths(6);
			books.remove(book);
			student.getBorrowedBooks().add(book);
			book.setDueDate(dueDate);

			sendMessageToStudent(student, "Your request to borrow the book '" + book.getTitle() + "' has been approved. Due date: " + dueDate, true);
			System.out.println("Book '" + book.getTitle() + "' lent to " + student.getFirstName() + ".");
		} else {
			sendMessageToStudent(student, "Your request to borrow the book '" + book.getTitle() + "' has been declined.", false);
			System.out.println("Book borrowing request declined for student " + student.getFirstName());
		}

		// Mark the request as processed
		request.setProcessed(true);
	}

	// Helper method to send a message to the student
	private void sendMessageToStudent(Student student, String content, boolean approved) {
		Message message = new Message(this,student,content);
		message.setDate(LocalDateTime.now());
		student.receiveMessage(message);

		if (approved) {
			System.out.println("Message sent to student: " + content);
		} else {
			System.out.println("Message (Decline) sent to student: " + content);
		}
	}

	// Role Getter
	public String getRole() {
		return "Librarian";
	}

	public String viewBooks() {
		StringBuilder booksInfo = new StringBuilder();

		if (books.isEmpty()) {
			booksInfo.append("There are no books in your library.");
		} else {
			booksInfo.append("Books currently in the library:\n");
			for (Book book : books) {
				// Assuming the Book class has getTitle() and getAuthor() methods
				booksInfo.append("Title: ").append(book.getTitle())
						.append(", Author: ").append(book.getAuthor())
						.append("\n");
			}
		}

		// Return the string containing the book information
		return booksInfo.toString();
	}
}

