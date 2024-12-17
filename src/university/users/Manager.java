package university.users;

import university.communication.ComplaintRegistry;
import university.communication.Complaints;

import java.util.ArrayList;
import java.util.List;

import university.communication.News;
import university.database.DatabaseManager;

public class Manager extends Employee {
    public ManagerTypes managerType;

    public Manager(String id, String name, String surName, String email, String password, DepartmentsOfEmployees department, int salary, ManagerTypes managerType) {
        super(id, name, surName, email, password, department, salary);
        this.managerType = managerType;
    }

    // Get all unsigned complaints from DatabaseManager
    public List<String> getUnsignedComplaints() {
        return DatabaseManager.getInstance().getAllUnsignedComplaints();
    }

    // Sign a complaint from the list of unsigned complaints
    public boolean signComplaint(String complaintText) {
        Complaints complaint = DatabaseManager.getInstance().getComplaintByText(complaintText);
        if (complaint != null && complaint.getUnsignedComplaints().contains(complaintText)) {
            complaint.markComplaintAsSigned(complaintText);
            return true;
        }
        return false;
    }

    // Method to approve complaints (marks them as signed)
    public void approveComplaints() {
        List<String> unsignedComplaints = getUnsignedComplaints();
        if (unsignedComplaints.isEmpty()) {
            System.out.println("No unsigned complaints to approve.");
        } else {
            System.out.println("Approving complaints...");
            for (String complaintText : unsignedComplaints) {
                boolean signed = signComplaint(complaintText);
                if (signed) {
                    System.out.println("Complaint approved: " + complaintText);
                } else {
                    System.out.println("Failed to approve complaint: " + complaintText);
                }
            }
        }
    }

    public String getTypeOfManager() {
        return managerType.toString();
    }
    public void addNews(String topic, String content) {
        News newNews = new News(topic, content, new ArrayList<>(), false); // Create a new News object
        DatabaseManager.getInstance().addNews(newNews);  // Add the news to the DatabaseManager
        System.out.println("News added successfully: " + topic);
    }
    public void removeAllNews() {
        DatabaseManager.getInstance().removeAllNews(); // Call the DatabaseManager to remove all news
        System.out.println("All news removed.");
    }

    public String getRole() {
        return getTypeOfManager();
    }

    @Override
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


