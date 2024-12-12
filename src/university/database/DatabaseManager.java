package university.database;

import university.users.*;
import university.courses.*;
import java.io.*;
import java.util.*;

public class DatabaseManager implements Serializable {
    private static final long serialVersionUID = 1L;

    // Singleton instance
    private static DatabaseManager instance;

    // Polymorphic collection of users
    private List<User> users;
    private List<Course> courses;

    // Private constructor for singleton
    private DatabaseManager() {
        users = new ArrayList<>();
        courses = new ArrayList<>();
    }

    // Get the singleton instance
    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    // Add a user (polymorphic method)
    public void addUser(User user) {
        users.add(user);
    }

    // Getters
    public List<User> getUsers() {
        return users;
    }

    public List<Course> getCourses() {
        return courses;
    }

    // Add a course
    public void addCourse(Course course) {
        courses.add(course);
    }

    // Print all users (polymorphic behavior)
    public void printAllUsers() {
        for (User user : users) {
            System.out.println(user); // Polymorphic toString() behavior
        }
    }

    // Perform user-specific actions (example of polymorphism)
    public void performUserActions() {
        for (User user : users) {
            if (user instanceof Student) {
                ((Student) user).viewTranscript(); // Specific to Student
            } else if (user instanceof Teacher) {
                ((Teacher) user).putMarks(); // Specific to Teacher
            }
        }
    }

    // Filter and get all students
    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        for (User user : users) {
            if (user instanceof Student) {
                students.add((Student) user);
            }
        }
        return students;
    }

    // Filter and get all teachers
    public List<Teacher> getAllTeachers() {
        List<Teacher> teachers = new ArrayList<>();
        for (User user : users) {
            if (user instanceof Teacher) {
                teachers.add((Teacher) user);
            }
        }
        return teachers;
    }

    // Filter and get all managers
    public List<Manager> getAllManagers() {
        List<Manager> managers = new ArrayList<>();
        for (User user : users) {
            if (user instanceof Manager) {
                managers.add((Manager) user);
            }
        }
        return managers;
    }

    // Serialization: Save data to a file
    public void saveToFile(String filePath) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(this);
        }
    }

    // Deserialization: Load data from a file
    public static void loadFromFile(String filePath) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            instance = (DatabaseManager) ois.readObject();
        }
    }
}