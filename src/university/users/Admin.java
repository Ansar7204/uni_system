package university.users;

import university.communication.Language;
import university.communication.Log;
import university.courses.Course;
import university.courses.StudentOrganization;
import university.courses.Transcript;
import university.database.DatabaseManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


public class Admin extends User {

	DatabaseManager db = DatabaseManager.getInstance();

	public Admin(String id, String name, String surName,String email, String password) {
		super(id, name, surName, email, password);

	}

	public void addUser(Scanner scanner) {
		Language lang = Language.getInstance();
		System.out.println(lang.getLocalizedMessage(
				"Enter user details to add:",
				"Введите данные пользователя для добавления:",
				"Пайдаланушы деректерін қосу үшін енгізіңіз:"
		));

		System.out.print(lang.getLocalizedMessage(
				"Role (Student/Teacher/Manager/Librarian): ",
				"Роль (Студент/Учитель/Менеджер/Библиотекарь): ",
				"Рөл (Студент/Ұстаз/Менеджер/Кітапханашы): "
		));
		String role = scanner.nextLine().trim();

		System.out.print(lang.getLocalizedMessage(
				"First Name: ",
				"Имя: ",
				"Аты: "
		));
		String firstName = scanner.nextLine();

		System.out.print(lang.getLocalizedMessage(
				"Last Name: ",
				"Фамилия: ",
				"Тегі: "
		));
		String lastName = scanner.nextLine();

		System.out.print(lang.getLocalizedMessage(
				"Email: ",
				"Электронная почта: ",
				"Электрондық пошта: "
		));
		String email = scanner.nextLine();

		System.out.print(lang.getLocalizedMessage(
				"Password: ",
				"Пароль: ",
				"Құпия сөз: "
		));

		String password = null;
		while (true) {
			 password = scanner.nextLine();
			if(DatabaseManager.getInstance().isPasswordAlreadyExists(password)){
				System.out.println(Language.getInstance().getLocalizedMessage(
						"Password already exists. Please choose a different password.",
						"Пароль уже существует. Пожалуйста, выберите другой пароль.",
						"Құпия сөз қазірдің өзінде бар. Басқа құпия сөзді таңдаңыз."
				));
			} else {
				break;
			}
		}


		User newUser = createUser(role, firstName, lastName, email, password, scanner);

		if (newUser != null) {
			db.addUser(newUser);

			System.out.println(lang.getLocalizedMessage(
					"User " + newUser.getFirstName() + " " + newUser.getSurname() + " added.",
					"Пользователь " + newUser.getFirstName() + " " + newUser.getSurname() + " добавлен.",
					"Қолданушы " + newUser.getFirstName() + " " + newUser.getSurname() + " қосылды."
			));
			Log log = new Log(this.getFirstName() + " " + this.getSurname(),"ADDED NEW USER");
			DatabaseManager.getInstance().addLog(log);
		} else {
			System.out.println("Failed to create user. Please try again.");
		}
	}

	public User createUser(String role, String firstName, String lastName, String email, String password, Scanner scanner) {
		User newUser = null;

		switch (role.toLowerCase()) {
			case "student", "студент":
				newUser = createStudent(firstName, lastName, email, password, scanner);
				break;
            case "teacher","учитель","ұстаз":
				newUser = createTeacher(firstName, lastName, email, password, scanner);
				break;

			case "manager","менеджер":
				newUser = createManager(firstName, lastName, email, password, scanner);
				break;

			case "librarian","библиотекарь","кітапханашы":
				newUser = createLibrarian(firstName, lastName, email, password, scanner);
				break;

			default:
				System.out.println("Invalid role entered. Please try again.");
				break;
		}

		return newUser;
	}

