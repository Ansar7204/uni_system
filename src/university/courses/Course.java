package university.courses;

import university.users.Student;
import university.users.Teacher;

import java.util.ArrayList;
import java.util.List;

public class Course {
	private String courseID;
	public String courseName;
	public String majorRequirement;
	public String minorRequirement;
	public String elective;
	public List<Teacher> courseTeachers;
	public List<Student> enrolledStudents;

	public Course(String courseID, String courseName, String majorRequirement, String minorRequirement, String elective) {
		this.courseID = courseID;
		this.courseName = courseName;
		this.majorRequirement = majorRequirement;
		this.minorRequirement = minorRequirement;
		this.elective = elective;
		this.courseTeachers = new ArrayList<>();
		this.enrolledStudents = new ArrayList<>();
	}


	
	public void assignTeacher(Teacher teacher) {
		if (!courseTeachers.contains(teacher)) {
			courseTeachers.add(teacher);
		}
	}

	public void assignTeachers(List<Teacher> teachers) {
		for (Teacher teacher : teachers) {
			assignTeacher(teacher);
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

	public List<Teacher> getCourseTeachers() {
		return courseTeachers;
	}


	public List<Student> getEnrolledStudents() {
		return enrolledStudents;
	}


	public String toString() {
		return "Course ID: " + courseID + "\n" +
				"Course Name: " + courseName + "\n" +
				"Major Requirement: " + majorRequirement + "\n" +
				"Minor Requirement: " + minorRequirement + "\n" +
				"Elective: " + elective + "\n" +
				"Teachers: " + courseTeachers + "\n" +
				"Enrolled Students: " + enrolledStudents;
	}
}

