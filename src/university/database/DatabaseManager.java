package university.database;

import university.users.*;
import university.courses.*;
import java.io.*;
import java.util.*;

public class DatabaseManager implements Serializable {
    private static final long serialVersionUID = 1L;
    private static DatabaseManager instance;

    private List<User> users;              // List of all users
    private List<Course> courses;          // List of all courses
    private List<Files> fileSystem;        // List of files (Folders)

    private DatabaseManager() {
        users = new ArrayList<>();
        courses = new ArrayList<>();
        fileSystem = new ArrayList<>();    // Initialize the file system
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

    // File system management methods
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
        return null; // Return null if no matching folder is found
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
