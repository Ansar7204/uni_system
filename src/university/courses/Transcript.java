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

	public void addCourseMark(Course course, Mark mark) {
		courseMarks.put(course, mark);
	}


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

	public Mark getMarks(Course course) {
		return courseMarks.get(course);
	}
	public Collection<Mark> getMarks() {
		return courseMarks.values();
	}




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

