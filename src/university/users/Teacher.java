package university.users;

import university.communication.Complaint;
import university.communication.Language;
import university.communication.Languages;
import university.communication.UrgencyLevel;
import university.courses.Course;
import university.courses.Mark;
import university.database.DatabaseManager;
import university.research.ResearchPaper;
import university.research.Researcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Teacher extends Employee{
	public School school;
	public TeacherTypes typeOfTeacher;
	public List<Course> courseList;
	private List<Integer> ratings;
	public Researcher researcherProfile;

	public Teacher(String id, String name, String surName, String email, String password, DepartmentsOfEmployees department, int salary, TeacherTypes typeOfTeacher) {
		super(id, name, surName, email, password, department, salary);
        this.ratings = new ArrayList<>();
		this.typeOfTeacher = typeOfTeacher;
		this.courseList = new ArrayList<>();
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
		System.out.println("Courses you are teaching:");
		List<Course> courses = this.getCourses();  // Assuming Teacher has getCourses method
		for (int i = 0; i < courses.size(); i++) {
			System.out.println((i + 1) + ". " + courses.get(i).getCourseName());
		}

		// Select a course
		System.out.print("Select a course (1-" + courses.size() + "): ");
		int courseIndex = scanner.nextInt() - 1;
		Course selectedCourse = courses.get(courseIndex);

		// Display students enrolled in the selected course
		List<Student> enrolledStudents = selectedCourse.getEnrolledStudents();  // Method to get students enrolled in the course
		System.out.println("Students enrolled in " + selectedCourse.getCourseName() + ":");
		for (int i = 0; i < enrolledStudents.size(); i++) {
			System.out.println((i + 1) + ". " + enrolledStudents.get(i).getFirstName() + " " + enrolledStudents.get(i).getSurname());
		}

		// Select a student
		System.out.print("Select a student to put marks for (1-" + enrolledStudents.size() + "): ");
		int studentIndex = scanner.nextInt() - 1;
		Student selectedStudent = enrolledStudents.get(studentIndex);

		// Input marks
		System.out.print("Enter marks for " + selectedStudent.getFirstName() + " " + selectedStudent.getSurname() + " in " + selectedCourse.getCourseName() + "\n");

		System.out.print("First Attestation (0-30): ");
		double firstAttestation = scanner.nextDouble();

		System.out.print("Second Attestation (0-30): ");
		double secondAttestation = scanner.nextDouble();

		System.out.print("Final Exam (0-40): ");
		double finalExam = scanner.nextDouble();

		// Call the putMarks method to save the marks
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
		List<Student> students = db.getAllStudents();  // Assuming teacher has a method to get students
		if (students.isEmpty()) {
			System.out.println("No students available.");
		}

		// Show student options to choose from
		System.out.println("Choose a student to file a complaint against:");
		for (int i = 0; i < students.size(); i++) {
			System.out.println(i + 1 + ". " + students.get(i).getFirstName() + " " + students.get(i).getSurname());
		}
		int studentChoice = scanner.nextInt() - 1;  // Get student choice
		if (studentChoice < 0 || studentChoice >= students.size()) {
			System.out.println("Invalid choice.");
		}

		// Get the selected student
		Student selectedStudent = students.get(studentChoice);

		// Ask for the urgency level of the complaint
		System.out.println("Choose the urgency level for the complaint (1. LOW, 2. MEDIUM, 3. HIGH):");
		int urgencyChoice = scanner.nextInt();
		UrgencyLevel urgency = UrgencyLevel.LOW;  // Default to LOW
		switch (urgencyChoice) {
			case 1 -> urgency = UrgencyLevel.LOW;
			case 2 -> urgency = UrgencyLevel.MEDIUM;
			case 3 -> urgency = UrgencyLevel.HIGH;
			default -> System.out.println("Invalid urgency level. Defaulting to LOW.");
		}

		// Ask for the complaint text
		scanner.nextLine();  // Consume newline character from previous input
		System.out.println("Enter the complaint text:");
		String complaintText = scanner.nextLine();

		// Call the sendComplaint method to send the complaint
		return (this.sendComplaint(urgency, "Complaint against student " + selectedStudent.getFirstName() + " " + selectedStudent.getSurname(), selectedStudent, complaintText, db));

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
		return "something";
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
				return "Teacher of type " + getTypeOfTeacher(); // По умолчанию — английский
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


