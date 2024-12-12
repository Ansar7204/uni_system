package university.courses;

import university.users.Student;
import university.users.Teacher;

import java.util.List;

public class Course {
	private String courseID;
	public String courseName;
	public String majorRequirement;
	public String minorRequirement;
	public String elective;
	public List<Teacher> courseTeachers;
	public List<Student> enrolledStudents;

	public Course(){
		super();
	}


	
	public void assignTeacher(Teacher teacher) {
		if (!courseTeachers.contains(teacher)) {
			courseTeachers.add(teacher);
		}
	}

	public void assignTeachers(List<Teacher> teachers) {
		for (Teacher teachers : teachers) {
			assignTeacher(teachers);
		}
	}


	public void addStudent(Student student) {
		if (!enrolledStudents.contains(student)) {
			enrolledStudents.add(student);
		}
	}

	public void addStudents(List<Student> students) {
		for (Student student : students) {
			addStudent(student);
		}
	}

	public List<Student> getEnrolledStudents() {
		return enrolledStudents;
	}

	
	public String toString() {
		return "";
	}
}

