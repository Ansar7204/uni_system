package university.users;

import university.research.ResearchPaper;
import university.research.Researcher;
import university.courses.School;
import university.courses.StudentOrganization;
import university.courses.Transcript;

public class Student implements Researcher
{
	public String studentId;
	public School school;
	public Transcript transcript;
	public int gpa;
	public StudentOrganization organizationMembership;
	public int yearOfStudy;
	

	public Student(){
		super();
	}


	public String viewCourses() {
		// TODO implement me
		return "";	
	}
	

	
	public void registerForCourses() {
		// TODO implement me	
	}
	

	
	public String viewTeacher() {
		// TODO implement me
		return "";	
	}

	
	public String viewMarks() {
		// TODO implement me
		return "";	
	}
	

	public String viewTranscript() {
		// TODO implement me
		return "";	
	}

	
	public void rateTeachers() {
		// TODO implement me	
	}
	

	public void getTranscript() {
		// TODO implement me	
	}
	

	
	public String seeSchedule() {
		// TODO implement me
		return "";	
	}
	

	
	public String viewFiles() {
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

