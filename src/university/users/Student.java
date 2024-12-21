package university.users;

import university.communication.News;
import university.courses.*;
import university.database.DatabaseManager;
import university.exceptions.CreditLimitExceededException;
import university.library.Book;
import university.research.ResearchPaper;
import university.research.Researcher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

public class Student extends User {

	private String studentId;
	private School school;
	private Transcript transcript;
	private StudentOrganization organizationMembership;
	private int yearOfStudy;
	private List<Course> registeredCourses;
	private List<ResearchPaper> diplomaProjects;
	public List<Files> files;
	public List<Book> borrowedBooks;


	public Student(String studentID, String firstname, String surname, String email, String password, School school, Transcript transcript, StudentOrganization organizationMembership, int yearOfStudy) {
		 super(studentID, firstname, surname, email, password);
		 this.school = school;
		 this.transcript = transcript;
		 this.organizationMembership = organizationMembership;
		 this.yearOfStudy = yearOfStudy;
		 this.registeredCourses = new ArrayList<Course>();
		 this.newsList = new ArrayList<News>();
		 this.diplomaProjects = new ArrayList<>();
		 this.files = loadFilesFromDatabase();
		 this.borrowedBooks = new ArrayList<>();
	}
	private boolean isTeacherRelatedToStudent(Teacher teacher) {
		for (Course course : registeredCourses) {
			if (course.getCourseTeachers().contains(teacher)) {
				return true;
			}
		}
		return false;
	}

	public List<Book> getBorrowedBooks() {
		return borrowedBooks;
	}

	private List<Files> loadFilesFromDatabase() {
		DatabaseManager dbManager = DatabaseManager.getInstance();
		List<Files> studentFiles = new ArrayList<>();

		// Loop through all files in DatabaseManager to find those owned by this student's teachers
		for (Files file : dbManager.getAllFolders()) {
			if (file.getTeacher() != null && isTeacherRelatedToStudent(file.getTeacher())) {
				studentFiles.add(file);
			}
		}

		return studentFiles;
	}
	public void refreshFiles() {
		this.files = loadFilesFromDatabase();
	}

	public void addDiplomaProject(Researcher researcher, ResearchPaper paper) {
		if (!researcher.getPublications().contains(paper)) {
			throw new IllegalArgumentException("The research paper is not published by the researcher.");
		}
		diplomaProjects.add(paper);
		System.out.println("Added research paper '" + paper.getTitle() + "' to the diploma projects of " + getFirstName() + " " + getSurname());
	}

	public String viewCourses() {
		StringBuilder coursesList = new StringBuilder("Registered courses:\n");
		for (Course course : registeredCourses) {
			coursesList.append(course.getCourseName()).append("\n");
		}
		return coursesList.toString();
	}

	public String registerForCourses(Course course) throws CreditLimitExceededException {
		int totalCredits = course.getCredits(); // Start with the credits of the course being added

		// Calculate total credits of currently registered courses
		for (Course registeredCourse : registeredCourses) {
			totalCredits += registeredCourse.getCredits();
		}

		// Check if total credits exceed the limit
		if (totalCredits > 21) {
			throw new CreditLimitExceededException(
					"Cannot register for " + course.getCourseName() +
							". Total credits (" + totalCredits + ") exceed the limit of 21."
			);
		}

		if (registeredCourses.contains(course)) {
			return "You are already registered for the course: " + course.getCourseName();
		}

		// Register the course and update the student's course list
		registeredCourses.add(course);
		course.addStudent(this);

		return "Successfully registered for the course: " + course.getCourseName();
	}


	public String viewTeacher(String courseName) {
		for (Course course : registeredCourses) {
			if (course.getCourseName().equals(courseName)) {
				List<Teacher> teachers = course.getCourseTeachers(); // List of teachers
				if (teachers.isEmpty()) {
					return "No teachers assigned for " + courseName;
				}

				StringBuilder teacherNames = new StringBuilder("Teachers for " + courseName + ": ");
				for (Teacher teacher : teachers) {
					teacherNames.append(teacher.getFirstName()).append(", ");
				}

				// Remove the trailing comma and space
				return teacherNames.substring(0, teacherNames.length() - 2);
			}
		}
		return "You are not registered for the course: " + courseName;
	}


	public String viewMarks(String courseName) {
		for (Mark mark : transcript.getMarks()) {
			if (mark.getCourse().getCourseName().equals(courseName)) {
				return "Your marks for " + courseName + ": " +
						"First Attestation: " + mark.getFirstAttestation() +
						", Second Attestation: " + mark.getSecondAttestation() +
						", Final: " + mark.getFinal() +
						", Total: " + mark.getValue();
			}
		}
		return "No marks found for the course: " + courseName;
	}

