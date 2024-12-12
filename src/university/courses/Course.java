package university.courses;

import university.users.Student;
import university.users.Teacher;

//vvv
public class Course
{

	private String id;
	public String name;
	public String majorRequirement;
	public String minorRequirement;
	public String elective;
	public Teacher teachers;
	public Student students;

	public Course(){
		super();
	}


	
	public void addTeacher(Teacher parameter2) {
		// TODO implement me	
	}

	
	public void addStudent(Student parameter) {
		// TODO implement me	
	}

	
	public String toString() {
		// TODO implement me
		return "";	
	}
	
}

