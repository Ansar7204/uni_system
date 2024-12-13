package university.users;

import university.communication.Complaints;
import university.communication.Message;
import university.communication.UrgencyLevel;
import university.courses.Course;
import university.courses.Mark;
import university.research.ResearchPaper;
import university.research.ResearchProject;
import university.research.Researcher;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class Teacher extends Employee implements Researcher {

	private List<Message> receivedMessages;
	public TeacherTypes typeOfTeacher;
	private String courses;
	public List<Course> courseList;
	private List<Integer> ratings;

	public Teacher(String id, String name, String surName, String email, String password, DepartmentsOfEmployees department, int salary, TeacherTypes typeOfTeacher, String courses, List<Integer> ratings) {
		super(id, name, surName, email, password, department, salary);
        this.ratings = ratings;
        this.receivedMessages = new ArrayList<>();
		this.typeOfTeacher = typeOfTeacher;
		this.courseList = new ArrayList<>();
	}

	public void addRating(int rating) {
		ratings.add(rating);
	}

	// Method to calculate the average rating
	public double getAverageRating() {
		if (ratings.isEmpty()) {
			return 0.0; // No ratings yet
		}
		double sum = 0;
		for (int rating : ratings) {
			sum += rating;
		}
		return sum / ratings.size();
	}

	public List<Integer> getRatings() {
		return ratings;
	}


	public List<Course> viewCourses() {
		return courseList;
	}



	public String viewStudent(Student student) {
		return student.toString();
	}


	public void putMarks(Student student, Course course, double firstAttestation, double secondAttestation, double finalExam) {
		// Check if the student is registered for the course
		if (!student.getRegisteredCourses().contains(course)) {
			System.out.println("Student is not registered for the course: " + course.getCourseName());
			return;
		}

		// Create the Mark object with the provided details
		Mark mark = new Mark(course, firstAttestation, secondAttestation, finalExam);

		// Add the Mark to the student's Transcript
		student.getTranscript().addCourseMark(course, mark);

		// Optionally, you can print a confirmation message
		System.out.println("Marks have been successfully added for student " + student.getName() + " in the course " + course.getCourseName());
	}




	public String sendComplaint(UrgencyLevel urgency, String complaintContent,Student student) {
		// Create a new Complaint instance for the given teacher and student, with the appropriate urgency level
		Student studentGettingComplaint = student;
		Complaints newComplaint = new Complaints(urgency, this, studentGettingComplaint, false); // Assuming `this` is the teacher

		// Add the complaint content to the list of all complaints
		newComplaint.addComplaint(complaintContent);

		// Log the complaint or save it in a database (assuming you have some manager to handle storage)
		// DatabaseManager.getInstance().addComplaint(newComplaint); // Uncomment if you have a database manager

		// Output message to confirm that the complaint was sent
		System.out.println("Complaint sent: " + complaintContent);

		return "Complaint has been sent with " + urgency + " urgency.";
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

	public String getTypeOfTeacher() {
		return typeOfTeacher.toString();
	}

	public String getRole() {
		return "Teacher " + "of type " + getTypeOfTeacher();
	}

	public String toString() {
		return "Teacher{" +
				"id='" + id + '\'' +
				", name='" + name + '\'' +
				", email='" + email + '\'' +
				", typeOfTeacher=" + typeOfTeacher +
				", courses='" + courses + '\'' +
				", averageRating=" + getAverageRating();
	}
}

