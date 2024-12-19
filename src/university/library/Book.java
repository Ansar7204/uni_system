package university.library;

import java.io.Serializable;
import java.time.LocalDate;

public class Book implements Serializable {
	private String id;
	private String title;
	private String author;
	private int numberOfPages;
	private boolean isAvailable;
	private LocalDate dueDate;

	public Book(String id, String title, String author, int numberOfPages) {
		this.id = id;
		this.title = title;
		this.author = author;
		this.numberOfPages = numberOfPages;
		this.isAvailable = true;
		this.dueDate = null;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public int getNumberOfPages() {
		return numberOfPages;
	}

	public void setNumberOfPages(int numberOfPages) {
		this.numberOfPages = numberOfPages;
	}

	public boolean isAvailable() {
		return isAvailable;
	}

	public void setAvailable(boolean available) {
		isAvailable = available;
	}

	public LocalDate getDueDate() {
		return dueDate;
	}

	public void setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
	}

	public String toString() {
		return "Book{" +
				"id='" + id + '\'' +
				", title='" + title + '\'' +
				", author='" + author + '\'' +
				", numberOfPages=" + numberOfPages +
				", isAvailable=" + isAvailable +
				'}';
	}
}