	public User createStudent(String firstName, String lastName, String email, String password, Scanner scanner) {
		Language lang = Language.getInstance();
		System.out.println(lang.getLocalizedMessage(
				"Available Schools: " + Arrays.toString(School.values()),
				"Доступные школы: " + Arrays.toString(School.values()),
				"Қолжетімді мектептер: " + Arrays.toString(School.values())
		));

		System.out.print(lang.getLocalizedMessage(
				"Please enter your school: ",
				"Пожалуйста, введите вашу школу: ",
				"Өз мектебіңізді енгізіңіз: "
		));

		School school;
		while (true) {
			try {
				school = School.valueOf(scanner.nextLine().trim().toUpperCase());
				break;
			} catch (IllegalArgumentException e) {
				System.out.println(lang.getLocalizedMessage(
						"Invalid school. Please enter a valid school: " + Arrays.toString(School.values()),
						"Неверная школа. Пожалуйста, введите действительную школу: " + Arrays.toString(School.values()),
						"Қате мектеп. Дұрыс мектеп енгізіңіз: " + Arrays.toString(School.values())
				));
			}
		}

		System.out.print(lang.getLocalizedMessage(
				"Year of Study: ",
				"Год обучения: ",
				"Оқу жылы: "
		));
		int year = scanner.nextInt();
		scanner.nextLine();

		System.out.println(lang.getLocalizedMessage(
				"Existing student organizations:",
				"Существующие студенческие организации:",
				"Мақұлданған студенттік ұйымдар:"
		));

		if (db.getOrganizations().isEmpty()) {
			System.out.println(lang.getLocalizedMessage(
					"No organizations available.",
					"Нет доступных организаций.",
					"Ұйымдар жоқ."
			));
		} else {
			for (int i = 0; i < db.getOrganizations().size(); i++) {
				System.out.println((i + 1) + ". " + db.getOrganizations().get(i).getName());
			}
		}

		System.out.println(lang.getLocalizedMessage(
				"Enter the number of an existing organization to join, or type a new organization name to create one (leave empty if none):",
				"Введите номер существующей организации для вступления, или введите имя новой организации для создания (оставьте пустым, если нет):",
				"Бар ұйымға қосылу үшін нөмірін енгізіңіз немесе жаңа ұйымның атын жазыңыз (ештеңе болмаса бос қалдырыңыз):"
		));
		String organizationMembershipInput = scanner.nextLine();

		StudentOrganization organizationMembership = null;
		List<StudentOrganization> myOrgs = new ArrayList<>();

		if (!organizationMembershipInput.isEmpty()) {
			try {
				int selectedIndex = Integer.parseInt(organizationMembershipInput) - 1;
				if (selectedIndex >= 0 && selectedIndex < db.getOrganizations().size()) {
					organizationMembership = db.getOrganizations().get(selectedIndex);
					System.out.println(lang.getLocalizedMessage(
							"Joined existing organization: " + organizationMembership.getName(),
							"Присоединился к существующей организации: " + organizationMembership.getName(),
							"Бар ұйымға қосылды: " + organizationMembership.getName()
					));
					myOrgs.add(organizationMembership);
				} else {
					System.out.println(lang.getLocalizedMessage(
							"Invalid selection. Creating a new organization.",
							"Неверный выбор. Создаю новую организацию.",
							"Қате таңдау. Жаңа ұйым құру."
					));
				}
			} catch (NumberFormatException e) {
				organizationMembership = new StudentOrganization(organizationMembershipInput);
				db.addOrganization(organizationMembership);
				System.out.println(lang.getLocalizedMessage(
						"Created and became head of the new organization: " + organizationMembership.getName(),
						"Создано и возглавлено новое объединение: " + organizationMembership.getName(),
						"Жаңа ұйым құрылды және оның жетекшісі болдым: " + organizationMembership.getName()
				));
				myOrgs.add(organizationMembership);
			}
		}

		User newStudent = new Student(
				"S" + (db.getUsers().size() + 1),
				firstName,
				lastName,
				email,
				password,
				school,
				new Transcript(),
				myOrgs,
				year
		);
		if (organizationMembership != null && organizationMembership.getHead() == null) {
			organizationMembership.setHead((Student) newStudent);
		}
		return newStudent;
	}

