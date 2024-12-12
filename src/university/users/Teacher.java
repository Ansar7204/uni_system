package university.users;

import university.research.ResearchPaper;
import university.research.Researcher;


public class Teacher extends Employee implements Researcher
{
	private boolean isProfessor;
	private String courses;

	public Teacher(){
		super();
	}

	
	public String viewCourses() {
		return "";
	}

	
	public String manageCourses() {
		return "";
	}

	public String viewStudents() {
		return "";
	}

	
	public void putMarks() {
	}

	
	public String sendComplaint() {
		return "";
	}

	public double calculateHIndex() {
		return 0;
	}

	public ResearchPaper printPapers(ResearchPaper parameter) {
		return null;
	}
}

