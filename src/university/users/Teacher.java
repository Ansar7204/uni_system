package university.users;

import university.communication.*;
import university.courses.Course;
import university.courses.Files;
import university.courses.Mark;
import university.database.DatabaseManager;
import university.research.ResearchPaper;
import university.research.Researcher;

import java.util.*;


public class Teacher extends Employee{
	public School school;
	public TeacherTypes typeOfTeacher;
	public List<Course> courseList;
	private List<Integer> ratings;
	public Researcher researcherProfile;
	public List<Files> myFiles;

	public Teacher(String id, String name, String surName, String email, String password, DepartmentsOfEmployees department, int salary, TeacherTypes typeOfTeacher) {
		super(id, name, surName, email, password, department, salary);
        this.ratings = new ArrayList<>();
		this.typeOfTeacher = typeOfTeacher;
		this.courseList = new ArrayList<>();
		this.myFiles = new ArrayList<>();
		if (isProfessor()) {
			this.researcherProfile = new Researcher(id, firstname, surname, email, password, school, 0);
		}
	}

	public void addRating(int rating) {
		ratings.add(rating);
	}

	public double getAverageRating() {
		if (ratings.isEmpty()) {
			return 0.0;
		}
		double sum = 0;
		for (int rating : ratings) {
			sum += rating;
		}
		return sum / ratings.size();
	}

	public void putMarkForStudent(Scanner scanner) {
		Language lang = Language.getInstance();
		System.out.println(lang.getLocalizedMessage("Courses you are teaching:","Курсы которые вы проводите: ","Сабақ беретін курстар: "));
		List<Course> courses = this.getCourses();
		for (int i = 0; i < courses.size(); i++) {
			System.out.println((i + 1) + ". " + courses.get(i).getCourseName());
		}

		System.out.print(lang.getLocalizedMessage("Select a course (1-" + courses.size() + "): ","Выберите курс (1-" + courses.size() + ")","Курсты таңдаңыз (1-" + courses.size() + ") "));
		int courseIndex = scanner.nextInt() - 1;
		Course selectedCourse = courses.get(courseIndex);

		List<Student> enrolledStudents = selectedCourse.getEnrolledStudents();
		System.out.println(lang.getLocalizedMessage("Students enrolled in " + selectedCourse.getCourseName() + ":","Студенты обучающийся в " + selectedCourse.getCourseName(),selectedCourse.getCourseName() + " оқып жүрген студенттер"));
		for (int i = 0; i < enrolledStudents.size(); i++) {
			System.out.println((i + 1) + ". " + enrolledStudents.get(i).getFirstName() + " " + enrolledStudents.get(i).getSurname());
		}

		System.out.print(lang.getLocalizedMessage("Select a student to put marks for (1-" + enrolledStudents.size() + "): ","Выберите студента для оценки (1-" + enrolledStudents.size() + "):","Бағалауға студентты таңдаңыз (1-" + enrolledStudents.size() + "):"));
		int studentIndex = scanner.nextInt() - 1;
		Student selectedStudent = enrolledStudents.get(studentIndex);

		System.out.print(lang.getLocalizedMessage("Enter marks for " + selectedStudent.getFirstName() + " " + selectedStudent.getSurname() + " in " + selectedCourse.getCourseName() + "\n",
				"Введите оценки для " + selectedStudent.getFirstName() + " " + selectedStudent.getSurname() + " в " + selectedCourse.getCourseName() + "\n",
				"Бағалауды " +  selectedCourse.getCourseName() + " курсындағы " + selectedStudent.getFirstName() + " " + selectedStudent.getSurname() + " студентіне "  + "енгізіңіз"));

		System.out.print(lang.getLocalizedMessage("First Attestation (0-30): ","Первая аттестация (0-30): ","Бірінші аттестация (0-30): "));
		double firstAttestation = scanner.nextDouble();

		System.out.print(lang.getLocalizedMessage("Second Attestation (0-30): ","Вторая аттестация (0-30): ","Екінші аттестация (0-30): "));
		double secondAttestation = scanner.nextDouble();

		System.out.print(lang.getLocalizedMessage("Final exam (0-40): ","Финальный экзамен (0-30): ","Шешуші экзамен (0-30): "));
		double finalExam = scanner.nextDouble();

		this.putMarks(selectedStudent, selectedCourse, firstAttestation, secondAttestation, finalExam);
	}


