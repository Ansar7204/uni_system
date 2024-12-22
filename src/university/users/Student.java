package university.users;

import university.communication.Language;
import university.communication.Log;
import university.communication.Message;
import university.communication.News;
import university.courses.*;
import university.database.DatabaseManager;
import university.exceptions.CreditLimitExceededException;
import university.library.Book;
import university.research.ResearchPaper;
import university.research.Researcher;

import java.util.*;

public class Student extends User {

	private String studentId;
	private School school;
	private Transcript transcript;
	private List<StudentOrganization> organizationMembership;
	private int yearOfStudy;
	private List<Course> registeredCourses;
	private List<ResearchPaper> diplomaProjects;
	public List<Files> files;
	public List<Book> borrowedBooks;


	public Student(String studentID, String firstname, String surname, String email, String password, School school, Transcript transcript, List<StudentOrganization> organizationMembership, int yearOfStudy) {
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
		Language lang = Language.getInstance();
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
			return lang.getLocalizedMessage("You are already registered for the course: " + course.getCourseName(),
					"Вы уже зарегистрированы на курс " + course.getCourseName(),"Сіз " + course.getCourseName() + " курсына тіркелгенсіз");
		}

		// Register the course and update the student's course list
		registeredCourses.add(course);
		course.addStudent(this);
		Log log = new Log(this.getFirstName() + " " + this.getSurname(),"REGISTRATION FOR COURSES");
		DatabaseManager.getInstance().addLog(log);

