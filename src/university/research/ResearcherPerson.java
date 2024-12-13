package university.research;

import university.courses.School;
import university.users.DepartmentsOfEmployees;
import university.users.Employee;

import java.util.Comparator;
import java.util.List;


public class ResearcherPerson extends Employee implements Researcher {
	public School school;
	public ResearchPaper researchPapers;
	public ResearchProject researchProject;

	public ResearcherPerson(String id, String name, String email, String password, DepartmentsOfEmployees department, int salary) {
		super(id, name, email, password, department, salary);
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

