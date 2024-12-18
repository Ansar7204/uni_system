package university.communication;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Log implements Serializable {
    private static final long serialVersionUID = 1L;

    private String userName; // Name of the user who performed the action
    private String activity; // Description of the activity
    private LocalDateTime timestamp; // Time of the activity
    private String activityType; // Optional: Type of activity (e.g., LOGIN, VIEW_COURSES, LOGOUT)

    // Constructor
    public Log(String userName, String activity, String activityType) {
        this.userName = userName;
        this.activity = activity;
        this.activityType = activityType;
        this.timestamp = LocalDateTime.now();
    }

    // Getters
    public String getUserName() {
        return userName;
    }

    public String getActivity() {
        return activity;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getActivityType() {
        return activityType;
    }

    // toString for displaying log info
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return "Log{" +
                "userName='" + userName + '\'' +
                ", activity='" + activity + '\'' +
                ", activityType='" + activityType + '\'' +
                ", timestamp=" + timestamp.format(formatter) +
                '}';
    }
}

