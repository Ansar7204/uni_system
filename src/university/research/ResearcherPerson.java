package university.research;

import university.courses.School;
import university.users.Employee;

import java.util.Comparator;
import java.util.List;


public class ResearcherPerson extends Employee implements Researcher {
	public School school;
	public ResearchPaper researchPapers;
	public ResearchProject researchProject;

	public ResearcherPerson(){
		super();
	}

	@Override
	public String getRole() {
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