		return lang.getLocalizedMessage("Successfully registered for the course: " + course.getCourseName(),
				"Вы успешно зарегистрировались на курс " + course.getCourseName() ,"Сіз " + course.getCourseName() + " курсына сәтті тіркелдіңіз");
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
		Language lang = Language.getInstance();
		if (transcript.getMarks().isEmpty()) {
			return lang.getLocalizedMessage("No marks available in the transcript.","Нет доступных оценок в транскрипте","Транскриптте баға жоқ");
		}
		return lang.getLocalizedMessage("Transcript: " + transcript.getMarks().toString(),"Транскрипт: " +  transcript.getMarks().toString(),
				"Транскрипт: " +  transcript.getMarks().toString());
	}
	public Transcript getTranscript() {
		return transcript;
	}

	public String rateTeacher(Scanner scanner) {
		Language lang = Language.getInstance();
		List<Teacher> teachers = DatabaseManager.getInstance().getAllTeachers();

		if (teachers.isEmpty()) {

			System.out.println(lang.getLocalizedMessage("No teachers available to rate.","Нет учиетелей для оценки","Бағалауға мұғалімдер жоқ"));

		}
		// Display the list of teachers
		System.out.println(lang.getLocalizedMessage("Select a teacher to rate:","Выберите учителя для оценки: ","Бағалауға мұғалімді таңдаңыз: "));
		for (int i = 0; i < teachers.size(); i++) {
			Teacher teacher = teachers.get(i);
			System.out.println((i + 1) + ". " + teacher.getFirstName() + " " + teacher.getSurname());
		}

		// Get the student's choice
		System.out.print(lang.getLocalizedMessage("Enter the number of the teacher you want to rate: ","Введите число учителя для оценки","Бағалайтын мұғалімнің санын таңдаңыз"));
		int teacherChoice = scanner.nextInt();

		if (teacherChoice < 1 || teacherChoice > teachers.size()) {
			System.out.println(lang.getLocalizedMessage("Invalid option.\n","Неверный выбор \n","Қате таңдау \n"));
		}

		Teacher selectedTeacher = teachers.get(teacherChoice - 1);

		// Prompt the student to enter a rating
		System.out.print(lang.getLocalizedMessage("Enter your rating for " + selectedTeacher.getFirstName() + ": ","Введите рейтинг для " + selectedTeacher.getFirstName() + ":",
				"Бағалауды " + selectedTeacher.getFirstName() + " мұғаліміне енгізіңіз" + ":"));
		int rating = scanner.nextInt();
		if (rating < 0 || rating > 10) {
			return lang.getLocalizedMessage("Invalid rating. Please provide a rating between 0 and 10.","Неверная оценка. Оцените от 1 до 10","Қате бағалау. Бағалауды 1 ден 10 ға дейін таңдаңыз");
		}

		selectedTeacher.addRating(rating);
		Log log = new Log(this.getFirstName() + " " + this.getSurname(),"RATED TEACHERS");
		DatabaseManager.getInstance().addLog(log);

		return lang.getLocalizedMessage("Rated teacher " + selectedTeacher.getFirstName() + " with " + rating + " points. Average rating: " + selectedTeacher.getAverageRating(),
				"Оценен учитель " + selectedTeacher.getFirstName() + "с оценкой" + rating +  "Средний рейтинг: " + selectedTeacher.getAverageRating(),
				selectedTeacher.getFirstName() + " мұғалімі " + rating + " бағамен бағаланды. Орташа рейтинг: " + selectedTeacher.getAverageRating() );
	}


	public String viewFiles() {
		Language lang = Language.getInstance();

		// Refresh the files list to ensure it's up-to-date
		refreshFiles();

		if (files.isEmpty()) {
			return lang.getLocalizedMessage(
					"No files available for your courses.",
					"Нет файлов для просмотра.",
					"Қарайтын файл жоқ."
			);
		}

		// Group files by teacher and their folders
		Map<Teacher, Map<String, List<String>>> filesByTeacherAndFolder = new HashMap<>();
		for (Files folder : files) {
			Teacher teacher = folder.getTeacher();
			if (!filesByTeacherAndFolder.containsKey(teacher)) {
				filesByTeacherAndFolder.put(teacher, new HashMap<>());
			}
			// Add folder and its files to the teacher's map
			Map<String, List<String>> teacherFolders = filesByTeacherAndFolder.get(teacher);
			teacherFolders.put(folder.getNameOfFile(), folder.getFilesInFolder());
		}

		// Build a string to display files grouped by teacher and folder
		StringBuilder fileList = new StringBuilder(lang.getLocalizedMessage(
				"Files available from your teachers:\n",
				"Доступные файлы от ваших учителей:\n",
				"Сіздің мұғалімдеріңізден қолжетімді файлдар:\n"
		));

		for (Map.Entry<Teacher, Map<String, List<String>>> teacherEntry : filesByTeacherAndFolder.entrySet()) {
			Teacher teacher = teacherEntry.getKey();
			fileList.append(lang.getLocalizedMessage(
					"Teacher: ",
					"Учитель: ",
					"Мұғалім: "
			)).append(teacher.getFirstName()).append(" ").append(teacher.getSurname()).append("\n");

			Map<String, List<String>> folders = teacherEntry.getValue();
			for (Map.Entry<String, List<String>> folderEntry : folders.entrySet()) {
				String folderName = folderEntry.getKey();
				List<String> folderFiles = folderEntry.getValue();

				fileList.append("  ").append(lang.getLocalizedMessage(
						"Folder: ",
						"Папка: ",
						"Қалта: "
				)).append(folderName).append("\n");

				if (folderFiles.isEmpty()) {
					fileList.append("    ").append(lang.getLocalizedMessage(
							"No files in this folder.",
							"В этой папке нет файлов.",
							"Бұл қалтада файлдар жоқ."
					)).append("\n");
				} else {
					for (String fileName : folderFiles) {
						fileList.append("    - ").append(fileName).append("\n");
					}
				}
			}
		}

		return fileList.toString();
	}



	public String borrowBook(Scanner scanner) {
		Language lang = Language.getInstance();

		// Retrieve the single instance of the librarian
		Librarian librarian = DatabaseManager.getInstance().getLibrarian();
		if (librarian == null) {
			return  lang.getLocalizedMessage("No librarian found in the system.","Нет библиотекаря в системе","Кітапханашы системада жоқ");

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
			return lang.getLocalizedMessage("No books are available to borrow at the moment.","Нет книг для взятия","Алуға кітап жоқ");

		}

		// Display available books
		System.out.println(lang.getLocalizedMessage("Available books to borrow:","Доступные книги для взятия: ","Алуға болатын кітаптар: "));
		for (int i = 0; i < booksToBorrow.size(); i++) {
			Book book = booksToBorrow.get(i);
			System.out.println((i + 1) + ". " + book.getTitle());
		}

		// Ask the student to choose a book
		System.out.print(lang.getLocalizedMessage("Enter the number of the book you want to borrow: ","Введите число книги для взятие: ","Алатын кітаптың санын енгізіңіз: "));
		int bookChoice = scanner.nextInt();

		if (bookChoice < 1 || bookChoice > booksToBorrow.size()) {
			System.out.println(lang.getLocalizedMessage("Invalid option.\n","Неверный выбор \n","Қате таңдау \n"));
		}

		Book selectedBook = booksToBorrow.get(bookChoice - 1);

		// Create a borrow request
		librarian.receiveRequest(this, selectedBook);

		// Mark the book as borrowed
		selectedBook.setAvailable(false);  // Set the book's availability to false (borrowed)
		this.getBorrowedBooks().add(selectedBook);

		Log log = new Log(this.getFirstName() + " " + this.getSurname(),"RATED TEACHERS");
		DatabaseManager.getInstance().addLog(log);

		// Confirmation message
		return lang.getLocalizedMessage("You have successfully requested to borrow the book: " + selectedBook.getTitle(),"Вы отправили заявку на взятие книги: " + selectedBook.getTitle(),
				selectedBook.getTitle() + " кітабын алуға ұсыныс жіберілді");
	}

	public void joinOrganization(DatabaseManager db) {
		Language lang = Language.getInstance();

		// Check if there are any organizations in the system
		if (db.getOrganizations().isEmpty()) {
			System.out.println(lang.getLocalizedMessage("There are no organizations available to join.",
					"Нет доступных организаций для присоединения.",
					"Қосылуға қолжетімді ұйымдар жоқ."));
			return;
		}

		// Ask the student to choose an organization to join
		System.out.println(lang.getLocalizedMessage("Available organizations to join:",
				"Доступные организации для присоединения:",
				"Қосылуға қолжетімді ұйымдар:"));
		List<StudentOrganization> organizations = db.getOrganizations();

		for (int i = 0; i < organizations.size(); i++) {
			StudentOrganization org = organizations.get(i);
			System.out.println((i + 1) + ". " + org.getName() + " (Head: " + org.getHead().getFirstName() + " " + org.getHead().getSurname() + ")");
		}

		// Get student's choice of organization
		Scanner scanner = new Scanner(System.in);
		System.out.print(lang.getLocalizedMessage("Enter the number of the organization you want to join: ",
				"Введите номер организации, в которую вы хотите присоединиться: ",
				"Қосылғыңыз келетін ұйымның нөмірін енгізіңіз: "));
		int choice = scanner.nextInt();
		scanner.nextLine();  // Consume newline

		if (choice < 1 || choice > organizations.size()) {
			System.out.println(lang.getLocalizedMessage("Invalid organization selection.",
					"Неверный выбор организации.",
					"Қате ұйым таңдау."));
			return;
		}

		StudentOrganization selectedOrganization = organizations.get(choice - 1);

		// Check if the student is the head of the selected organization
		if (selectedOrganization.getHead().equals(this)) {
			System.out.println(lang.getLocalizedMessage("You cannot join your own organization as the head.",
					"Вы не можете присоединиться к своей организации как глава.",
					"Өзіңіздің ұйымыңызға жетекші ретінде қосыла алмайсыз."));
			return;
		}

		// Check if the student is already a member of the selected organization
		if (selectedOrganization.getMembers().contains(this)) {
			System.out.println(lang.getLocalizedMessage("You are already a member of this organization.",
					"Вы уже являетесь членом этой организации.",
					"Сіз осы ұйымның мүшесісіз."));
			return;
		}

		// Join the organization
		selectedOrganization.addMember(this);
		this.getOrganizationMemberships().add(selectedOrganization);
		System.out.println(lang.getLocalizedMessage("You have successfully joined the organization: " + selectedOrganization.getName(),
				"Вы успешно присоединились к организации: " + selectedOrganization.getName(),
				"Сіз ұйымға сәтті қосылдыңыз: " + selectedOrganization.getName()));
	}

	public void viewAndUpdateOrganization(Scanner scanner, DatabaseManager db) {
		Language lang = Language.getInstance();

		// Check if the student is a head of any organization
		StudentOrganization studentOrganization = null;
		for (StudentOrganization org : this.organizationMembership) {
			if (org.getHead().equals(this)) {
				studentOrganization = org; // Student is the head of this organization
				break;
			}
		}

		if (studentOrganization == null) {
			// Student is not the head of any organization
			System.out.println(lang.getLocalizedMessage(
					"You are not the head of any organization, you cannot perform this action.",
					"Вы не являетесь руководителем никакой организации, вы не можете выполнить это действие.",
					"Сіз ешқандай ұйымның жетекшісі емессіз, осы әрекетті орындай алмайсыз."));
			return;
		}

		// Display the organization details
		System.out.println(lang.getLocalizedMessage("Your organization details:",
				"Детали вашей организации:",
				"Сіздің ұйымыңыздың мәліметтері:"));
		System.out.println(lang.getLocalizedMessage("Organization: " + studentOrganization.getName(),
				"Организация: " + studentOrganization.getName(),
				"Ұйым: " + studentOrganization.getName()));
		System.out.println(lang.getLocalizedMessage("Head: " + studentOrganization.getHead().getFirstName() + " " + studentOrganization.getHead().getSurname(),
				"Руководитель: " + studentOrganization.getHead().getFirstName() + " " + studentOrganization.getHead().getSurname(),
				"Жетекші: " + studentOrganization.getHead().getFirstName() + " " + studentOrganization.getHead().getSurname()));
		System.out.println(lang.getLocalizedMessage("Members: ",
				"Члены: ",
				"Мүшелері: "));
		for (Student member : studentOrganization.getMembers()) {
			System.out.println(member.getFirstName() + " " + member.getSurname());
		}

		// Option to update organization name
		System.out.println(lang.getLocalizedMessage("Would you like to update the organization name? (Yes/No)",
				"Хотите ли вы обновить название организации? (Да/Нет)",
				"Ұйымның атауын жаңартқыңыз келе ме? (Иә/Жоқ)"));
		String choice = scanner.nextLine();
		if ("Yes".equalsIgnoreCase(choice)) {
			// Get new name for the organization
			System.out.print(lang.getLocalizedMessage("Enter the new organization name: ",
					"Введите новое название организации: ",
					"Ұйымның жаңа атауын енгізіңіз: "));
			String newName = scanner.nextLine();

			// Check if the new name already exists in the database
			for (StudentOrganization org : db.getOrganizations()) {
				if (org.getName().equalsIgnoreCase(newName)) {
					System.out.println(lang.getLocalizedMessage("Organization with that name already exists.",
							"Организация с таким названием уже существует.",
							"Осы атаумен ұйым бар."));
					return;
				}
			}

			// Update organization name
			boolean updated = db.updateOrganization(studentOrganization.getName(), newName);
			if (updated) {
				studentOrganization.setName(newName);
				System.out.println(lang.getLocalizedMessage("Organization name updated successfully.",
						"Название организации успешно обновлено.",
						"Ұйымның атауы сәтті жаңартылды."));
			}
		}

		// Option to delete organization
		System.out.println(lang.getLocalizedMessage("Would you like to delete the organization? (Yes/No)",
				"Хотите ли вы удалить организацию? (Да/Нет)",
				"Ұйымды жойғыңыз келе ме? (Иә/Жоқ)"));
		String deleteChoice = scanner.nextLine();
		if ("Yes".equalsIgnoreCase(deleteChoice)) {
			// Delete the organization
			boolean deleted = db.deleteOrganization(studentOrganization.getName());
			if (deleted) {
				// Send message to all affected members
				for (Student member : studentOrganization.getMembers()) {
					Message message = new Message(this,member, lang.getLocalizedMessage("The organization " + studentOrganization.getName() + " has been deleted.",
							"Организация " + studentOrganization.getName() + " была удалена.",
							"Ұйым " + studentOrganization.getName() + " жойылды."));
					member.receiveMessage(message);  // Send message to each student
				}
				// Clear the student's organization list after deletion
				this.organizationMembership.remove(studentOrganization);
				System.out.println(lang.getLocalizedMessage("Organization deleted successfully.",
						"Организация успешно удалена.",
						"Ұйым сәтті жойылды."));
			}
		}
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

	public List<StudentOrganization> getOrganizationMemberships() {
		return organizationMembership;
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

