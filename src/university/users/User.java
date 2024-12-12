package university.users;

import university.communication.Message;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;

public abstract class User {
	private String id;
	private String name;
	private String email;

	// List to store received messages
	private List<Message> receivedMessages;

	// Constructor
	public User(String id, String name, String email) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.receivedMessages = new ArrayList<>();
	}

	// Default constructor
	public User() {
		this("", "", "");
	}

	// Getters and Setters
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<Message> getReceivedMessages() {
		return receivedMessages;
	}

	// Login method
	public String login() {
		return "User " + name + " has logged in successfully.";
	}

	// Send a message to another user
	public Message sendMessage(User recipient, Message message) {
		if (recipient == null || message == null) {
			throw new IllegalArgumentException("Recipient or message cannot be null.");
		}
		recipient.receiveMessage(message);
		return message;
	}

	// Receive a message (adds to the receivedMessages list)
	public void receiveMessage(Message message) {
		if (message != null) {
			receivedMessages.add(message);
		}
	}

	// View received messages
	public String viewMessages() {
		if (receivedMessages.isEmpty()) {
			return "No messages.";
		}
		StringBuilder sb = new StringBuilder();
		sb.append("Messages for ").append(name).append(":\n");
		for (Message message : receivedMessages) {
			sb.append("- ").append(message.getContent()).append("\n");
		}
		return sb.toString();
	}

	// View news (placeholder for actual news logic)
	public String viewNews() {
		return "Displaying the latest news...";
	}

	// Abstract method to get the role of the user
	public abstract String getRole();

	// ToString method
	@Override
	public String toString() {
		return "User{id='" + id + "', name='" + name + "', email='" + email + "'}";
	}
}


