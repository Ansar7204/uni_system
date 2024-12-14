package university.research;


import university.users.User;
import java.util.ArrayList;
import java.util.List;


public class ResearchProject {
	public String topic;
	public List<ResearchPaper> publishedPapers;
	public List<Researcher> participants;

	public ResearchProject(String topic) {
		this.topic = topic;
		this.publishedPapers = new ArrayList<>();
		this.participants = new ArrayList<>();
	}


	public void addParticipant(Object participant) {
		if (!(participant instanceof Researcher)) {
			throw new IllegalArgumentException("Only researchers can join a research project.");
		}
		participants.add((Researcher) participant);
		System.out.println(((Researcher) participant).getFirstName()+ ((Researcher) participant).getSurname()  + " has joined the research project: " + topic);
	}

	public void addResearchPaper(ResearchPaper paper) {
		publishedPapers.add(paper);
	}

	public List<ResearchPaper> getPublishedPapers() {
		return publishedPapers;
	}

	public List<Researcher> getParticipants() {
		return participants;
	}

	public String getTopic() {
		return topic;
	}
}

