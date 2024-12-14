package university.users;

import university.communication.Language;
import university.communication.Languages;
import university.communication.News;
import university.courses.*;
import university.database.DatabaseManager;
import university.research.ResearchPaper;
import university.research.ResearchProject;
import university.research.Researcher;

import java.io.File;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;
public class Student extends User {

	private String studentId;
	private School school;
	private Transcript transcript;
	private StudentOrganization organizationMembership;
	private int yearOfStudy;
	private List<Course> registeredCourses;
	private List<ResearchPaper> diplomaProjects;
	public List<Files> files;


	public Student(String studentID, String firstname, String surname, String email, String password, School school, Transcript transcript, StudentOrganization organizationMembership, int yearOfStudy) {
		 super(studentID, firstname, surname, email, password);
		 this.school = school;
		 this.transcript = transcript;
		 this.organizationMembership = organizationMembership;
		 this.yearOfStudy = yearOfStudy;
		 this.registeredCourses = new ArrayList<Course>();
		 this.newsList = new ArrayList<News>();
		this.diplomaProjects = new ArrayList<>();
		this.files = loadFilesFromDatabase() ;
	}
	private boolean isTeacherRelatedToStudent(Teacher teacher) {
		for (Course course : registeredCourses) {
			if (course.getCourseTeachers().contains(teacher)) {
				return true;
			}
		}
		return false;
	}

	private List<Files> loadFilesFromDatabase() {
		DatabaseManager dbManager = DatabaseManager.getInstance();
		List<Files> studentFiles = new ArrayList<>();

		// Loop through all files in DatabaseManager to find those owned by this student's teachers
		for (Files file : dbManager.getAllFolders()) {
			if (file.getTeacher() != null && isTeacherRelatedToStudent(file.getTeacher())) {
				studentFiles.add(file);
			}
		}

		return studentFiles;
	}
	public void refreshFiles() {
		this.files = loadFilesFromDatabase();
	}

	public void addDiplomaProject(Researcher researcher, ResearchPaper paper) {
		Language language = Language.getInstance();
		if (!researcher.getPublications().contains(paper)) {
			throw new IllegalArgumentException("The research paper is not published by the researcher.");
		}
		diplomaProjects.add(paper);
		System.out.println(language.getLocalizedMessage("Added research paper '" + paper.getTitle() + "' to the diploma projects of " + getFirstName() + " " + getSurname(),
				"Добавлена научная работа '" + paper.getTitle() + "' в дипломные проекты студента " + getFirstName() + " " + getSurname(),
				"'" + paper.getTitle() + "' атты ғылыми жұмыс " + getFirstName() + " " + getSurname() + " студентінің дипломдық жобаларына қосылды"
		));
	}

	public String viewCourses() {
		StringBuilder coursesList = new StringBuilder("Registered courses:\n");
		for (Course course : registeredCourses) {
			coursesList.append(course.getCourseName()).append("\n");
		}
		return coursesList.toString();
	}

	public String registerForCourses(Course Course) {
		Language language = Language.getInstance();
		Languages currentLanguage = language.getCurrentLanguage();
		StringBuilder coursesList = new StringBuilder();
		for (Course course : registeredCourses) {
			coursesList.append(course.getCourseName()).append("\n");
			if (course.getCourseName().equals(Course.getCourseName())) {
				switch (currentLanguage) {
					case RU:
						return "Вы уже зарегестрированы на курсы: " + course.getCourseName();
					default:
						return "You are already registered for the course: " + course.getCourseName();
					case KZ:
						return " Сіз курстарға тіркелгенсіз" + course.getCourseName();
				}
			}
			else {
				registeredCourses.add(Course);
				Course.addStudent(this);
					switch (currentLanguage) {
						default: return "Successfully registered for the course: " + Course.getCourseName();
						case RU: return "Успешно зарегестрированы на курс: " + course.getCourseName();
						case KZ: return "Курсқа сәтті тіркелдіңіз: " + course.getCourseName();
					}
			}
		}
		return coursesList.toString();
	}

	public String viewTeacher(String courseName) {
		Language language = Language.getInstance();
		Languages currentLanguage = language.getCurrentLanguage();
		for (Course course : registeredCourses) {
			if (course.getCourseName().equals(courseName)) {
				List<Teacher> teachers = course.getCourseTeachers();
				if (teachers.isEmpty()) {
					switch (currentLanguage) {
						default:
							return "No teachers assigned for " + courseName;
						case RU:
							return "Не учителей: " + course.getCourseName();
						case KZ:
							return "Мүғалім табылмады: " + course.getCourseName();
					}
				}

				StringBuilder teacherNames = new StringBuilder("Teachers for " + courseName + ": ");
				for (Teacher teacher : teachers) {
					teacherNames.append(teacher.getFirstName()).append(", ");
				}
				return teacherNames.substring(0, teacherNames.length() - 2);
			}
		}
		switch (currentLanguage) {
			default:
				return "You are not registered for the course: " + courseName;
			case RU:
				return "Вы не зарегестрированы на курс " + courseName;
			case KZ:
				return "Сіз курсқа тіркелмегенсіз" + courseName;
		}
	}


	public String viewMarks(String courseName) {
		Language language = Language.getInstance();
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
	public Transcript getTranscript() {
		return transcript;
	}

	public String rateTeacher(Teacher teacher, int rating) {
		if (rating < 0 || rating > 10) {
			return "Invalid rating. Please provide a rating between 0 and 10.";
		}

		teacher.addRating(rating);

		return "Rated teacher " + teacher.getFirstName() + " with " + rating + " points. Average rating: " + teacher.getAverageRating();
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
		for (Files file : files) {
			fileList.append(file.getNameOfFile()).append("\n");
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
		System.out.println("Notification for " + getFirstName() + ": New paper published - " + paper.getTitle());
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

	public Collection<Course> getRegisteredCourses() {
		return registeredCourses;
	}
}