	public void putMarks(Student student, Course course, double firstAttestation, double secondAttestation, double finalExam) {
		Language language = Language.getInstance();
		if (!student.getRegisteredCourses().contains(course)) {
			System.out.println(language.getLocalizedMessage(
					"Student is not registered for the course:"  + course.getCourseName(),
					"Студент не зарегистрирован на курс: " + course.getCourseName(),
					"Студент " + course.getCourseName() + " курсына тіркелмеген"
			));
			return;
		}


		Mark mark = new Mark(course, firstAttestation, secondAttestation, finalExam);
		student.getTranscript().addCourseMark(course, mark);

		System.out.println(language.getLocalizedMessage("Marks have been successfully added for student " + student.getFirstName() + " " + student.getSurname() + " in the course " + course.getCourseName(),
				                " Оценки для студента успешно добавлены: "+ student.getFirstName()+ " "+ student.getSurname()+ " в курсе  " + course.getCourseName(),
				" Бағалар сәтті енгізілді "+ student.getFirstName()+ " "+ student.getSurname()+ " курс  " + course.getCourseName()));
	}


	public String sendComplaint(UrgencyLevel urgency, String complaintContent, Student student, String complaintText, DatabaseManager db) {
		Language language = Language.getInstance();
		Complaint newComplaint = new Complaint(urgency, this, student, false,complaintText);
		db.addComplaint(newComplaint);
		System.out.println(language.getLocalizedMessage("Complaint " + complaintContent + " has been sent with " + urgency + " urgency.",
				"Жалоба " + complaintContent + " была отправлена с уровнем срочности: " + urgency,
				 complaintContent + " шағымы " + urgency + " шұғылдылық деңгейімен жіберілді."));
		return complaintContent;
	}

	public String sendComplaintForStudent(Scanner scanner, DatabaseManager db) {
		Language lang = Language.getInstance();
		List<Student> students = db.getAllStudents();
		if (students.isEmpty()) {
			System.out.println(lang.getLocalizedMessage("No students available.","Студентов нет","Студенттер жоқ"));
		}

		System.out.println(lang.getLocalizedMessage("Choose a student to file a complaint against:","Выберите студента для жалобы: ","Шағым жасауға студентті таңдаңыз: "));
		for (int i = 0; i < students.size(); i++) {
			System.out.println(i + 1 + ". " + students.get(i).getFirstName() + " " + students.get(i).getSurname());
		}
		int studentChoice = scanner.nextInt() - 1;
		if (studentChoice < 0 || studentChoice >= students.size()) {
			System.out.println(lang.getLocalizedMessage("Invalid option.\n","Неверный выбор \n","Қате таңдау \n"));
		}

		Student selectedStudent = students.get(studentChoice);

		System.out.println(lang.getLocalizedMessage("Choose the urgency level for the complaint (1. LOW, 2. MEDIUM, 3. HIGH):",
				"Выберите уровень срочности для жалобы (1. НИЗКИЙ, 2. СРЕДНИЙ, 3. ВЫСОКИЙ): ",
				"Шағымның шұғылдық деңгейін таңдаңыз (1.ТӨМЕН , 2. ОРТАША , 3. ЖОҒАРЫ)"));
		int urgencyChoice = scanner.nextInt();
		UrgencyLevel urgency = UrgencyLevel.LOW;
		switch (urgencyChoice) {
			case 1 -> urgency = UrgencyLevel.LOW;
			case 2 -> urgency = UrgencyLevel.MEDIUM;
			case 3 -> urgency = UrgencyLevel.HIGH;
			default -> System.out.println(lang.getLocalizedMessage("Invalid urgency level. Defaulting to LOW.","Неверный уровень срочности. Поставлен НИЗКИЙ по умолчанию","Шұғыл деңгейі қате. ТӨМЕН деңгейіне қойылды"));
		}

		scanner.nextLine();
		System.out.println(lang.getLocalizedMessage("Enter the complaint text:","Введите текст жалобы: ","Шағымды жазыңыз: "));
		String complaintText = scanner.nextLine();

		return (this.sendComplaint(urgency, lang.getLocalizedMessage("Complaint against student ","Жалобу против студента","Шағым студентке") + selectedStudent.getFirstName() + " " + selectedStudent.getSurname(), selectedStudent, complaintText, db));

	}

