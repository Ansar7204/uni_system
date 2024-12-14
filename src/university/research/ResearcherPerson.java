package university.research;

import university.courses.School;
import university.users.DepartmentsOfEmployees;
import university.users.Employee;

import java.util.Comparator;
import java.util.List;


public class ResearcherPerson extends Researcher {
	public School school;
	public ResearchPaper researchPapers;
	public ResearchProject researchProject;

	public ResearcherPerson(String id, String firstname, String surname, String email, String password, School school, int experienceYear) {
		super(id, firstname, surname, email, password, school, experienceYear );
	}


	public String getRole() {
		return "";
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
}

