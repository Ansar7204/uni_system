package university.communication;

import university.research.ResearchPaper;
import university.research.Researcher;
import university.communication.Journal;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class News {
	public String topic;
	public String content;
	public List<String> comments;
	public boolean isPinned;
	public static int currentYear;

	public News(String topic, String content, List<String> comments, boolean isPinned) {
		this.topic = topic;
		this.content = content;
		this.comments = new ArrayList<>();
		this.isPinned = false;
		this.currentYear = Calendar.getInstance().get(Calendar.YEAR);
	}

	public String getTopic() {
		return topic;
	}

	public String getContent() {
		return content;
	}

	public List<String> getComments() {
		return comments;
	}

	public boolean isPinned() {
		return isPinned;
	}

	public static News generateResearchPaperAnnouncement(Researcher researcher, ResearchPaper paper) {
		String topic = "Research";
		String content = "New research paper published by " + researcher.getFirstName() + " " + researcher.getSurname() + ": \"" + paper.getTitle() + "\"";
		return new News(topic, content, null, true);
	}

	public static News generateTopCitedResearcherOfSchoolAnnouncement() {
		Researcher topResearcher = Journal.printTopCitedResearcherOfSchool(null);
		String topic = "University Highlights";
		String content = "Top-cited researcher of school: " + topResearcher.getSchool().toString() + " is " + topResearcher.getFirstName() + " " + topResearcher.getSurname();
		return new News(topic, content, null, true);
	}

	public static News generateTopCitedResearcherOfYearAnnouncement() {
		Researcher topResearcher = Journal.printTopCitedResearcherOfYear(null, currentYear);
		String topic = "University Highlights";
		String content = "Top-cited researcher of year: " + topResearcher.getSchool().toString() + " is " + topResearcher.getFirstName() + " " + topResearcher.getSurname();
		return new News(topic, content, null, true);
	}

	public void addComment(String commenterName, String comment) {
		comments.add("[" + commenterName + "]: " + comment);
	}

	public void addNews(String topic, String content) {
		this.topic = topic;
		this.content = content;
		this.comments.clear();
	}

	public void deleteNews() {
		this.topic = null;
		this.content = null;
		this.comments.clear();
	}

	public String toString() {
		return "News{" +
				"topic='" + topic + '\'' +
				", content='" + content + '\'' +
				", comments=" + comments +
				'}';
	}
}


