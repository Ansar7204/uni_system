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
		// TODO implement me
		return "";	
	}

	
	public String manageCourses() {
		// TODO implement me
		return "";	
	}

	public String viewStudents() {
		// TODO implement me
		return "";	
	}

	
	public void putMarks() {
		// TODO implement me	
	}

	
	public String sendComplaint() {
		// TODO implement me
		return "";	
	}

	@Override
	public int calculateHIndex() {
		return 0;
	}

	@Override
	public ResearchPaper printPapers(ResearchPaper parameter) {
		return null;
	}
}

