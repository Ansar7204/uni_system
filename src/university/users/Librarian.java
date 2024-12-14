package university.users;

import university.communication.Message;
import university.library.Book;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Librarian extends Employee {
	private String id;
	public String name;
	private String email;
	private String password;
	private List<Message> receivedMessages;
	public List<Book> books;
	public LocalDate currentDate;

	public Librarian(String id, String name, String surName, String email, String password, DepartmentsOfEmployees department, int salary) {
		super(id, name, surName, email, password, department, salary);
		this.receivedMessages = new ArrayList<>();
		this.books = new ArrayList<>();
		this.currentDate = LocalDate.now();
	}

	public void addBook(Book b) {
		books.add(b);
	}

	public void lendBook(Book book, Student student) {
		if (!books.contains(book)) {
			System.out.println("The book is not available in the library.");
			return;
		}

		LocalDate dueDate = currentDate.plusMonths(6);
		books.remove(book);
		student.borrowedBooks.add(book);
		System.out.println("Book '" + book.getTitle() + "' has been lent to " + student.getFirstName() + ". Due date: " + dueDate);
	}


	public void returnBook(Book book, Student student) {
		Book borrowedBook = null;
		for (Book b : student.borrowedBooks) {
			if (student.getBorrowedBooks().equals(book)) {
				borrowedBook = b;
				break;
			}
		}

		if (borrowedBook == null) {
			System.out.println("The book '" + book.getTitle() + "' was not borrowed by the student.");
			return;
		}

		LocalDate returnDate = LocalDate.now();
		long daysLate = ChronoUnit.DAYS.between(borrowedBook.getDueDate(), returnDate);

		books.add(book);
		student.borrowedBooks.remove(borrowedBook);

		if (daysLate > 0) {
			System.out.println("Book '" + book.getTitle() + "' returned late by " + daysLate + " days.");
		} else {
			System.out.println("Book '" + book.getTitle() + "' returned on time.");
		}
	}

	public String getRole() {
		return "Librarian";
	}
}
