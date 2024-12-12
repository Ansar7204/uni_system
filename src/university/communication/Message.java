package university.communication;

import university.users.User;

import java.util.Date;

import java.time.LocalDateTime;

public class Message {
	private User sender;         // Sender is now of type User
	private User recipient;      // Recipient is now of type User
	private String content;      // The message content
	private LocalDateTime date;  // The date and time when the message was sent

	// Constructor
	public Message(User sender, User recipient, String content) {
		if (sender == null || recipient == null || content == null || content.isEmpty()) {
			throw new IllegalArgumentException("Sender, recipient, and content cannot be null or empty.");
		}
		this.sender = sender;
		this.recipient = recipient;
		this.content = content;
		this.date = LocalDateTime.now(); // Automatically set the current date and time
	}

	// Getters and Setters
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

	@Override
	public String toString() {
		return "Message from " + sender.getName() + " to " + recipient.getName() + " at " + date + ": " + content;
	}
}

