package university.users;

import university.communication.Complaints;
import university.communication.Message;
import university.communication.UrgencyLevel;
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
	private List<Integer> ratings;
	public Researcher researcherProfile;

	public Teacher(String id, String name, String surName, String email, String password, DepartmentsOfEmployees department, int salary, TeacherTypes typeOfTeacher, String courses, List<Integer> ratings) {
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
		if (!student.getRegisteredCourses().contains(course)) {
			System.out.println("Student is not registered for the course: " + course.getCourseName());
			return;
		}

		Mark mark = new Mark(course, firstAttestation, secondAttestation, finalExam);
		student.getTranscript().addCourseMark(course, mark);

		System.out.println("Marks have been successfully added for student " + student.getFirstName() + " " + student.getSurname() + " in the course " + course.getCourseName());
	}




	public String sendComplaint(UrgencyLevel urgency, String complaintContent,Student student) {
		Student studentGettingComplaint = student;
		Complaints newComplaint = new Complaints(urgency, this, studentGettingComplaint, false);

		newComplaint.addComplaint(complaintContent);

		return "Complaint " + complaintContent + " has been sent with " + urgency + " urgency.";
	}

	public boolean isProfessor() {
		return this.typeOfTeacher.equals("Professor");
	}


	public void printPapers(Comparator<ResearchPaper> comparator) {

	}

	public int calculateHIndex() {
		return 0;
	}

	public List<ResearchProject> getResearchProjects() {
		return List.of();
	}

	public void addResearchProject(ResearchProject project) {

	}

	public List<ResearchPaper> getResearchPapers() {
		return List.of();
	}

	public void addResearchPaper(ResearchPaper paper) {

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
		return "Teacher " + "of type " + getTypeOfTeacher();
	}

	public String toString() {
		return "Teacher{" +
				"id='" + id + '\'' +
				", name='" + firstname + '\'' +
				", email='" + email + '\'' +
				", typeOfTeacher=" + typeOfTeacher +
				", courses='" + courses + '\'' +
				", averageRating=" + getAverageRating();
	}
}

