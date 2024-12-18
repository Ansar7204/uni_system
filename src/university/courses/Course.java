package university.courses;

import university.users.Student;
import university.users.Teacher;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Course implements Serializable {
	public String courseID;
	public String courseName;
	public String majorRequirement;
	public String minorRequirement;
	public String elective;
	public List<Teacher> courseTeachers;
	public List<Student> enrolledStudents;
	public List<Lesson> lessonsOfCourse;
	public int credits;


	public Course(String courseID, String courseName, String majorRequirement, String minorRequirement, String elective,int credits) {
		this.courseID = courseID;
		this.courseName = courseName;
		this.majorRequirement = majorRequirement;
		this.minorRequirement = minorRequirement;
		this.elective = elective;
		this.courseTeachers = new ArrayList<>();
		this.enrolledStudents = new ArrayList<>();
		this.lessonsOfCourse = new ArrayList<>();
		this.credits = credits;
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


	public String toString() {
		return "Course ID: " + courseID + "\n" +
				"Course Name: " + courseName + "\n" +
				"Major Requirement: " + majorRequirement + "\n" +
				"Minor Requirement: " + minorRequirement + "\n" +
				"Elective: " + elective + "\n" +
				"Teachers: " + courseTeachers + "\n" +
				"Enrolled Students: " + enrolledStudents+ "\n" +
				"Lessons of Course: " + lessonsOfCourse;

	}
	public int getCredits() {
		return credits;
	}

	public void setCredits(int credits) {
		this.credits = credits;
	}

	public String getCourseID() {
		return courseID;
	}

	public void setCourseID(String courseID) {
		this.courseID = courseID;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getMajorRequirement() {
		return majorRequirement;
	}

	public void setMajorRequirement(String majorRequirement) {
		this.majorRequirement = majorRequirement;
	}

	public String getMinorRequirement() {
		return minorRequirement;
	}

	public void setMinorRequirement(String minorRequirement) {
		this.minorRequirement = minorRequirement;
	}

	public String getElective() {
		return elective;
	}

	public void setElective(String elective) {
		this.elective = elective;
	}

	public List<Teacher> getCourseTeachers() {
		return courseTeachers;
	}


	public void setCourseTeachers(List<Teacher> courseTeachers) {
		this.courseTeachers = courseTeachers;
	}

	public List<Student> getEnrolledStudents() {
		return enrolledStudents;
	}

	public void setEnrolledStudents(List<Student> enrolledStudents) {
		this.enrolledStudents = enrolledStudents;
	}

	public List<Lesson> getLessonsOfCourse() {
		return lessonsOfCourse;
	}

	public void setLessonsOfCourse(List<Lesson> lessonsOfCourse) {
		this.lessonsOfCourse = lessonsOfCourse;
	}

}

