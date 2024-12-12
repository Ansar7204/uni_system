package university.users;

import university.research.ResearchPaper;
import university.research.ResearchProject;
import university.research.Researcher;
import university.courses.School;
import university.courses.StudentOrganization;
import university.courses.Transcript;

import java.util.Comparator;
import java.util.List;

public class Student extends User implements Researcher
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

	@Override
	public String getRole() {
		return "";
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

