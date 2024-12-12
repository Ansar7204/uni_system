package university.research;


import java.util.Comparator;
import java.util.List;

public interface Researcher {

	void printPapers(Comparator<ResearchPaper> comparator);

	int calculateHIndex();

	List<ResearchProject> getResearchProjects();

	void addResearchProject(ResearchProject project);

	List<ResearchPaper> getResearchPapers();

	void addResearchPaper(ResearchPaper paper);

}