	public User createTeacher(String firstName, String lastName, String email, String password, Scanner scanner) {
		Language lang = Language.getInstance();
		System.out.println(lang.getLocalizedMessage(
				"Available Teacher Types: " + Arrays.toString(TeacherTypes.values()),
				"Доступные типы преподавателей: " + Arrays.toString(TeacherTypes.values()),
				"Қолжетімді оқытушы түрлері: " + Arrays.toString(TeacherTypes.values())
		));
		TeacherTypes teacherType;
		while (true) {
			try {
				System.out.print(lang.getLocalizedMessage(
						"Please enter the teacher type (Tutor, Lector, SeniorLector, Professor): ",
						"Введите тип преподавателя (Tutor - Тьютор,Lector - Лектор,SeniorLector - Старший Лектор,Professor - Профессор): ",
						"Оқытушы түрін енгізіңіз (Tutor - Тьютор,Lector - Лектор,SeniorLector -  Аға Лектор,Professor - Профессор): "
				));
				teacherType = TeacherTypes.valueOf(scanner.nextLine().trim());
				break;
			} catch (IllegalArgumentException e) {
				System.out.println(lang.getLocalizedMessage(
						"Invalid teacher type. Please enter a valid type: " + Arrays.toString(TeacherTypes.values()),
						"Неверный тип преподавателя. Пожалуйста, введите действительный тип: " + Arrays.toString(TeacherTypes.values()),
						"Қате оқытушы түрі. Дұрыс түрін енгізіңіз: " + Arrays.toString(TeacherTypes.values())
				));
			}
		}

		System.out.print(lang.getLocalizedMessage(
				"Salary: ",
				"Зарплата: ",
				"Жалақы: "
		));
		int salary = scanner.nextInt();
		scanner.nextLine();

		Teacher newTeacher = new Teacher(
				"T" + System.currentTimeMillis(),
				firstName,
				lastName,
				email,
				password,
				DepartmentsOfEmployees.Teacher,
				salary,
				teacherType
		);


		if (db.getCourses().isEmpty()) {
			System.out.println(lang.getLocalizedMessage(
					"No courses available in the system.",
					"В системе нет доступных курсов.",
					"Жүйеде қолжетімді курстар жоқ."
			));
		} else {
			System.out.println(lang.getLocalizedMessage(
					"Available Courses:",
					"Доступные курсы:",
					"Қолжетімді курстар:"
			));
			db.listCourses();
		}

		List<Course> teacherCourses = new ArrayList<>();
		System.out.println(lang.getLocalizedMessage(
				"Enter course IDs (comma separated) that this teacher will teach, or press Enter to skip:",
				"Введите идентификаторы курсов (через запятую), которые этот преподаватель будет преподавать, или нажмите Enter для пропуска:",
				"Бұл оқытушы оқытатын курстардың ID-лерін енгізіңіз (үлкен әріптермен, үтірмен бөлініп), немесе өткізіп жіберу үшін Enter басыңыз:"
		));

		String courseInput = scanner.nextLine().trim();
		if (!courseInput.isEmpty()) {
			String[] courseIDs = courseInput.split(",");
			for (String courseID : courseIDs) {
				Course course = db.findCourseByID(courseID.trim());
				if (course != null) {
					teacherCourses.add(course);
					course.assignTeacher(newTeacher);
				} else {
					System.out.println(lang.getLocalizedMessage(
							"Course with ID " + courseID.trim() + " not found.",
							"Курс с ID " + courseID.trim() + " не найден.",
							"ID-мен курс табылмады: " + courseID.trim()
					));
				}
			}
		}

		newTeacher.courseList = teacherCourses;

		return newTeacher;
	}


	public User createManager(String firstName, String lastName, String email, String password, Scanner scanner) {
		Language lang = Language.getInstance();
		System.out.println(lang.getLocalizedMessage(
				"Available Manager Types: " + Arrays.toString(ManagerTypes.values()),
				"Доступные типы менеджеров: " + Arrays.toString(ManagerTypes.values()),
				"Қолжетімді менеджер түрлері: " + Arrays.toString(ManagerTypes.values())
		));
		ManagerTypes managerType;
		while (true) {
			try {
				System.out.print(lang.getLocalizedMessage(
						"Please enter the manager type (OR, Dean): ",
						"Введите тип менеджера (OR - ОР, Dean - Декан): ",
						"Менеджер түрін енгізіңіз (OR - ОР,Dean - Декан): "
				));
				managerType = ManagerTypes.valueOf(scanner.nextLine().trim());
				break;
			} catch (IllegalArgumentException e) {
				System.out.println(lang.getLocalizedMessage(
						"Invalid manager type. Please enter a valid type: " + Arrays.toString(ManagerTypes.values()),
						"Неверный тип менеджера. Пожалуйста, введите действительный тип: " + Arrays.toString(ManagerTypes.values()),
						"Қате менеджер түрі. Дұрыс түрін енгізіңіз: " + Arrays.toString(ManagerTypes.values())
				));
			}
		}

		System.out.print(lang.getLocalizedMessage(
				"Salary: ",
				"Зарплата: ",
				"Жалақы: "
		));
		int salary = scanner.nextInt();
		scanner.nextLine();

		return new Manager(
				"M" + (db.getUsers().size() + 1),
				firstName,
				lastName,
				email,
				password,
				DepartmentsOfEmployees.Manager,
				salary,
				managerType
		);
	}

