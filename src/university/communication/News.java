package university.communication;

import java.util.ArrayList;
import java.util.List;

public class News {
	private String topic;
	private String content;
	private List<String> comments;

	// Constructor
	public News(String topic, String content) {
		this.topic = topic;
		this.content = content;
		this.comments = new ArrayList<>();
	}

	// Default constructor
	public News() {
		this.comments = new ArrayList<>();
	}

	// Getters
	public String getTopic() {
		return topic;
	}

	public String getContent() {
		return content;
	}

	public List<String> getComments() {
		return comments;
	}

	// Add a comment to the news
	public void addComment(String commenterName, String comment) {
		comments.add("[" + commenterName + "]: " + comment);
	}

	// Add news content
	public void addNews(String topic, String content) {
		this.topic = topic;
		this.content = content;
		this.comments.clear(); // Reset comments for new news
	}

	// Delete all content and comments in the news
	public void deleteNews() {
		this.topic = null;
		this.content = null;
		this.comments.clear();
	}

	@Override
	public String toString() {
		return "News{" +
				"topic='" + topic + '\'' +
				", content='" + content + '\'' +
				", comments=" + comments +
				'}';
	}
}


