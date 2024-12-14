package university.courses;


import university.users.Student;

import java.util.HashMap;
import java.util.Map;

public class Lesson {
	public LessonTypes lessonType;
	public String lessonTopic;
	public String lessonName;
	public Course course;
	public WeekDays lessonTime;
	public String roomType;
	private Map<Student, Boolean> attendance;

	public Lesson(LessonTypes lessonType, String lessonTopic, String lessonName, Course course, WeekDays lessonTime, String roomType) {
		this.lessonType = lessonType;
		this.lessonTopic = lessonTopic;
		this.lessonName = lessonName;
		this.course = course;
		this.lessonTime = lessonTime;
		this.roomType = roomType;
		this.attendance = new HashMap<>(); // Initialize attendance map
	}

	public String getDetails() {
		return "Type: " + lessonType +
				", Topic: " + lessonTopic +
				", Name: " + lessonName +
				", Time: " + lessonTime +
				", Room: " + roomType +
				", Course: " + (course != null ? course.getCourseName() : "None");
	}

	@Override
	public String toString() {
		return "Lesson{" +
				"lessonType=" + lessonType +
				", lessonTopic='" + lessonTopic + '\'' +
				", lessonName='" + lessonName + '\'' +
				", course=" + (course != null ? course.getCourseName() : "None") +
				", lessonTime=" + lessonTime +
				", roomType='" + roomType + '\'' +
				'}';
	}

	// New method to mark attendance
	public void markAttendance(Student student, boolean isPresent) {
		attendance.put(student, isPresent);
	}

	// New method to check if a specific student attended the lesson
	public boolean checkAttendance(Student student) {
		return attendance.getOrDefault(student, false);
	}

	// New method to print attendance summary
	public String getAttendanceSummary() {
		if (attendance.isEmpty()) {
			return "No attendance records available.";
		}
		StringBuilder summary = new StringBuilder("Attendance Summary:\n");
		for (Map.Entry<Student, Boolean> entry : attendance.entrySet()) {
			summary.append(entry.getKey().getFirstName()) // Assuming Student class has getName()
					.append(" - ")
					.append(entry.getValue() ? "Present" : "Absent")
					.append("\n");
		}
		return summary.toString();
	}
}


