package university.courses;



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

	/**
	 * Provides detailed information about the lesson.
	 *
	 * @return A string with the lesson details.
	 */
	public String getDetails() {
		return "Type: " + lessonType +
				", Topic: " + lessonTopic +
				", Name: " + lessonName +
				", Time: " + lessonTime +
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
				'}';
	}
}


