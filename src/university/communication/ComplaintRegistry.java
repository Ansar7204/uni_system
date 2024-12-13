package university.communication;

import java.util.ArrayList;
import java.util.List;

public class ComplaintRegistry {
    private List<Complaints> allComplaints;

    public ComplaintRegistry() {
        this.allComplaints = new ArrayList<>();
    }

    public void addComplaint(Complaints complaint) {
        allComplaints.add(complaint);
    }

    public Complaints getComplaintByText(String complaintText) {
        for (Complaints complaint : allComplaints) {
            if (complaint.getAllComplaints().contains(complaintText)) {
                return complaint;
            }
        }
        return null; // Complaint not found
    }

    public List<String> getAllUnsignedComplaints() {
        List<String> unsignedComplaints = new ArrayList<>();
        for (Complaints complaint : allComplaints) {
            unsignedComplaints.addAll(complaint.getUnsignedComplaints());
        }
        return unsignedComplaints;
    }
}

