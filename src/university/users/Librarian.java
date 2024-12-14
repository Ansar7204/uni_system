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

	public Librarian(String id, String name, String surName,String email, String password, DepartmentsOfEmployees department, int salary, String books) {
		super(id,name,surName,email,password,department,salary);
		this.receivedMessages = new ArrayList<>();
		this.books = new ArrayList<>();
	}


	public void addBook(Book b){

	}

	
	public void lendBook(Book b, Student s) {
	}
	
	public void returnBook(Book b, Student s) {
	}

	public String getRole() {
		return "";
	}
}

