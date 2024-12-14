package university.research;

import university.communication.Language;
import university.courses.School;
import university.users.Student;
import university.users.Teacher;
import university.users.User;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class Researcher extends User {
	private School school;
	private int experienceYear;
	private List<ResearchPaper> publications;
	private List<Student> supervisedStudents;
	private List<Teacher> associatedTeachers;

	public Researcher(String id, String firstname, String surname, String email, String password, School school, int experienceYear) {
		super(id, firstname, surname, email, password);
		this.school = school;
		this.experienceYear = experienceYear;
		this.publications = new ArrayList<>();
		this.supervisedStudents = new ArrayList<>();
		this.associatedTeachers = new ArrayList<>();
	}

	public int calculateHIndex() {
		publications.sort(Comparator.comparingInt(ResearchPaper::getNumberOfCitations).reversed());

		int hIndex = 0;

		for (int i = 0; i < publications.size(); i++) {
			if (publications.get(i).getNumberOfCitations() >= i + 1) {
				hIndex = i + 1;
			}
			else {
				break;
			}
		}

		return hIndex;
	}

	public void assignResearchStudent(Student student) {
		int hIndex = calculateHIndex();
		Language languageManager = Language.getInstance();
		if (hIndex < 3) {
			throw new IllegalArgumentException(languageManager.getLocalizedMessage(
					"Cannot assign supervisor with H-index less than 3. Current H-index: " + hIndex,
					"Нельзя назначить научного руководителя с H-индексом менее 3. Текущий H-индекс: " + hIndex,
					"H-индексі 3-тен төмен ғылыми жетекшіні тағайындауға болмайды. Ағымдағы H-индекс: " + hIndex
			));
		}

		supervisedStudents.add(student);
		System.out.println(languageManager.getLocalizedMessage(
				"Student " + student.getFirstName() + " assigned to supervisor " + getFirstName(),
				"Студент " + student.getFirstName() + " назначен научным руководителем " + getFirstName(),
				"Студент " + student.getFirstName() + " " + getFirstName() + " ғылыми жетекші ретінде тағайындалды"
		));
	}

	public void linkTeacher(Teacher teacher) {
		if (!teacher.isProfessor()) {
			throw new IllegalArgumentException("Only professors can be automatically linked as researchers.");
		}
		associatedTeachers.add(teacher);
	}

	public void printPapers(Comparator<ResearchPaper> comparator) {
		publications.sort(comparator);
		publications.forEach(System.out::println);
	}


	public void addResearchPaper(ResearchPaper paper) {
		publications.add(paper);
	}

	public void conductResearch(String topic) {
		System.out.println(getFirstName() + " is conducting research on: " + topic);
	}

	public void publishPaper(String title) {
		ResearchPaper paper = new ResearchPaper(title, getFirstName(), "Content of " + title, 10, new Date());
		publications.add(paper);
		System.out.println(getFirstName() + " has published a paper titled: " + title);
	}

	public void attendConference(String conferenceName) {
		System.out.println(getFirstName() + " is attending the conference: " + conferenceName);
	}


	public boolean canSuperviseMoreStudents(int maxStudents) {
		return supervisedStudents.size() < maxStudents;
	}

	public void sortPublications(Comparator<ResearchPaper> comparator) {
		PublicationSorter sorter = new PublicationSorter(comparator);
		sorter.sort(publications);
	}

	public void addExperienceYear(int years) {
		if (years > 0) {
			this.experienceYear += years;
			System.out.println(getFirstName() + " has gained " + years + " year(s) of experience. Total: " + experienceYear);
		} else {
			System.out.println("Invalid number of years.");
		}
	}

	public String getRole() {
		return "Researcher";
	}

	public School getSchool() {
		return school;
	}

	public void setSchool(School school) {
		this.school = school;
	}

	public int getExperienceYear() {
		return experienceYear;
	}

	public void setExperienceYear(int experienceYear) {
		this.experienceYear = experienceYear;
	}

	public List<ResearchPaper> getPublications() {
		return publications;
	}

	public List<Student> getSupervisedStudents() {
		return supervisedStudents;
	}


}
