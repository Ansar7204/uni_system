package university.communication;

import university.database.DatabaseManager;
import university.users.Student;
import university.users.Teacher;

import java.util.ArrayList;
import java.util.List;

public class Complaint {

    public UrgencyLevel urgencyLevel;
    public Teacher teacherWhoComplained;
    public Student studentGettingComplaint;
    public boolean signedByManager;
    public String complaintText;


    public Complaint(UrgencyLevel urgencyLevel, Teacher teacherWhoComplained, Student studentGettingComplaint, boolean signedByManager,String complaintText) {
        this.urgencyLevel = urgencyLevel;
        this.teacherWhoComplained = teacherWhoComplained;
        this.studentGettingComplaint = studentGettingComplaint;
        this.signedByManager = signedByManager;
        this.complaintText = complaintText;

    }


    public void markComplaintAsSigned(String complaintText) {
        this.signedByManager = true;
    }
}
