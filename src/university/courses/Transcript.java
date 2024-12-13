package university.courses;

import java.util.*;

import java.util.HashMap;
import java.util.Map;

import java.util.HashMap;
import java.util.Map;

public class Transcript {
	private Map<Course, Mark> courseMarks;

	public Transcript() {
		this.courseMarks = new HashMap<>();
	}

	// Adds a Mark object associated with a specific Course
	public void addCourseMark(Course course, Mark mark) {
		courseMarks.put(course, mark);
	}

	// Calculates the overall GPA based on all courses in the transcript
	public double calculateGPA() {
		if (courseMarks.isEmpty()) {
			return 0.0;
		}

		double totalGpa = 0.0;
		for (Mark mark : courseMarks.values()) {
			totalGpa += mark.getGpaValue();
		}

		return totalGpa / courseMarks.size();
	}

	// Retrieves the Mark object for a specific Course
	public Mark getMarks(Course course) {
		return courseMarks.get(course);
	}
	public Collection<Mark> getMarks() {
		return courseMarks.values();
	}



	// Displays the transcript as a formatted string
	public String viewTranscript() {
		StringBuilder sb = new StringBuilder("Transcript:\n");
		for (Map.Entry<Course, Mark> entry : courseMarks.entrySet()) {
			sb.append(entry.getValue().toString()).append("\n");
		}
		sb.append("Overall GPA: ").append(String.format("%.2f", calculateGPA()));
		return sb.toString();
	}

	@Override
	public String toString() {
		return viewTranscript();
	}
}

