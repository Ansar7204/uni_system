package university.users;

import university.communication.*;
import university.courses.Course;
import university.courses.Mark;
import university.courses.School;
import university.research.ResearchPaper;
import university.research.ResearchProject;
import university.research.Researcher;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class Teacher extends Employee{
	private List<Message> receivedMessages;
	public School school;
	public TeacherTypes typeOfTeacher;
	private String courses;
	public List<Course> courseList;
	private List<Integer> ratings = new ArrayList<>();
	public Researcher researcherProfile;

	public Teacher(String id, String name, String surName, String email, String password, DepartmentsOfEmployees department, int salary, TeacherTypes typeOfTeacher, String courses) {
		super(id, name, surName, email, password, department, salary);
        this.ratings = ratings;
        this.receivedMessages = new ArrayList<>();
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




	public void sendComplaint(UrgencyLevel urgency, String complaintContent,Student student) {
		Language language = Language.getInstance();
		Student studentGettingComplaint = student;
		Complaints newComplaint = new Complaints(urgency, this, studentGettingComplaint, false);

		newComplaint.addComplaint(complaintContent);

		System.out.println(language.getLocalizedMessage("Complaint " + complaintContent + " has been sent with " + urgency + " urgency.",
				"Жалоба " + complaintContent + " была отправлена с уровнем срочности: " + urgency,
				 complaintContent + " шағымы " + urgency + " шұғылдылық деңгейімен жіберілді."));
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

	public List<Course> viewCourses() {
		return courseList;
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
						", курсы='" + courses + '\'' +
						", средний рейтинг=" + getAverageRating() +
						'}';
			case KZ:
				return "Мұғалім{" +
						"id='" + id + '\'' +
						", аты='" + firstname + '\'' +
						", email='" + email + '\'' +
						", мұғалім түрі=" + typeOfTeacher +
						", курстар='" + courses + '\'' +
						", орташа рейтинг=" + getAverageRating() +
						'}';
			default:
				return "Teacher{" +
						"id='" + id + '\'' +
						", name='" + firstname + '\'' +
						", email='" + email + '\'' +
						", typeOfTeacher=" + typeOfTeacher +
						", courses='" + courses + '\'' +
						", averageRating=" + getAverageRating() +
						'}';
		}
	}

}


