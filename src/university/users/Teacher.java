package university.users;

import university.communication.Message;
import university.communication.UrgencyLevel;
import university.research.ResearchPaper;
import university.research.ResearchProject;
import university.research.Researcher;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class Teacher extends Employee implements Researcher {
	private String id;
	private String name;
	private String email;
	private String password;
	private List<Message> receivedMessages;
	public TeacherTypes typeOfTeacher;
	private String courses;

	public Teacher(String id, String name, String email, String password, TeacherTypes typeOfTeacher, String courses) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.receivedMessages = new ArrayList<>();
		this.typeOfTeacher = typeOfTeacher;
		this.courses = courses;
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

	public String getRole() {
		return "";
	}


	public String viewCourses() {
		return "";
	}

	
	public String manageCourses() {
		return "";
	}

	public String viewStudents(Student s) {
		return s.toString();
	}

	
	public void putMarks() {
	}

	
	public String sendComplaint(UrgencyLevel urgency) {
		return "Complaint has been sent!";
	}

	@Override
	public void printPapers(Comparator<ResearchPaper> comparator) {

	}

	public int calculateHIndex() {
		return 0;
	}

	@Override
	public List<ResearchProject> getResearchProjects() {
		return List.of();
	}

	@Override
	public void addResearchProject(ResearchProject project) {

	}

	@Override
	public List<ResearchPaper> getResearchPapers() {
		return List.of();
	}

	@Override
	public void addResearchPaper(ResearchPaper paper) {

	}

	public ResearchPaper printPapers(ResearchPaper parameter) {
		return null;
	}
}

