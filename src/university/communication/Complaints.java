package university.communication;

import university.database.DatabaseManager;
import university.users.Student;
import university.users.Teacher;

import java.util.ArrayList;
import java.util.List;

public class Complaints {

    public UrgencyLevel urgencyLevel;
    public Teacher teacherWhoComplained;
    public Student studentGettingComplaint;
    public boolean signedByManager;
    public List<String> allComplaints;
    public List<String> signedComplaints;
    public List<String> unsignedComplaints;

    public Complaints(UrgencyLevel urgencyLevel, Teacher teacherWhoComplained, Student studentGettingComplaint, boolean signedByManager) {
        this.urgencyLevel = urgencyLevel;
        this.teacherWhoComplained = teacherWhoComplained;
        this.studentGettingComplaint = studentGettingComplaint;
        this.signedByManager = signedByManager;
        allComplaints = new ArrayList<>();
        signedComplaints = new ArrayList<>();
        unsignedComplaints = new ArrayList<>();
    }

    public void addComplaint(String complaint) {
        allComplaints.add(complaint);
        if (!signedByManager) {
            unsignedComplaints.add(complaint);
        }
    }

    public void markComplaintAsSigned(String complaint) {
        if (unsignedComplaints.remove(complaint)) {
            signedComplaints.add(complaint);
        }
    }

    public List<String> getUnsignedComplaints() {
        return unsignedComplaints;
    }

    public List<String> getSignedComplaints() {
        return signedComplaints;
    }

    public List<String> getAllComplaints() {
        return allComplaints;
    }

}
