package university.users;

import university.communication.Language;
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
		// Get user details from the admin
		System.out.println("Enter user details to add:");
		System.out.print("Role (Student/Teacher/Manager/Librarian): ");
		String role = scanner.nextLine().trim();

		System.out.print("First Name: ");
		String firstName = scanner.nextLine();
		System.out.print("Last Name: ");
		String lastName = scanner.nextLine();
		System.out.print("Email: ");
		String email = scanner.nextLine();
		System.out.print("Password: ");
		String password = scanner.nextLine();

		// Create user based on role
		User newUser = createUser(role, firstName, lastName, email, password, scanner);

		// Add user to the database and print success message
		if (newUser != null) {
			db.addUser(newUser);
			Language language = Language.getInstance();
			System.out.println(language.getLocalizedMessage(
					"User " + newUser.getFirstName() + " " + newUser.getSurname() + " added.",
					"Пользователь " + newUser.getFirstName() + " " + newUser.getSurname() + " добавлен.",
					"Қолданушы " + newUser.getFirstName() + " " + newUser.getSurname() + " қосылды."
			));
		} else {
			System.out.println("Failed to create user. Please try again.");
		}
	}

	public User createUser(String role, String firstName, String lastName, String email, String password, Scanner scanner) {
		User newUser = null;

		switch (role.toLowerCase()) {
			case "student":
				newUser = createStudent(firstName, lastName, email, password, scanner);
				break;

			case "teacher":
				newUser = createTeacher(firstName, lastName, email, password, scanner);
				break;

			case "manager":
				newUser = createManager(firstName, lastName, email, password, scanner);
				break;

			case "librarian":
				newUser = createLibrarian(firstName, lastName, email, password, scanner);
				break;

			default:
				System.out.println("Invalid role entered. Please try again.");
				break;
		}

		return newUser;
	}

	public User createStudent(String firstName, String lastName, String email, String password, Scanner scanner) {
		System.out.println("Available Schools: " + Arrays.toString(School.values()));
		System.out.print("Please enter your school: ");
		School school;
		while (true) {
			try {
				school = School.valueOf(scanner.nextLine().trim().toUpperCase());
				break;
			} catch (IllegalArgumentException e) {
				System.out.println("Invalid school. Please enter a valid school: " + Arrays.toString(School.values()));
			}
		}

		System.out.print("Year of Study: ");
		int year = scanner.nextInt();
		scanner.nextLine(); // Consume newline

		System.out.println("Existing student organizations:");
		if (db.getOrganizations().isEmpty()) {
			System.out.println("No organizations available.");
		} else {
			for (int i = 0; i < db.getOrganizations().size(); i++) {
				System.out.println((i + 1) + ". " + db.getOrganizations().get(i).getName());
			}
		}

		System.out.println("Enter the number of an existing organization to join, or type a new organization name to create one (leave empty if none):");
		String organizationMembershipInput = scanner.nextLine();

		StudentOrganization organizationMembership = null;

		if (!organizationMembershipInput.isEmpty()) {
			try {
				int selectedIndex = Integer.parseInt(organizationMembershipInput) - 1;
				if (selectedIndex >= 0 && selectedIndex < db.getOrganizations().size()) {
					organizationMembership = db.getOrganizations().get(selectedIndex);
					System.out.println("Joined existing organization: " + organizationMembership.getName());
				} else {
					System.out.println("Invalid selection. Creating a new organization.");
				}
			} catch (NumberFormatException e) {
				// If input is not a number or invalid, assume it's a new organization name
				organizationMembership = new StudentOrganization(organizationMembershipInput);
				db.addOrganization(organizationMembership); // Add the new organization to the database
				System.out.println("Created and became head of the new organization: " + organizationMembership.getName());
			}
		}

		User newStudent = new Student(
				"S" + (db.getUsers().size() + 1),  // Unique student ID
				firstName,
				lastName,
				email,
				password,
				school,
				new Transcript(),
				organizationMembership,
				year
		);
		if (organizationMembership != null && organizationMembership.getHead() == null) {
			organizationMembership.setHead((Student) newStudent);
		}
		return newStudent;
	}

	public User createTeacher(String firstName, String lastName, String email, String password, Scanner scanner) {
		// Ask for teacher type
		System.out.println("Available Teacher Types: " + Arrays.toString(TeacherTypes.values()));
		TeacherTypes teacherType;
		while (true) {
			try {
				System.out.print("Please enter the teacher type (Tutor, Lector, SeniorLector, Professor): ");
				teacherType = TeacherTypes.valueOf(scanner.nextLine().trim());
				break;
			} catch (IllegalArgumentException e) {
				System.out.println("Invalid teacher type. Please enter a valid type: " + Arrays.toString(TeacherTypes.values()));
			}
		}

		// Ask for salary
		System.out.print("Salary: ");
		int salary = scanner.nextInt();
		scanner.nextLine(); // Consume newline

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
			System.out.println("No courses available in the system.");
		} else {
			System.out.println("Available Courses:");
			db.listCourses(); // List all available courses
		}

		// Ask which courses the teacher will teach
		List<Course> teacherCourses = new ArrayList<>();
		System.out.println("Enter course IDs (comma separated) that this teacher will teach, or press Enter to skip:");

		String courseInput = scanner.nextLine().trim();
		if (!courseInput.isEmpty()) {
			String[] courseIDs = courseInput.split(",");
			for (String courseID : courseIDs) {
				Course course = db.findCourseByID(courseID.trim());
				if (course != null) {
					teacherCourses.add(course);
					course.assignTeacher(newTeacher);
				} else {
					System.out.println("Course with ID " + courseID.trim() + " not found.");
				}
			}
		}

		newTeacher.courseList = teacherCourses;

		return newTeacher;
	}


	public User createManager(String firstName, String lastName, String email, String password, Scanner scanner) {
		System.out.println("Available Manager Types: " + Arrays.toString(ManagerTypes.values()));
		ManagerTypes managerType;
		while (true) {
			try {
				System.out.print("Please enter the manager type (OR, Dean): ");
				managerType = ManagerTypes.valueOf(scanner.nextLine().trim());
				break;
			} catch (IllegalArgumentException e) {
				System.out.println("Invalid manager type. Please enter a valid type: " + Arrays.toString(ManagerTypes.values()));
			}
		}

		System.out.print("Salary: ");
		int salary = scanner.nextInt();
		scanner.nextLine(); // Consume newline

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
		System.out.print("Salary: ");
		int salary = scanner.nextInt();
		scanner.nextLine(); // Consume newline

		return new Librarian(
				"L" + (db.getUsers().size() + 1),
				firstName,
				lastName,
				email,
				password,
				DepartmentsOfEmployees.Librarian, // Librarian's department is always null
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
		scanner.nextLine(); // Consume newline character

		List<Course> majorRequirements = new ArrayList<>();
		List<Course> minorRequirements = new ArrayList<>();
		// Ask about Major Requirements
		System.out.println("Do you want to add major requirements for this course? (yes/no)");
		String addMajor = scanner.nextLine().trim().toLowerCase();

		if (addMajor.equals("yes")) {
			// Major and Minor Requirements selection
			System.out.println("Select courses to add as Major Requirements:");

			listCourses();  // Show existing courses
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

		// Ask about Minor Requirements
		System.out.println("Do you want to add minor requirements for this course? (yes/no)");
		String addMinor = scanner.nextLine().trim().toLowerCase();

		if (addMinor.equals("yes")) {

			System.out.println("Select courses to add as Minor Requirements:");

			listCourses();  // Show existing courses
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

		// Create the new course with selected requirements
		Course newCourse = new Course(courseID, courseName, majorRequirements, minorRequirements, "No", credits);
		db.addCourse(newCourse);
		System.out.println("Course " + courseName + " added successfully.");
	}

	// Method to remove a course
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
		} else {
			System.out.println("Course with ID " + courseID + " not found.");
		}
	}

	// Method to update a course
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
			scanner.nextLine(); // Consume newline character
			if (newCredits > 0) {
				courseToUpdate.credits = newCredits;
			}

			// If needed, add other updates for majorRequirements, minorRequirements, etc.

			System.out.println("Course updated successfully.");
		} else {
			System.out.println("Course with ID " + courseID + " not found.");
		}
	}

	// Method to view all courses
	public void viewAllCourses() {
		listCourses();
	}

	// Helper method to list all courses
	public void listCourses() {
		System.out.println("Available courses:");
		for (Course course : db.getCourses()) {
			System.out.println("Course ID: " + course.courseID + ", Course Name: " + course.courseName);
		}
	}


	public String getRole() {
		return "Admin";
	}
}

