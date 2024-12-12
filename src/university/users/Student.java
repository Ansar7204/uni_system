package university.users;

import university.courses.Course;
import university.research.ResearchPaper;
import university.research.ResearchProject;
import university.research.Researcher;
import university.courses.School;
import university.courses.StudentOrganization;
import university.courses.Transcript;

import java.io.File;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;
public class Student extends User implements Researcher {
	private String studentId;
	private School school;
	private Transcript transcript;
	private StudentOrganization organizationMembership;
	private int yearOfStudy;
	private List<Course> registeredCourses;
	private List<File> files;

	public Student(String studentId, School school, Transcript transcript, StudentOrganization organizationMembership, int yearOfStudy) {
		this.studentId = studentId;
		this.school = school;
		this.transcript = transcript;
		this.organizationMembership = organizationMembership;
		this.yearOfStudy = yearOfStudy;
		this.registeredCourses = new ArrayList<Course>();
		this.files = new ArrayList<File>();
	}

	public Student() {
		super();
	}

	// Getters and Setters
	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public School getSchool() {
		return school;
	}

	public void setSchool(School school) {
		this.school = school;
	}

	public StudentOrganization getOrganizationMembership() {
		return organizationMembership;
	}

	public void setOrganizationMembership(StudentOrganization organizationMembership) {
		this.organizationMembership = organizationMembership;
	}

	public int getYearOfStudy() {
		return yearOfStudy;
	}

	public void setYearOfStudy(int yearOfStudy) {
		this.yearOfStudy = yearOfStudy;
	}

	@Override
	public String getRole() {
		return "Student";
	}

	public String viewCourses() {
		if (registeredCourses.isEmpty()) {
			return "You are not registered for any courses.";
		}
		StringBuilder coursesList = new StringBuilder("Registered courses:\n");
		for (Course course : registeredCourses) {
			coursesList.append(course.getCourseName()).append("\n");
		}
		return coursesList.toString();
	}

	public void registerForCourses(Course course) {
		// Check if the student is already registered for the course
		if (registeredCourses.contains(course)) {
			System.out.println("You are already registered for the course: " + course.getCourseName());
			return;
		}
	}

	public String viewTeacher(String courseName) {
		for (Course course : registeredCourses) {
			if (course.getCourseName().equals(courseName)) {
				Teacher teacher = course.getTeacher();
				return "Teacher for " + courseName + ": " + teacher.getName();
			}
		}
		return "You are not registered for the course: " + courseName;
	}

	public String viewMarks(String courseName) {
		for (Mark mark : transcript.getMarks()) {
			if (mark.getCourse().getCourseName().equals(courseName)) {
				return "Your marks for " + courseName + ": " +
						"First Attestation: " + mark.getFirstAttestation() +
						", Second Attestation: " + mark.getSecondAttestation() +
						", Final: " + mark.getFinal() +
						", Total: " + mark.getValue();
			}
		}
		return "No marks found for the course: " + courseName;
	}

	public String viewTranscript() {
		if (transcript.getMarks().isEmpty()) {
			return "No marks available in the transcript.";
		}
		return "Transcript: " + transcript.getMarks().toString();
	}

	public String rateTeacher(Teacher teacher, int rating) {
		if (rating < 0 || rating > 10) {
			return "Invalid rating. Please provide a rating between 0 and 10.";
		}
		return "Rated teacher " + teacher.getName() + " with " + rating + " points.";
	}

	public String seeSchedule() {
		return "";
	}


	public String viewFiles() {
		if (files.isEmpty()) {
			return "No files available for your courses.";
		}
		StringBuilder fileList = new StringBuilder("Files:\n");
		for (File file : files) {
			fileList.append(file.getFileName()).append("\n");
		}
		return fileList.toString();
	}

	@Override
	public void printPapers(Comparator<ResearchPaper> comparator) {

	}

	public int calculateHIndex() {
		return 0;
	}

	@Override
	public List<ResearchProject> getResearchProjects() {
		return List.of();
	}

	@Override
	public void addResearchProject(ResearchProject project) {

	}

	@Override
	public List<ResearchPaper> getResearchPapers() {
		return List.of();
	}

	@Override
	public void addResearchPaper(ResearchPaper paper) {

	}

	public ResearchPaper printPapers(ResearchPaper parameter) {
		return null;
	}
}

