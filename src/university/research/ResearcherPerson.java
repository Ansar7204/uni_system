package university.research;

import university.courses.School;
import university.users.Employee;



public class ResearcherPerson extends Employee implements Researcher
{

	public School school;
	public ResearchPaper researchPapers;
	public ResearchProject researchProject;

	public ResearcherPerson(){
		super();
	}

	@Override
	public double calculateHIndex() {
		return 0;
	}

	@Override
	public ResearchPaper printPapers(ResearchPaper parameter) {
		return null;
	}
}