	public String viewTranscript() {
		if (transcript.getMarks().isEmpty()) {
			return "No marks available in the transcript.";
		}
		return "Transcript: " + transcript.getMarks().toString();
	}
	public Transcript getTranscript() {
		return transcript;
	}

	public String rateTeacher(Scanner scanner) {
		List<Teacher> teachers = DatabaseManager.getInstance().getAllTeachers();

		if (teachers.isEmpty()) {
			System.out.println("No teachers available to rate.");

		}
		// Display the list of teachers
		System.out.println("Select a teacher to rate:");
		for (int i = 0; i < teachers.size(); i++) {
			Teacher teacher = teachers.get(i);
			System.out.println((i + 1) + ". " + teacher.getFirstName() + " " + teacher.getSurname());
		}

		// Get the student's choice
		System.out.print("Enter the number of the teacher you want to rate: ");
		int teacherChoice = scanner.nextInt();

		if (teacherChoice < 1 || teacherChoice > teachers.size()) {
			System.out.println("Invalid choice.");
		}

		Teacher selectedTeacher = teachers.get(teacherChoice - 1);

		// Prompt the student to enter a rating
		System.out.print("Enter your rating for " + selectedTeacher.getFirstName() + ": ");
		int rating = scanner.nextInt();
		if (rating < 0 || rating > 10) {
			return "Invalid rating. Please provide a rating between 0 and 10.";
		}

		selectedTeacher.addRating(rating);

		return "Rated teacher " + selectedTeacher.getFirstName() + " with " + rating + " points. Average rating: " + selectedTeacher.getAverageRating();
	}


	public String viewFiles() {
		if (files.isEmpty()) {
			return "No files available for your courses.";
		}
		StringBuilder fileList = new StringBuilder("Files:\n");
		for (Files file : files) {
			fileList.append(file.getNameOfFile()).append("\n");
		}
		return fileList.toString();
	}

	public String borrowBook(Scanner scanner) {
		// Retrieve the single instance of the librarian
		Librarian librarian = DatabaseManager.getInstance().getLibrarian();
		if (librarian == null) {
			return "No librarian found in the system.";

		}

		// Retrieve the list of books from the librarian
		List<Book> availableBooks = librarian.getBooks();

		// Filter out the books that are available
		List<Book> booksToBorrow = new ArrayList<>();
		for (Book book : availableBooks) {
			if (book.isAvailable()) {
				booksToBorrow.add(book);
			}
		}

		if (booksToBorrow.isEmpty()) {
			return "No books are available to borrow at the moment.";

		}

		// Display available books
		System.out.println("Available books to borrow:");
		for (int i = 0; i < booksToBorrow.size(); i++) {
			Book book = booksToBorrow.get(i);
			System.out.println((i + 1) + ". " + book.getTitle());
		}

		// Ask the student to choose a book
		System.out.print("Enter the number of the book you want to borrow: ");
		int bookChoice = scanner.nextInt();

		if (bookChoice < 1 || bookChoice > booksToBorrow.size()) {
			System.out.println("Invalid choice.");
		}

		Book selectedBook = booksToBorrow.get(bookChoice - 1);

		// Create a borrow request
		librarian.receiveRequest(this, selectedBook);

		// Mark the book as borrowed
		selectedBook.setAvailable(false);  // Set the book's availability to false (borrowed)
		this.getBorrowedBooks().add(selectedBook);

		// Confirmation message
		return "You have successfully requested to borrow the book: " + selectedBook.getTitle();
	}


	public void notify(ResearchPaper paper) {
		System.out.println("Notification for " + getFirstName() + ": New paper published - " + paper.getTitle());
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public School getSchool() {
		return school;
	}

	public void setSchool(School school) {
		this.school = school;
	}

	public StudentOrganization getOrganizationMembership() {
		return organizationMembership;
	}

	public void setOrganizationMembership(StudentOrganization organizationMembership) {
		this.organizationMembership = organizationMembership;
	}

	public int getYearOfStudy() {
		return yearOfStudy;
	}

	public void setYearOfStudy(int yearOfStudy) {
		this.yearOfStudy = yearOfStudy;
	}

	public String getRole() {
		return "Student";
	}

	public Collection<Course> getRegisteredCourses() {
		return registeredCourses;
	}
}

