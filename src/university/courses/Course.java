package university.courses;

import university.users.Student;
import university.users.Teacher;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Course implements Serializable {
	public String courseID;
	public String courseName;
	public List<Course> majorRequirement;
	public List<Course> minorRequirement;
	public String elective;
	public List<Teacher> courseTeachers;
	public List<Student> enrolledStudents;
	public int credits;

	public Course(String courseID, String courseName, List<Course> majorRequirement, List<Course> minorRequirement, String elective, int credits) {
		this.courseID = courseID;
		this.courseName = courseName;
		this.majorRequirement = majorRequirement;
		this.minorRequirement = minorRequirement;
		this.elective = elective;
		this.courseTeachers = new ArrayList<>();
		this.enrolledStudents = new ArrayList<>();
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
				"Enrolled Students: " + enrolledStudents;

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

	public List<Course> getMajorRequirements() {
		return majorRequirement;
	}

	public List<Course> getMinorRequirements() {
		return minorRequirement;
	}

	// Print Methods
	public void printMajorRequirements() {
		if (majorRequirement.isEmpty()) {
			System.out.println("No major requirements.");
		} else {
			System.out.println("Major Requirements:");
			for (Course course : majorRequirement) {
				System.out.println("- " + course.getCourseName());
			}
		}
	}

	public void printMinorRequirements() {
		if (minorRequirement.isEmpty()) {
			System.out.println("No minor requirements.");
		} else {
			System.out.println("Minor Requirements:");
			for (Course course : minorRequirement) {
				System.out.println("- " + course.getCourseName());
			}
		}
	}

	// Add Methods
	public void addMajorRequirement(Course course) {
		if (!majorRequirement.contains(course)) {
			majorRequirement.add(course);
			System.out.println("Course " + course.getCourseName() + " added to major requirements.");
		} else {
			System.out.println("Course " + course.getCourseName() + " is already in the major requirements.");
		}
	}

	public void addMinorRequirement(Course course) {
		if (!minorRequirement.contains(course)) {
			minorRequirement.add(course);
			System.out.println("Course " + course.getCourseName() + " added to minor requirements.");
		} else {
			System.out.println("Course " + course.getCourseName() + " is already in the minor requirements.");
		}
	}

	// Remove Methods
	public void removeMajorRequirement(Course course) {
		if (majorRequirement.contains(course)) {
			majorRequirement.remove(course);
			System.out.println("Course " + course.getCourseName() + " removed from major requirements.");
		} else {
			System.out.println("Course " + course.getCourseName() + " is not in the major requirements.");
		}
	}

	public void removeMinorRequirement(Course course) {
		if (minorRequirement.contains(course)) {
			minorRequirement.remove(course);
			System.out.println("Course " + course.getCourseName() + " removed from minor requirements.");
		} else {
			System.out.println("Course " + course.getCourseName() + " is not in the minor requirements.");
		}
	}

	// Update Methods
	public void updateMajorRequirement(Course oldCourse, Course newCourse) {
		if (majorRequirement.contains(oldCourse)) {
			int index = majorRequirement.indexOf(oldCourse);
			majorRequirement.set(index, newCourse);
			System.out.println("Major requirement course updated to: " + newCourse.getCourseName());
		} else {
			System.out.println("Course " + oldCourse.getCourseName() + " is not in the major requirements.");
		}
	}

	public void updateMinorRequirement(Course oldCourse, Course newCourse) {
		if (minorRequirement.contains(oldCourse)) {
			int index = minorRequirement.indexOf(oldCourse);
			minorRequirement.set(index, newCourse);
			System.out.println("Minor requirement course updated to: " + newCourse.getCourseName());
		} else {
			System.out.println("Course " + oldCourse.getCourseName() + " is not in the minor requirements.");
		}
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


}

