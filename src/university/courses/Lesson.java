package university.courses;

import java.util.Date;

public class Lesson {
	public LessonTypes lessonType;
	public String lessonTopic;
	public String lessonName;
	public Course course;
	public WeekDays lessonTime;

	public Lesson(LessonTypes lessonType, String lessonTopic, String lessonName, Course course, WeekDays lessonTime) {
		this.lessonType = lessonType;
		this.lessonTopic = lessonTopic;
		this.lessonName = lessonName;
		this.course = course;
		this.lessonTime = lessonTime;
	}

	public String toString() {
		return "Lesson{" +
				"lessonType=" + lessonType +
				", lessonTopic='" + lessonTopic + '\'' +
				", lessonName='" + lessonName + '\'' +
				", course=" + (course != null ? course.courseName : "None") +
				", lessonTime=" + lessonTime +
				'}';
	}

}