	public void addFileToCourse(String folderName, String fileName, DatabaseManager db) {
		Language lang = Language.getInstance();
		Files courseFolder = db.getFolderByName(folderName);

		if (courseFolder != null) {
			courseFolder.addFile(fileName);
			System.out.println(lang.getLocalizedMessage(
					"File added successfully to the course folder.",
					"Файл успешно добавлен в папку курса.",
					"Файл сәтті түрде курс қалтасына қосылды."
			));
			Log log = new Log(this.firstname + " " + this.surname,"ADDED FILE");
			db.addLog(log);
		} else {
			System.out.println(lang.getLocalizedMessage(
					"Course folder not found.",
					"Папка курса не найдена.",
					"Курс қалтасы табылмады."
			));
		}
	}

	public List<Files> getMyFiles() {
		return myFiles;
	}

	public void viewAllFoldersAndFiles() {
		Language lang = Language.getInstance();

		if (this.myFiles.isEmpty()) {
			System.out.println(lang.getLocalizedMessage(
					"You don't have any course folders.",
					"У вас нет папок курса.",
					"Сіздің курс қалталарыңыз жоқ."
			));
			return;
		}

		System.out.println(lang.getLocalizedMessage(
				"Your course folders and their contents:",
				"Ваши папки курса и их содержимое:",
				"Сіздің курс қалталарыңыз және олардың мазмұны:"
		));

		for (Files folder : this.myFiles) {
			System.out.println(lang.getLocalizedMessage(
					"Folder: " + folder.getNameOfFile(),
					"Папка: " + folder.getNameOfFile(),
					"Қалта: " + folder.getNameOfFile()
			));

			if (folder.getFilesInFolder().isEmpty()) {
				System.out.println(lang.getLocalizedMessage(
						"  (Empty folder)",
						"  (Пустая папка)",
						"  (Бос қалта)"
				));
			} else {
				for (String file : folder.getFilesInFolder()) {
					System.out.println("  - " + file);
				}
			}
		}
	}