	public User createLibrarian(String firstName, String lastName, String email, String password, Scanner scanner) {
		Language lang = Language.getInstance();
		System.out.print(lang.getLocalizedMessage(
				"Salary: ",
				"Зарплата: ",
				"Жалақы: "
		));
		int salary = scanner.nextInt();
		scanner.nextLine();

		return new Librarian(
				"L" + (db.getUsers().size() + 1),
				firstName,
				lastName,
				email,
				password,
				DepartmentsOfEmployees.Librarian,
				salary
		);
	}


	public void removeUser(User user) {
		Language language = Language.getInstance();
		if (db.getUsers().remove(user)) {
			System.out.println(language.getLocalizedMessage(
					"User " +  user.getFirstName() + " " + user.getSurname() + " removed.",
					"Пользователь " + user.getFirstName() + " " + user.getSurname() + " удален.",
					"Қолданушы " + user.getFirstName() + " " + user.getSurname() + " жойылды."
			));
		}
		else {
			System.out.println("User " + user.getFirstName() + " " + user.getSurname() + " not found.");
			System.out.println(language.getLocalizedMessage(
					"User " +  user.getFirstName() + " " + user.getSurname() + " not found.",
					"Пользователь " + user.getFirstName() + " " + user.getSurname() + " не найден.",
					"Қолданушы " + user.getFirstName() + " " + user.getSurname() + " табылмады."
			));
			Log log = new Log(this.getFirstName() + " " + this.getSurname(),"REMOVED USER");
			DatabaseManager.getInstance().addLog(log);
		}
	}

	public void updateUser(String userId, String newFirstName, String newSurName, String newEmail, String newPassword) {
		Language language = Language.getInstance();
		for (User user : db.getUsers()) {
			if (user.getId().equals(userId)) {
				user.setFirstName(newFirstName);
				user.setSurname(newSurName);
				user.setEmail(newEmail);
				user.setPassword(newPassword);
				System.out.println(language.getLocalizedMessage(
						"User " +  userId + " updated.",
						"Пользователь " + userId + " обновлен.",
						"Қолданушы " + userId + " өзгертіліді."
				));
				return;
			}
			else{
				System.out.println(language.getLocalizedMessage(
						"User " +  user.getFirstName() + " " + user.getSurname() + " not found.",
						"Пользователь " + user.getFirstName() + " " + user.getSurname() + " не найден.",
						"Қолданушы " + user.getFirstName() + " " + user.getSurname() + " табылмады."
				));
			}
		}
			}

	public void addCourse(Scanner scanner) {
		System.out.println("Enter course details:");

		System.out.print("Course ID: ");
		String courseID = scanner.nextLine();

		System.out.print("Course Name: ");
		String courseName = scanner.nextLine();

		System.out.print("Credits: ");
		int credits = scanner.nextInt();
		scanner.nextLine();

		List<Course> majorRequirements = new ArrayList<>();
		List<Course> minorRequirements = new ArrayList<>();
		System.out.println("Do you want to add major requirements for this course? (yes/no)");
		String addMajor = scanner.nextLine().trim().toLowerCase();

		if (addMajor.equals("yes")) {
			System.out.println("Select courses to add as Major Requirements:");

			listCourses();
			System.out.print("Enter course IDs (separated by commas) for major requirements, or type 'none' to skip: ");
			String majorCourseIDs = scanner.nextLine();

			if (!majorCourseIDs.equals("none")) {
				String[] majorCourses = majorCourseIDs.split(",");
				for (String id : majorCourses) {
					Course course = db.findCourseByID(id.trim());
					if (course != null) {
						majorRequirements.add(course);
					} else {
						System.out.println("Course with ID " + id.trim() + " not found.");
					}
				}
			}
		}

		System.out.println("Do you want to add minor requirements for this course? (yes/no)");
		String addMinor = scanner.nextLine().trim().toLowerCase();

		if (addMinor.equals("yes")) {

			System.out.println("Select courses to add as Minor Requirements:");

			listCourses();
			System.out.print("Enter course IDs (separated by commas) for minor requirements, or type 'none' to skip: ");
			String minorCourseIDs = scanner.nextLine();

			if (!minorCourseIDs.equals("none")) {
				String[] minorCourses = minorCourseIDs.split(",");
				for (String id : minorCourses) {
					Course course = db.findCourseByID(id.trim());
					if (course != null) {
						minorRequirements.add(course);
					} else {
						System.out.println("Course with ID " + id.trim() + " not found.");
					}
				}
			}
		}
		Course newCourse = new Course(courseID, courseName, majorRequirements, minorRequirements, "No", credits);
		db.addCourse(newCourse);
		System.out.println("Course " + courseName + " added successfully.");
		Log log = new Log(this.getFirstName() + " " + this.getSurname(),"ADDED COURSE");
		DatabaseManager.getInstance().addLog(log);
	}

