package university.users;

import university.communication.Complaints;
import java.util.List;

public class Manager extends Employee{
    public ManagerTypes managerType;

    public Manager(String id, String name, String email, String password, DepartmentsOfEmployees department, int salary, ManagerTypes managerType) {
        super(id, name, email, password, department, salary);
        this.managerType = managerType;
    }

    public List<String> getUnsignedComplaints(Complaints complaint) {
        return complaint.getUnsignedComplaints();
    }

    public boolean signComplaint(Complaints complaint, String complaintText) {
        if (complaint.getUnsignedComplaints().contains(complaintText)) {
            complaint.markComplaintAsSigned(complaintText);
            return true;
        }
        return false;
    }

    public String getTypeOfManager(){
        return managerType.toString();
    }

    public String getRole() {
        return getTypeOfManager();
    }
}