	public void updateFolders(Scanner scanner, DatabaseManager db) {
		Language lang = Language.getInstance();

		if (this.myFiles.isEmpty()) {
			System.out.println(lang.getLocalizedMessage(
					"You don't have any course folders.",
					"У вас нет папок курса.",
					"Сіздің курс қалталарыңыз жоқ."
			));
			return;
		}

		System.out.println(lang.getLocalizedMessage(
				"Your course folders and their contents:",
				"Ваши папки курса и их содержимое:",
				"Сіздің курс қалталарыңыз және олардың мазмұны:"
		));
		for (Files folder : this.myFiles) {
			System.out.println(lang.getLocalizedMessage(
					"Folder: " + folder.getNameOfFile(),
					"Папка: " + folder.getNameOfFile(),
					"Қалта: " + folder.getNameOfFile()
			));

			if (folder.getFilesInFolder().isEmpty()) {
				System.out.println(lang.getLocalizedMessage(
						"  (Empty folder)",
						"  (Пустая папка)",
						"  (Бос қалта)"
				));
			} else {
				for (String file : folder.getFilesInFolder()) {
					System.out.println("  - " + file);
				}
			}
		}

		System.out.println(lang.getLocalizedMessage(
				"Enter the name of the folder you want to update (or type 'back' to return): ",
				"Введите название папки, которую вы хотите обновить (или введите 'назад' для возврата): ",
				"Жаңартқыңыз келетін қалтаның атын енгізіңіз (немесе 'артқа' деп жазыңыз): "
		));
		String folderName = scanner.nextLine();

		if (folderName.equalsIgnoreCase("back") || folderName.equalsIgnoreCase("назад") || folderName.equalsIgnoreCase("артқа")) {
			return;
		}

		Files folderToUpdate = db.getFolderByName(folderName);
		if (folderToUpdate == null || !this.myFiles.contains(folderToUpdate)) {
			System.out.println(lang.getLocalizedMessage(
					"Folder not found.",
					"Папка не найдена.",
					"Қалта табылмады."
			));
			return;
		}

		if (folderToUpdate.getFilesInFolder().isEmpty()) {
			System.out.println(lang.getLocalizedMessage(
					"The folder is empty. Nothing to delete.",
					"Папка пуста. Нечего удалять.",
					"Қалта бос. Жоюға ештеңе жоқ."
			));
			return;
		}

		System.out.println(lang.getLocalizedMessage(
				"Enter the name of the file you want to delete (or type 'back' to return): ",
				"Введите название файла, который вы хотите удалить (или введите 'назад' для возврата): ",
				"Жою қажет файлдың атын енгізіңіз (немесе 'артқа' деп жазыңыз): "
		));
		String fileName = scanner.nextLine();

		if (fileName.equalsIgnoreCase("back") || fileName.equalsIgnoreCase("назад") || fileName.equalsIgnoreCase("артқа")) {
			return;
		}

		if (folderToUpdate.containsFile(fileName)) {
			folderToUpdate.removeFile(fileName);
			System.out.println(lang.getLocalizedMessage(
					"File deleted successfully.",
					"Файл успешно удален.",
					"Файл сәтті түрде жойылды."
			));
		} else {
			System.out.println(lang.getLocalizedMessage(
					"File not found in the folder.",
					"Файл не найден в папке.",
					"Файл қалтада табылмады."
			));
		}
	}



	public void deleteFileFromCourse(String folderName, String fileName, DatabaseManager db) {
		Language lang = Language.getInstance();
		Files courseFolder = db.getFolderByName(folderName);

		if (courseFolder != null) {
			if (courseFolder.containsFile(fileName)) {
				courseFolder.removeFile(fileName);
				System.out.println(lang.getLocalizedMessage(
						"File deleted successfully from the course folder.",
						"Файл успешно удален из папки курса.",
						"Файл сәтті түрде курс қалтасынан жойылды."
				));
			} else {
				System.out.println(lang.getLocalizedMessage(
						"File not found in the course folder.",
						"Файл не найден в папке курса.",
						"Файл курс қалтасында табылмады."
				));
			}
		} else {
			System.out.println(lang.getLocalizedMessage(
					"Course folder not found.",
					"Папка курса не найдена.",
					"Курс қалтасы табылмады."
			));
		}
	}

	public void createNewFolder(Scanner scanner,DatabaseManager db) {
		Language lang = Language.getInstance();

		System.out.println(lang.getLocalizedMessage(
				"Enter the name of the new folder: ",
				"Введите название новой папки: ",
				"Жаңа қалтаның атын енгізіңіз: "
		));
		String newFolderName = scanner.nextLine();

		Files newFolder = new Files(this, newFolderName);
		db.addFolder(newFolder);
		this.getMyFiles().add(newFolder);

		System.out.println(lang.getLocalizedMessage(
				"New folder created successfully.",
				"Новая папка успешно создана.",
				"Жаңа қалта сәтті жасалды."
		));
		Log log = new Log(this.firstname + " " + this.surname,"CREATED FOLDER");
		db.addLog(log);
	}

