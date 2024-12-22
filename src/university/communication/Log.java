package university.communication;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Log implements Serializable {
    private static final long serialVersionUID = 1L;

    private String userName;
    private LocalDateTime timestamp;
    private String activityType;

    public Log(String userName, String activityType) {
        this.userName = userName;
        this.activityType = activityType;
        this.timestamp = LocalDateTime.now();
    }

    public String getUserName() {
        return userName;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getActivityType() {
        return activityType;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return "Log{" +
                "userName='" + userName + '\'' +
                ", activityType='" + activityType + '\'' +
                ", timestamp=" + timestamp.format(formatter) +
                '}';
    }
}