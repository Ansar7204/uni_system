package university.users;

import university.communication.Message;
import university.communication.UrgencyLevel;
import university.courses.Course;
import university.research.ResearchPaper;
import university.research.ResearchProject;
import university.research.Researcher;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class Teacher extends Employee implements Researcher {

	private List<Message> receivedMessages;
	public TeacherTypes typeOfTeacher;
	private String courses;
	public List<Course> courseList;

	public Teacher(String id, String name, String email, String password, DepartmentsOfEmployees department, int salary, TeacherTypes typeOfTeacher, String courses) {
		super(id, name, email, password, department, salary);
		this.receivedMessages = new ArrayList<>();
		this.typeOfTeacher = typeOfTeacher;
		this.courseList = new ArrayList<>();
	}


	public List<Course> viewCourses() {
		return courseList;
	}

	
	public String manageCourses() {
		return "";
	}

	public String viewStudent(Student student) {
		return student.toString();
	}

	
	public void putMarks() {
	}

	
	public String sendComplaint(UrgencyLevel urgency) {
		return "Complaint has been sent!";
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

	public String getRole() {
		return "Teacher " + "of type " + getTypeOfTeacher();
	}

	public String toString() {
		return "Teacher{" +
				"id='" + id + '\'' +
				", name='" + name + '\'' +
				", email='" + email + '\'' +
				", typeOfTeacher=" + typeOfTeacher +
				", courses='" + courses + '\'' +
				", receivedMessages=" + (receivedMessages);
	}
}