	public void deleteAllFilesFromCourse(String folderName, DatabaseManager db) {
		Language lang = Language.getInstance();
		Files courseFolder = db.getFolderByName(folderName);

		if (courseFolder != null) {
			courseFolder.getFilesInFolder().clear();
			System.out.println(lang.getLocalizedMessage(
					"All files deleted from the course folder.",
					"Все файлы удалены из папки курса.",
					"Барлық файлдар курс қалтасынан жойылды."
			));
			Log log = new Log(this.firstname + " " + this.surname,"DELETED FILES");
			db.addLog(log);
		} else {
			System.out.println(lang.getLocalizedMessage(
					"Course folder not found.",
					"Папка курса не найдена.",
					"Курс қалтасы табылмады."
			));
		}
	}
	public void deleteFiles(Scanner scanner,DatabaseManager db){
		Language lang = Language.getInstance();

		System.out.println(lang.getLocalizedMessage(
				"Choose an option:\n1. Delete all folders\n2. Delete all files from a specific folder\nEnter your choice: ",
				"Выберите опцию:\n1. Удалить все папки\n2. Удалить все файлы из определенной папки\nВведите ваш выбор: ",
				"Опцияны таңдаңыз:\n1. Барлық қалталарды жою\n2. Белгілі бір қалтадан барлық файлдарды жою\nТаңдауыңызды енгізіңіз: "
		));
		int deleteChoice = scanner.nextInt();
		scanner.nextLine();

		switch (deleteChoice) {
			case 1:
				this.getMyFiles().clear();
				db.getAllFolders().removeIf(folder -> folder.getTeacher().equals(this));
				System.out.println(lang.getLocalizedMessage(
						"All folders deleted successfully.",
						"Все папки успешно удалены.",
						"Барлық қалталар сәтті жойылды."
				));
				break;

			case 2:
				if (this.getMyFiles().isEmpty()) {
					System.out.println(lang.getLocalizedMessage(
							"You don't have any folders to delete files from.",
							"У вас нет папок, из которых можно удалить файлы.",
							"Файлдарды жою үшін қалталарыңыз жоқ."
					));
					return;
				}

				System.out.println(lang.getLocalizedMessage(
						"Your existing course folders are:",
						"Ваши существующие папки курса:",
						"Сіздің бар курс қалталарыңыз:"
				));
				for (Files folder : this.getMyFiles()) {
					System.out.println("- " + folder.getNameOfFile());
				}

				System.out.println(lang.getLocalizedMessage(
						"Enter the folder name to delete all files: ",
						"Введите название папки для удаления всех файлов: ",
						"Барлық файлдарды жою үшін қалта атауын енгізіңіз: "
				));
				String folderName = scanner.nextLine();
				this.deleteAllFilesFromCourse(folderName, db);
				break;

			default:
				System.out.println(lang.getLocalizedMessage(
						"Invalid option. Returning to the main menu.",
						"Неверная опция. Возврат в главное меню.",
						"Қате опция. Негізгі мәзірге оралу."
				));
		}
	}

