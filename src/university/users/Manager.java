package university.users;

import university.communication.Complaint;
import university.communication.News;
import university.database.DatabaseManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
    public Complaint getComplaintByText(String complaintText){
        return DatabaseManager.getInstance().getComplaintByText(complaintText);

    }

    // Sign a complaint from the list of unsigned complaints
    public boolean signComplaint(String complaintText) {
        Complaint complaint = getComplaintByText(complaintText);
        if (complaint != null && getUnsignedComplaints().contains(complaintText)) {
            complaint.markComplaintAsSigned(complaintText);
            return true;
        }
        return false;
    }

    public String approveComplaint(Scanner scanner, DatabaseManager db) {
        System.out.println("Approving Complaints...");

        // Fetch all unsigned complaints from DatabaseManager
        List<String> unsignedComplaints = db.getAllUnsignedComplaints();

        // Check if there are any unsigned complaints
        if (unsignedComplaints.isEmpty()) {
            System.out.println("No unsigned complaints to approve.");
        }

        // Display the unsigned complaints
        System.out.println("Unsigned Complaints:");
        for (int i = 0; i < unsignedComplaints.size(); i++) {
            System.out.println((i + 1) + ". " + unsignedComplaints.get(i));
        }

        // Ask the manager to select a complaint to approve
        System.out.print("Select a complaint to approve (enter the number): ");
        int complaintChoice = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        if (complaintChoice < 1 || complaintChoice > unsignedComplaints.size()) {
            System.out.println("Invalid selection.");
        }

        // Get the selected complaint text
        String selectedComplaint = unsignedComplaints.get(complaintChoice - 1);

        // Retrieve the complaint from the database
        Complaint complaint = db.getComplaintByText(selectedComplaint);
        if (complaint != null) {
            // Approve the complaint
            boolean result = this.signComplaint(selectedComplaint); // Pass only the complaint text
            if (result) {
                return "Complaint approved successfully.";
            } else {
                return "Failed to approve the complaint.";
            }
        } else {
            return "Complaint not found.";
        }
    }


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


