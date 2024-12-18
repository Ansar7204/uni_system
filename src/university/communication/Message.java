package university.communication;

import university.users.User;

import java.io.Serializable;
import java.util.Date;

import java.time.LocalDateTime;

public class Message implements Serializable {
	private User sender;
	private User recipient;
	private String content;
	private LocalDateTime date;

	public Message(User sender, User recipient, String content) {
		if (sender == null || recipient == null || content == null || content.isEmpty()) {
			throw new IllegalArgumentException("Sender, recipient, and content cannot be null or empty.");
		}
		this.sender = sender;
		this.recipient = recipient;
		this.content = content;
		this.date = LocalDateTime.now();
	}

	public User getSender() {
		return sender;
	}

	public void setSender(User sender) {
		if (sender == null) {
			throw new IllegalArgumentException("Sender cannot be null.");
		}
		this.sender = sender;
	}

	public User getRecipient() {
		return recipient;
	}

	public void setRecipient(User recipient) {
		if (recipient == null) {
			throw new IllegalArgumentException("Recipient cannot be null.");
		}
		this.recipient = recipient;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		if (content == null || content.isEmpty()) {
			throw new IllegalArgumentException("Content cannot be null or empty.");
		}
		this.content = content;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		if (date == null) {
			throw new IllegalArgumentException("Date cannot be null.");
		}
		this.date = date;
	}

	public String toString() {
		return "Message from " + sender.getFirstName() + " " + sender.getSurname() + " to " + recipient.getFirstName() + " " + recipient.getSurname() + " at " + date + ": " + content;
	}
}

