package university.courses;

import java.util.HashMap;
import java.util.Map;

public class Transcript {
	private Map<Course, Marks> courseMarks;

	public Transcript() {
		this.courseMarks = new HashMap<>();
	}

	public void addCourseMarks(Course course, Marks marks) {
		courseMarks.put(course, marks);
	}

	public double calculateGPA() {
		if (courseMarks.isEmpty()) {
			return 0.0;
		}

		double totalGpa = 0.0;
		for (Marks marks : courseMarks.values()) {
			totalGpa += marks.getGpaValue();
		}

		return totalGpa / courseMarks.size();
	}

}
