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
		return "";
	}
	

	
	public void registerForCourses() {
	}
	

	
	public String viewTeacher() {
		return "";
	}

	
	public String viewMarks() {
		return "";
	}
	

	public String viewTranscript() {
		return "";
	}

	
	public void rateTeachers() {
	}
	

	public void getTranscript() {
	}
	

	
	public String seeSchedule() {
		return "";
	}
	

	
	public String viewFiles() {
		return "";
	}

	public double calculateHIndex() {
		return 0;
	}

	public ResearchPaper printPapers(ResearchPaper parameter) {
		return null;
	}
}