	public void removeCourse(Scanner scanner) {
		System.out.println("Select a course to remove:");
		listCourses();

		System.out.print("Enter the course ID to remove: ");
		String courseID = scanner.nextLine();

		Course courseToRemove = null;
		for (Course course : db.getCourses()) {
			if (course.courseID.equals(courseID)) {
				courseToRemove = course;
				break;
			}
		}

		if (courseToRemove != null) {
			db.getCourses().remove(courseToRemove);
			System.out.println("Course " + courseToRemove.getCourseName() + " removed successfully.");
			Log log = new Log(this.getFirstName() + " " + this.getSurname(),"REMOVED COURSE");
			DatabaseManager.getInstance().addLog(log);
		} else {
			System.out.println("Course with ID " + courseID + " not found.");
		}
	}

	public void updateCourse(Scanner scanner) {
		System.out.println("Select a course to update:");
		listCourses();

		System.out.print("Enter the course ID to update: ");
		String courseID = scanner.nextLine();

		Course courseToUpdate = null;
		for (Course course : db.getCourses()) {
			if (course.courseID.equals(courseID)) {
				courseToUpdate = course;
				break;
			}
		}

		if (courseToUpdate != null) {
			System.out.print("Enter new course name (current: " + courseToUpdate.getCourseName() + "): ");
			String newCourseName = scanner.nextLine();
			if (!newCourseName.isEmpty()) {
				courseToUpdate.courseName = newCourseName;
			}

			System.out.print("Enter new credits (current: " + courseToUpdate.credits + "): ");
			int newCredits = scanner.nextInt();
			scanner.nextLine();
			if (newCredits > 0) {
				courseToUpdate.credits = newCredits;
			}

			System.out.println("Course updated successfully.");
		} else {
			System.out.println("Course with ID " + courseID + " not found.");
		}
	}

	public void viewAllCourses() {
		listCourses();
	}

	public void listCourses() {
		System.out.println("Available courses:");
		for (Course course : db.getCourses()) {
			System.out.println("Course ID: " + course.courseID + ", Course Name: " + course.courseName);
		}
	}

	public void addStudentOrganization() {
		Scanner scanner = new Scanner(System.in);
		Language lang = Language.getInstance();

		System.out.print(lang.getLocalizedMessage("Enter organization name: ",
				"Введите название организации: ",
				"Ұйым атауын енгізіңіз: "));
		String organizationName = scanner.nextLine().trim();

		if (organizationName.isEmpty()) {
			System.out.println(lang.getLocalizedMessage("Organization name cannot be empty.",
					"Название организации не может быть пустым.",
					"Ұйым атауы бос болмауы керек."));
			return;
		}

		for (StudentOrganization org : db.getOrganizations()) {
			if (org.getName().equalsIgnoreCase(organizationName)) {
				System.out.println(lang.getLocalizedMessage("Organization already exists.",
						"Организация уже существует.",
						"Ұйым бар."));
				return;
			}
		}

		StudentOrganization newOrganization = new StudentOrganization(organizationName);

		System.out.println(lang.getLocalizedMessage("Available students to choose as the head of the organization:",
				"Доступные студенты для назначения руководителем организации:",
				"Ұйымның жетекшісі етіп тағайындай алатын студенттер тізімі:"));
		List<Student> students = db.getAllStudents();

		if (students.isEmpty()) {
			System.out.println(lang.getLocalizedMessage("No students available to assign as the head.",
					"Нет доступных студентов для назначения руководителем.",
					"Жетекші тағайындауға студенттер жоқ."));
			return;
		}

		for (int i = 0; i < students.size(); i++) {
			Student student = students.get(i);
			System.out.println((i + 1) + ". " + student.getFirstName() + " " + student.getSurname());
		}

		System.out.print(lang.getLocalizedMessage("Enter the number of the student to assign as the head: ",
				"Введите номер студента для назначения руководителем: ",
				"Жетекші етіп тағайындау үшін студенттің нөмірін енгізіңіз: "));
		int choice = scanner.nextInt();
		scanner.nextLine();
		if (choice < 1 || choice > students.size()) {
			System.out.println(lang.getLocalizedMessage("Invalid student selection.",
					"Неверный выбор студента.",
					"Қате студент таңдау."));
			return;
		}

		Student head = students.get(choice - 1);
		newOrganization.setHead(head);

		db.addOrganization(newOrganization);
	}


	public String getRole() {
		return "Admin";
	}
}

