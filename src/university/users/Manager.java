package university.users;

import university.communication.ComplaintRegistry;
import university.communication.Complaints;
import java.util.List;

public class Manager extends Employee {
    public ManagerTypes managerType;

    public Manager(String id, String name, String surName, String email, String password, DepartmentsOfEmployees department, int salary, ManagerTypes managerType) {
        super(id, name, surName, email, password, department, salary);
        this.managerType = managerType;
    }

    public List<String> getUnsignedComplaints(ComplaintRegistry registry) {
        return registry.getAllUnsignedComplaints();
    }

    public boolean signComplaint(ComplaintRegistry registry, String complaintText) {
        Complaints complaint = registry.getComplaintByText(complaintText);
        if (complaint != null && complaint.getUnsignedComplaints().contains(complaintText)) {
            complaint.markComplaintAsSigned(complaintText);
            return true;
        }
        return false;
    }

    public String getTypeOfManager() {
        return managerType.toString();
    }

    public String getRole() {
        return getTypeOfManager();
    }

    public String toString() {
        return "Manager {" +
                "ID = '" + getId() + '\'' +
                ", Name = '" + getFirstName() + '\'' +
                ", Surname = '" + getSurName() + '\'' +
                ", Email = '" + getEmail() + '\'' +
                ", Department = " + getDepartment() +
                ", Salary = " + getSalary() +
                ", Manager Type = " + managerType +
                '}';
    }

    private int getSalary() {
        return getSalary();
    }

    private String getDepartment() {
        return department.toString();
    }

    private String getSurName() {
        return surname;
    }


}

