package university.users;

import university.communication.UrgencyLevel;
import university.research.ResearchPaper;
import university.research.ResearchProject;
import university.research.Researcher;

import java.util.Comparator;
import java.util.List;


public class Teacher extends Employee implements Researcher {
	private boolean isProfessor;
	private String courses;

	public Teacher(){
		super();
	}

	@Override
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

