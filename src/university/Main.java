package university;

import university.communication.UrgencyLevel;
import university.courses.Course;
import university.courses.Files;
import university.courses.Transcript;
import university.users.*;
import university.database.DatabaseManager;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        DatabaseManager db = DatabaseManager.getInstance();


        initializeExampleData(db);

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Welcome to the system!");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline

            if (choice == 1) {
                // Login flow
                System.out.println("Please enter your email:");
                String email = scanner.nextLine();
                System.out.println("Please enter your password:");
                String password = scanner.nextLine();

                // Authenticate the user
                User user = db.getUsers().stream()
                        .filter(u -> u.logIn(email, password))
                        .findFirst()
                        .orElse(null);

                if (user == null) {
                    System.out.println("Invalid credentials, please try again.");
                    continue;
                }

                System.out.println("Login successful! You are logged in as " + user.getRole());

                if (user instanceof Student) {
                    studentMenu((Student) user, scanner, db);
                } else if (user instanceof Teacher) {
                    teacherMenu((Teacher) user,db);
                } else if (user instanceof Admin) {
                    adminMenu((Admin) user, scanner, db);
                } else {
                    System.out.println("Unknown user role.");
                }

                break; // Exit the loop after successful login and menu
            } else if (choice == 2) {
                // Registration flow
                System.out.println("Please enter your role (Student, Teacher, Admin):");
                String role = scanner.nextLine();
                System.out.println("Please enter your first name:");
                String firstName = scanner.nextLine();
                System.out.println("Please enter your surname:");
                String surname = scanner.nextLine();
                System.out.println("Please enter your email:");
                String email = scanner.nextLine();
                System.out.println("Please enter your password:");
                String password = scanner.nextLine();

                User newUser = null;

                switch (role.toLowerCase()) {
                    case "student":
                        newUser = new Student("S" + (db.getUsers().size() + 1), firstName, surname, email, password, null, new Transcript(), null, 1);
                        break;
                    case "teacher":
                        System.out.println("Please enter your department:");
                        String department = scanner.nextLine();
                        System.out.println("Please enter your salary:");
                        int salary = scanner.nextInt();
                        scanner.nextLine(); // Consume newline
                        System.out.println("Please enter your teacher type (Lector, Professor):");
                        String teacherType = scanner.nextLine();
                        newUser = new Teacher("T" + (db.getUsers().size() + 1), firstName, surname, email, password, DepartmentsOfEmployees.Teacher, salary, TeacherTypes.valueOf(teacherType), department);
                        break;
                    case "admin":
                        newUser = new Admin("A" + (db.getUsers().size() + 1), firstName, surname, email, password);
                        break;
                    default:
                        System.out.println("Invalid role entered.");
                        continue; // Retry registration if the role is invalid
                }

                if (newUser != null) {
                    db.addUser(newUser);
                    System.out.println("Registration successful! You can now log in.");
                }
            } else {
                System.out.println("Invalid option, please try again.");
            }
        }
    }


    private static void studentMenu(Student student, Scanner scanner, DatabaseManager db) {
        System.out.println("Welcome, " + student.getFirstName() + "!");
        List<Course> courses = db.getCourses();
        List<Files> files = db.getAllFolders();

        while (true) {
            System.out.println("\nStudent Menu:");
            System.out.println("1. View My Files");
            System.out.println("2. View My Courses");
            System.out.println("3. View Transcript");
            System.out.println("4. Register for a Course");
            System.out.println("5. Logout");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            switch (choice) {
                case 1 -> {
                    System.out.println("\nViewing Your Files:");
                    System.out.println(student.viewFiles());
                }
                case 2 -> {
                    System.out.println("\nYour Courses:");
                    System.out.println(student.viewCourses());
                }
                case 3 -> {
                    System.out.println("\nYour Transcript:");
                    System.out.println(student.viewTranscript());
                }
                case 4 -> {
                    System.out.println("\nAvailable Courses for Registration:");
                    for (int i = 0; i < courses.size(); i++) {
                        System.out.println((i + 1) + ". " + courses.get(i).getCourseName());
                    }
                    System.out.print("Enter the number of the course you want to register for: ");
                    int courseChoice = scanner.nextInt();
                    scanner.nextLine();

                    if (courseChoice > 0 && courseChoice <= courses.size()) {
                        Course selectedCourse = courses.get(courseChoice - 1);
                        String result = student.registerForCourses(selectedCourse);
                        System.out.println(result);
                    } else {
                        System.out.println("Invalid course selection. Please try again.");
                    }
                }
                case 5 -> {
                    System.out.println("Logging out...");
                    return;
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void teacherMenu(Teacher teacher, DatabaseManager databaseManager) {
        System.out.println("Welcome, " + teacher.getFirstName() + "!");

        // Main menu loop
        while (true) {
            System.out.println("\nSelect an option:");
            System.out.println("1. View Courses");
            System.out.println("2. Put Marks for a Student");
            System.out.println("3. Send Complaint to Student");
            System.out.println("4. Exit");

            int choice = new Scanner(System.in).nextInt();

            switch (choice) {
                case 1:
                    // View courses taught by the teacher
                    List<Course> courses = teacher.viewCourses();
                    if (courses.isEmpty()) {
                        System.out.println("You are not teaching any courses currently.");
                    } else {
                        System.out.println("Courses you are teaching:");
                        for (Course course : courses) {
                            System.out.println(course.getCourseName());
                        }
                    }
                    break;

                case 2:
                    // Put marks for a student in a specific course
                    putMarks(teacher, databaseManager);
                    break;

                case 3:
                    // Send a complaint to a student
                    sendComplaint(teacher, databaseManager);
                    break;

                case 4:
                    // Exit the menu
                    System.out.println("Goodbye, " + teacher.getFirstName() + "!");
                    return;

                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void putMarks(Teacher teacher, DatabaseManager databaseManager) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter student ID: ");
        String studentId = scanner.nextLine();

        // Find the student using DatabaseManager
        Student student = databaseManager.getAllStudents().stream()
                .filter(s -> s.getId().equals(studentId))
                .findFirst()
                .orElse(null);

        if (student == null) {
            System.out.println("Student not found.");
            return;
        }

        System.out.print("Enter course name: ");
        String courseName = scanner.nextLine();

        // Find the course using DatabaseManager
        Course course = databaseManager.getCourses().stream()
                .filter(c -> c.getCourseName().equals(courseName))
                .findFirst()
                .orElse(null);

        if (course == null) {
            System.out.println("Course not found.");
            return;
        }

        if (!student.getRegisteredCourses().contains(course)) {
            System.out.println("The student is not registered for this course.");
            return;
        }

        System.out.print("Enter first attestation mark (0-30): ");
        double firstAttestation = scanner.nextDouble();

        System.out.print("Enter second attestation mark (0-30): ");
        double secondAttestation = scanner.nextDouble();

        System.out.print("Enter final exam mark (0-40): ");
        double finalExam = scanner.nextDouble();

        // Use Teacher's method to put marks
        teacher.putMarks(student, course, firstAttestation, secondAttestation, finalExam);
    }

    private static void sendComplaint(Teacher teacher, DatabaseManager databaseManager) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter student ID: ");
        String studentId = scanner.nextLine();

        // Find the student using DatabaseManager
        Student student = databaseManager.getAllStudents().stream()
                .filter(s -> s.getId().equals(studentId))
                .findFirst()
                .orElse(null);

        if (student == null) {
            System.out.println("Student not found.");
            return;
        }

        System.out.print("Enter complaint content: ");
        String complaintContent = scanner.nextLine();

        System.out.print("Enter urgency level (LOW, MEDIUM, HIGH): ");
        String urgencyInput = scanner.nextLine();

        try {
            UrgencyLevel urgency = UrgencyLevel.valueOf(urgencyInput.toUpperCase());
            String result = teacher.sendComplaint(urgency, complaintContent, student);
            System.out.println(result);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid urgency level. Please try again.");
        }
    }



    private static void adminMenu(Admin admin, Scanner scanner, DatabaseManager db) {
        System.out.println("Welcome, Admin " + admin.getFirstName() + "!");

        while (true) {
            System.out.println("\nAdmin Menu:");
            System.out.println("1. Add User");
            System.out.println("2. Update User");
            System.out.println("3. Remove User");
            System.out.println("4. View All Users");
            System.out.println("5. Logout");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            switch (choice) {
                case 1 -> {
                    System.out.println("Enter user details to add:");
                    System.out.print("Role (Student/Teacher/Admin): ");
                    String role = scanner.nextLine();

                    System.out.print("First Name: ");
                    String firstName = scanner.nextLine();
                    System.out.print("Last Name: ");
                    String lastName = scanner.nextLine();
                    System.out.print("Email: ");
                    String email = scanner.nextLine();
                    System.out.print("Password: ");
                    String password = scanner.nextLine();

                    User newUser;
                    if (role.equalsIgnoreCase("Student")) {
                        System.out.print("Year: ");
                        int year = scanner.nextInt();
                        scanner.nextLine(); // Consume the newline
                        newUser = new Student("S" + System.currentTimeMillis(), firstName, lastName, email, password, null, new Transcript(), null, year);
                    } else if (role.equalsIgnoreCase("Teacher")) {
                        System.out.print("Department: ");
                        String department = scanner.nextLine();
                        newUser = new Teacher("T" + System.currentTimeMillis(), firstName, lastName, email, password, DepartmentsOfEmployees.Teacher, 0, TeacherTypes.Lector, department);
                    } else if (role.equalsIgnoreCase("Admin")) {
                        newUser = new Admin("A" + System.currentTimeMillis(), firstName, lastName, email, password);
                    } else {
                        System.out.println("Invalid role entered. Please try again.");
                        continue;
                    }

                    admin.addUser(newUser);
                }
                case 2 -> {
                    System.out.print("Enter the User ID of the user you want to update: ");
                    String userId = scanner.nextLine();

                    System.out.println("Enter new details (leave blank to keep unchanged):");
                    System.out.print("New First Name: ");
                    String newFirstName = scanner.nextLine();
                    System.out.print("New Last Name: ");
                    String newSurName = scanner.nextLine();
                    System.out.print("New Email: ");
                    String newEmail = scanner.nextLine();
                    System.out.print("New Password: ");
                    String newPassword = scanner.nextLine();

                    admin.updateUser(
                            userId,
                            newFirstName.isEmpty() ? null : newFirstName,
                            newSurName.isEmpty() ? null : newSurName,
                            newEmail.isEmpty() ? null : newEmail,
                            newPassword.isEmpty() ? null : newPassword
                    );
                }
                case 3 -> {
                    System.out.print("Enter the User ID of the user to remove: ");
                    String userId = scanner.nextLine();
                    User userToRemove = db.getUsers().stream()
                            .filter(u -> u.getId().equals(userId))
                            .findFirst()
                            .orElse(null);

                    if (userToRemove != null) {
                        admin.removeUser(userToRemove);
                    } else {
                        System.out.println("User not found. Please try again.");
                    }
                }
                case 4 -> {
                    System.out.println("\nAll Users in the System:");
                    for (User user : db.getUsers()) {
                        System.out.println(user);
                    }
                }
                case 5 -> {
                    System.out.println("Logging out...");
                    return;
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void initializeExampleData(DatabaseManager db) {
        // Create example teachers
        Teacher teacher1 = new Teacher("T1", "Alice", "Smith", "alice@school.com", "password123", DepartmentsOfEmployees.Teacher, 500, TeacherTypes.Lector, "Math");
        Teacher teacher2 = new Teacher("T2", "Bob", "Johnson", "bob@school.com", "password123", DepartmentsOfEmployees.Teacher, 100, TeacherTypes.Professor, "Physics");

        // Create example courses
        Course course1 = new Course("CS101", "Intro to Programming", "PP1", "PP2", "none");
        Course course2 = new Course("CS102", "Data Structures", "DicreteMAth", "Math", "none");
        db.addCourse(course1);
        db.addCourse(course2);

        course1.assignTeacher(teacher1);
        course2.assignTeacher(teacher2);

        // Create example students
        Student student1 = new Student(
                "S1", "John", "Doe", "john@student.com", "pass123",
                null, new Transcript(), null, 1
        );
        Student student2 = new Student(
                "S2", "Jane", "Doe", "jane@student.com", "pass123",
                null, new Transcript(), null, 2
        );

        // Register students for courses
        student1.registerForCourses(course1);
        student2.registerForCourses(course2);

        // Create example files
        Files file1 = new Files(teacher1, "CS101 Lecture Notes");
        Files file2 = new Files(teacher2, "CS102 Lecture Notes");
        db.addFolder(file1);
        db.addFolder(file2);

        // Create an example admin
        Admin admin1 = new Admin("A1", "Emma", "Williams", "emma@admin.com", "admin123");

        // Add example users to the database
        db.addUser(student1);
        db.addUser(student2);
        db.addUser(teacher1);
        db.addUser(teacher2);
        db.addUser(admin1);
    }


}
// new line
// oiejqlkwej

