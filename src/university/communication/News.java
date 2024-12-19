package university.communication;

import university.research.ResearchPaper;
import university.research.Researcher;
import university.communication.Journal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class News implements Serializable {
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