	public void addFiles(Scanner scanner,DatabaseManager db){
		Language lang = Language.getInstance();

		if (this.getMyFiles().isEmpty()) {
			System.out.println(lang.getLocalizedMessage(
					"You don't have any course folders. Would you like to create one? (yes/no): ",
					"У вас нет папок курса. Хотите создать одну? (да/нет): ",
					"Сіздің курс қалталарыңыз жоқ. Біреуін жасағыңыз келе ме? (иә/жоқ): "
			));
			Set<String> affirmativeResponses = new HashSet<>(Arrays.asList(
					"yes", "да", "иә"
			));
			String response = scanner.nextLine();
			if (affirmativeResponses.contains(response.trim().toLowerCase())) {
				this.createNewFolder(scanner, db);
			} else {
				return;
			}
		}

		while (true) {
			System.out.println(lang.getLocalizedMessage(
					"Your existing course folders are:",
					"Ваши существующие папки курса:",
					"Сіздің бар курс қалталарыңыз:"
			));
			for (Files folder : this.getMyFiles()) {
				System.out.println("- " + folder.getNameOfFile());
			}

			System.out.println(lang.getLocalizedMessage(
					"Enter the folder name to add files or type 'new' to create a new folder (or 'back' to return): ",
					"Введите название папки для добавления файлов или введите 'new' для создания новой папки (или 'назад' для возврата): ",
					"Файлдарды қосу үшін қалта атауын енгізіңіз немесе жаңа қалта жасау үшін 'new' деп жазыңыз (немесе 'артқа' деп жазыңыз): "
			));
			String folderName = scanner.nextLine();
			Set<String> affirmativeResponses = new HashSet<>(Arrays.asList(
					"back", "назад", "артқа"
			));

			if (affirmativeResponses.contains(folderName.trim().toLowerCase())) {
				return;
			} else if (folderName.equalsIgnoreCase("new")) {
				this.createNewFolder(scanner, db);
			} else {
				System.out.println(lang.getLocalizedMessage(
						"Enter the file name to add: ",
						"Введите имя файла для добавления: ",
						"Қосылатын файлдың атын енгізіңіз: "
				));
				String fileName = scanner.nextLine();
				this.addFileToCourse(folderName, fileName, db);
			}
		}
	}


	public boolean isProfessor() {
		return this.typeOfTeacher.equals("Professor");
	}




	public ResearchPaper printPapers(ResearchPaper parameter) {
		return null;
	}

	public String getTypeOfTeacher() {
		return typeOfTeacher.toString();
	}

	public void setTypeOfTeacher(TeacherTypes typeOfTeacher) {
		this.typeOfTeacher = typeOfTeacher;

		if (isProfessor() && researcherProfile == null) {
			this.researcherProfile = new Researcher(getId(), getFirstName(), getSurname(), getEmail(), getPassword(), school, 0);
		}
	}

	public List<Integer> getRatings() {
		return ratings;
	}

	public List<Course> getCourses() {
		return courseList;
	}

	public String viewCourses() {
		return courseList.toString();
	}

	public String viewStudent(Student student) {
		return student.toString();
	}
	public String getRole() {
		Language language = Language.getInstance();
		Languages currentLanguage = language.getCurrentLanguage();

		switch (currentLanguage) {
			case RU:
				return "Преподаватель типа " + getTypeOfTeacher();
			case KZ:
				return "Мұғалімнің түрі: " + getTypeOfTeacher();
			default:
				return "Teacher of type " + getTypeOfTeacher();
		}
	}


	public String toString() {
		Language language = Language.getInstance();
		Languages currentLanguage = language.getCurrentLanguage();

		switch (currentLanguage) {
			case RU:
				return "Преподаватель{" +
						"id='" + id + '\'' +
						", имя='" + firstname + '\'' +
						", email='" + email + '\'' +
						", тип преподавателя=" + typeOfTeacher +
						", курсы='" + viewCourses() + '\'' +
						", средний рейтинг=" + getAverageRating() +
						'}';
			case KZ:
				return "Мұғалім{" +
						"id='" + id + '\'' +
						", аты='" + firstname + '\'' +
						", email='" + email + '\'' +
						", мұғалім түрі=" + typeOfTeacher +
						", курстар='" + viewCourses() + '\'' +
						", орташа рейтинг=" + getAverageRating() +
						'}';
			default:
				return "Teacher{" +
						"id='" + id + '\'' +
						", name='" + firstname + '\'' +
						", email='" + email + '\'' +
						", typeOfTeacher=" + typeOfTeacher +
						", courses='" + viewCourses() + '\'' +
						", averageRating=" + getAverageRating() +
						'}';
		}
	}

}


