package university.users;

import university.communication.Message;
import university.library.Book;

import java.util.ArrayList;
import java.util.List;


public class Librarian extends Employee {
	private String id;
	private String name;
	private String email;
	private String password;
	private List<Message> receivedMessages;
	private List<Book> books;

	public Librarian(String id, String name, String email, String password, String books) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.receivedMessages = new ArrayList<>();
		this.books = new ArrayList<>();
	}


	public void addBook(Book b) {
	}

	
	public void lendBook(Book b, Student s) {
	}
	
	public void returnBook(Book b, Student s) {
	}

	public String getRole() {
		return "";
	}
}

