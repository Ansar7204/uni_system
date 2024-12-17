package university.database;

import university.communication.Complaint;
import university.communication.Log;
import university.communication.News;
import university.users.*;
import university.courses.*;
import java.io.*;
import java.util.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager implements Serializable {
    private static final long serialVersionUID = 1L;
    private static DatabaseManager instance;

    private List<User> users;
    private List<Course> courses;
    private List<Files> fileSystem;
    private List<Log> logs;                // User activity logs
    private List<Transcript> transcripts;  // Transcripts of students
    private List<News> newsList;
    private List<Complaint> complaints; // News updates

    private DatabaseManager() {
        users = new ArrayList<>();
        courses = new ArrayList<>();
        fileSystem = new ArrayList<>();
        logs = new ArrayList<>();
        transcripts = new ArrayList<>();
        newsList = new ArrayList<>();
        complaints = new ArrayList<>();
    }

    // Singleton instance retrieval
    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    // User management methods
    public void addUser(User user) {
        users.add(user);
    }

    public List<User> getUsers() {
        return users;
    }

    // Course management methods
    public List<Course> getCourses() {
        return courses;
    }

    public void addCourse(Course course) {
        courses.add(course);
    }

    // Files management
    public void addFolder(Files folder) {
        fileSystem.add(folder);
    }

    public List<Files> getAllFolders() {
        return fileSystem;
    }

    public Files getFolderByName(String folderName) {
        for (Files folder : fileSystem) {
            if (folder.getNameOfFile().equals(folderName)) {
                return folder;
            }
        }
        return null;
    }

    public void addFileToFolder(String folderName, String fileName) {
        Files folder = getFolderByName(folderName);
        if (folder != null) {
            folder.getFilesInFolder().add(fileName);
        } else {
            System.out.println("Folder not found!");
        }
    }

    public void printAllFolders() {
        for (Files folder : fileSystem) {
            System.out.println("Folder: " + folder.getNameOfFile() +
                    " (Owned by: " + folder.getTeacher().getFirstName() + " " + folder.getTeacher().getSurname() + ")");
            for (String file : folder.getFilesInFolder()) {
                System.out.println("  - " + file);
            }
        }
    }
    public void printAllUsers() {
        for (User user : users) {
            System.out.println(user);
        }
    }

    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        for (User user : users) {
            if (user instanceof Student) {
                students.add((Student) user);
            }
        }
        return students;
    }

    public List<Teacher> getAllTeachers() {
        List<Teacher> teachers = new ArrayList<>();
        for (User user : users) {
            if (user instanceof Teacher) {
                teachers.add((Teacher) user);
            }
        }
        return teachers;
    }

    public List<Manager> getAllManagers() {
        List<Manager> managers = new ArrayList<>();
        for (User user : users) {
            if (user instanceof Manager) {
                managers.add((Manager) user);
            }
        }
        return managers;
    }

    public Librarian getLibrarian() {
        for (User user : users) {
            if (user instanceof Librarian) {
                return (Librarian) user;
            }
        }
        return null;  // Return null if no librarian found
    }

    // Logs management
    public void addLog(Log log) {
        logs.add(log);
    }

    public List<Log> getLogs() {
        return logs;
    }

    public void printLogs() {
        for (Log log : logs) {
            System.out.println(log);
        }
    }

    // Transcript management
    public void addTranscript(Transcript transcript) {
        transcripts.add(transcript);
    }

    public List<Transcript> getTranscripts() {
        return transcripts;
    }

    public Transcript getTranscriptForStudent(String studentID) {
        // Iterate over all users and find the student with the given studentID
        for (User user : users) {
            if (user instanceof Student) {
                Student student = (Student) user;
                if (student.getStudentId().equals(studentID)) {
                    return student.getTranscript();
                }
            }
        }
        // If no matching student is found, return null
        System.out.println("Student with ID " + studentID + " not found.");
        return null;
    }


    public void printTranscripts() {
        for (Transcript transcript : transcripts) {
            System.out.println(transcript);
        }
    }

    // News management
    public void addNews(News news) {
        newsList.add(news);
    }
    public void removeAllNews() {
        newsList.clear();
    }

    public List<News> getNewsList() {
        return newsList;
    }

    public void printAllNews() {
        for (News news : newsList) {
            System.out.println(news);
        }
    }

    public void addComplaint(Complaint complaint) {
        complaints.add(complaint);  // Adds a new complaint to the list
    }


    public List<String> getAllUnsignedComplaints() {
        List<String> unsignedComplaints = new ArrayList<>();
        for (Complaint complaint : complaints) {
            if (!complaint.signedByManager) {
                unsignedComplaints.add(complaint.complaintText);  // Add the complaint text if it's unsigned
            }
        }
        return unsignedComplaints;  // Return a list of all unsigned complaints
    }


    public List<Complaint> getAllComplaints(){
        return complaints;
    }

    public Complaint getComplaintByText(String complaintText) {
        for (Complaint complaint : complaints) {
            if (this.getAllComplaints().contains(complaintText)) {
                return complaint;  // Returns the complaint by the text
            }
        }
        return null;
    }

    // Serialization methods
    public void saveToFile(String filePath) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(this);
        }
    }

    public static void loadFromFile(String filePath) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            instance = (DatabaseManager) ois.readObject();
        }
    }
}

