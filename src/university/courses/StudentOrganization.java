package university.courses;

import university.users.Student;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class StudentOrganization implements Serializable {
	private String name;
	private List<Student> members;
	private Student head;

	public StudentOrganization() {
		this.members = new ArrayList<>();
	}

	public StudentOrganization(String name) {
		this.name = name;
		this.members = new ArrayList<>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Student> getMembers() {
		return members;
	}

	public void addMember(Student member) {
		if (!members.contains(member)) {
			members.add(member);
		}
	}

	public void setHead(Student head) {
		this.head = head;
		if (!members.contains(head)) {
			members.add(head);
		}
	}

	public Student getHead() {
		return head;
	}
}


