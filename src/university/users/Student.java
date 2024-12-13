package university.users;

import university.courses.*;
import university.research.ResearchPaper;
import university.research.ResearchProject;
import university.research.Researcher;

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


	public Student(String studentID, String name, String email, String password, School school, Transcript transcript, StudentOrganization organizationMembership, int yearOfStudy) {
		 super(studentID, name, email, password);
		 this.school = school;
		 this.transcript = transcript;
		 this.organizationMembership = organizationMembership;
		 this.yearOfStudy = yearOfStudy;
		 this.registeredCourses = new ArrayList<Course>();
		 this.files = new ArrayList<File>();
	}

	public String viewCourses() {
		StringBuilder coursesList = new StringBuilder("Registered courses:\n");
		for (Course course : registeredCourses) {
			coursesList.append(course.getCourseName()).append("\n");
		}
		return coursesList.toString();
	}

	public String registerForCourses(Course Course) {
		StringBuilder coursesList = new StringBuilder();
		for (Course course : registeredCourses) {
			coursesList.append(course.getCourseName()).append("\n");
			if (course.getCourseName().equals(Course.getCourseName())) {
				return "You are already registered for the course: " + course.getCourseName();
			}
			else {
				registeredCourses.add(Course);
				Course.addStudent(this);
				return "Successfully registered for the course: " + Course.getCourseName();
			}
		}
		return coursesList.toString();
	}

	public String viewTeacher(String courseName) {
		for (Course course : registeredCourses) {
			if (course.getCourseName().equals(courseName)) {
				List <Teacher> teacher = course.getCourseTeachers();
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
		if (registeredCourses.isEmpty()) {
			return "No schedule available. You are not registered for any courses.";
		}
		StringBuilder schedule = new StringBuilder("Schedule:\n");
		for (Course course : registeredCourses) {
			schedule.append("Course: ").append(course.getCourseName()).append("\n");
			List<Lesson> lessons = course.getLessonsOfCourse();
			if (lessons.isEmpty()) {
				schedule.append("  No lessons scheduled for this course.\n");
			} else {
				for (Lesson lesson : lessons) {
					schedule.append("  Lesson: ").append(lesson.getDetails()).append("\n");
				}
			}
		}
		return schedule.toString();
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

	public void printPapers(Comparator<ResearchPaper> comparator) {
	}

	public int calculateHIndex() {
		return 0;
	}
	public List<ResearchProject> getResearchProjects() {
		return List.of();
	}
	public void addResearchProject(ResearchProject project) {

	}
	public List<ResearchPaper> getResearchPapers() {
		return List.of();
	}
	public void addResearchPaper(ResearchPaper paper) {
	}

	public ResearchPaper printPapers(ResearchPaper parameter) {
		return null;
	}

	public void notify(ResearchPaper paper) {
		System.out.println("Notification for " + getName() + ": New paper published - " + paper.getTitle());
	}

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

	public String getRole() {
		return "Student";
	}
}

