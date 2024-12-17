package university;

import university.communication.Complaints;
import university.communication.Message;
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
            scanner.nextLine();

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
                    teacherMenu((Teacher) user, db);
                } else if (user instanceof Admin) {
                    adminMenu((Admin) user, scanner, db);
                } else if (user instanceof Librarian) {
                    librarianMenu((Librarian) user, scanner, db);
                } else if (user instanceof Manager) {
                    managerMenu((Manager) user, scanner, db);
                }
                else {
                    System.out.println("Unknown user role.");
                }

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
                        continue;
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
                case 1 -> System.out.println("\nViewing Your Files:\n" + student.viewFiles());
                case 2 -> System.out.println("\nYour Courses:\n" + student.viewCourses());
                case 3 -> System.out.println("\nYour Transcript:\n" + student.viewTranscript());
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

        while (true) {
            System.out.println("\nSelect an option:");
            System.out.println("1. View Courses");
            System.out.println("2. Put Marks for a Student");
            System.out.println("3. Send Complaint to Student");
            System.out.println("4. Logout");

            int choice = new Scanner(System.in).nextInt();

            switch (choice) {
                case 1 -> {
                    List<Course> courses = teacher.viewCourses();
                    if (courses.isEmpty()) {
                        System.out.println("You are not teaching any courses currently.");
                    } else {
                        System.out.println("Courses you are teaching:");
                        for (Course course : courses) {
                            System.out.println(course.getCourseName());
                        }
                    }
                }
                case 2 -> {}
                case 3 -> {}
                case 4 -> {
                    System.out.println("Logging out...");
                    return;
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
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
                        scanner.nextLine();
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

    private static void librarianMenu(Librarian librarian, Scanner scanner, DatabaseManager db) {
        System.out.println("Welcome, Librarian " + librarian.getFirstName() + "!");

        while (true) {
            System.out.println("\nLibrarian Menu:");
            System.out.println("1. Send Message");
            System.out.println("2. View Books");
            System.out.println("3. View Borrowed Books");
            System.out.println("4. Add Books");
            System.out.println("5. Remove Books");
            System.out.println("6. View Messages");
            System.out.println("7. Change Language");
            System.out.println("8. View News");
            System.out.println("9. View Incoming Borrow Requests");  // New menu option
            System.out.println("10. Process Borrow Request");
            System.out.println("11. Logout");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter the recipient's email: ");
                    String recipientEmail = scanner.nextLine();

                    // Find the recipient (user) by email
                    User recipient = db.getUsers().stream()
                            .filter(u -> u.getEmail().equalsIgnoreCase(recipientEmail))
                            .findFirst()
                            .orElse(null);

                    if (recipient == null) {
                        System.out.println("User with the given email not found.");
                        break;
                    }

                    System.out.print("Enter the message content: ");
                    String content = scanner.nextLine();

                    try {
                        // Create and send the message
                        Message newMessage = new Message(librarian, recipient, content);
                        librarian.sendMessage(recipient, newMessage);
                        System.out.println("Message sent successfully to " + recipient.getFirstName() + ".");
                    } catch (IllegalArgumentException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                }

                case 2 -> librarian.viewBooks();
                case 3 -> librarian.viewBorrowedBooks();
                case 4 -> {
                    System.out.print("Enter the book ID: ");
                    String bookId = scanner.nextLine();

                    System.out.print("Enter the book title: ");
                    String bookTitle = scanner.nextLine();

                    System.out.print("Enter the author's name: ");
                    String author = scanner.nextLine();

                    System.out.print("Enter the number of pages: ");
                    int numberOfPages = scanner.nextInt();
                    scanner.nextLine(); // Consume newline

                    librarian.addBook(bookId, bookTitle, author, numberOfPages);
                    System.out.println("Book added successfully.");
                }
                case 5 -> {
                    System.out.print("Enter the book title to remove: ");
                    String bookTitle = scanner.nextLine();
                    librarian.removeBook(bookTitle);
                    System.out.println("Book removed successfully.");
                }

                case 6 -> System.out.println("\nMessages:\n" + librarian.viewMessages());
                case 7 -> {
                    System.out.print("Enter new language (e.g., English, Spanish): ");
                    String language = scanner.nextLine();
                    librarian.changeLanguage(language);
                    System.out.println("Language changed to " + language);
                }
                case 8 -> {
                    System.out.println("\nLatest News:\n" + librarian.viewNews());
                }
                case 9 -> {
                    // View Incoming Borrow Requests
                    System.out.println("\nIncoming Borrow Requests:\n" + librarian.viewIncomingRequests());
                }
                case 10 -> {
                    // Process Borrow Request
                    System.out.print("Enter the number of the request you want to process: ");
                    int requestIndex = scanner.nextInt();
                    scanner.nextLine(); // Consume newline

                    System.out.print("Approve request? (yes/no): ");
                    String approval = scanner.nextLine();
                    boolean approve = approval.equalsIgnoreCase("yes");

                    librarian.handleBorrowRequest(requestIndex, approve);
                }
                case 11 -> {
                    System.out.println("Logging out...");
                    return;
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void managerMenu(Manager manager, Scanner scanner, DatabaseManager db) {
        System.out.println("Welcome, Manager " + manager.getFirstName() + "!");

        while (true) {
            System.out.println("\nManager Menu:");
            System.out.println("1. Send Message");
            System.out.println("2. Approve Complaint");
            System.out.println("3. View Teacher Ratings");
            System.out.println("4. Add News");
            System.out.println("5. Remove All News");
            System.out.println("6. View Messages");
            System.out.println("7. Change Language");
            System.out.println("8. View News");
            System.out.println("9. Logout");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter the recipient's email: ");
                    String recipientEmail = scanner.nextLine();

                    // Find the recipient (user) by email
                    User recipient = db.getUsers().stream()
                            .filter(u -> u.getEmail().equalsIgnoreCase(recipientEmail))
                            .findFirst()
                            .orElse(null);

                    if (recipient == null) {
                        System.out.println("User with the given email not found.");
                        break;
                    }

                    System.out.print("Enter message content: ");
                    String content = scanner.nextLine();

                    try {
                        // Create a new message from the manager to the recipient
                        Message newMessage = new Message(manager, recipient, content);

                        // Send the message using the sendMessage method
                        manager.sendMessage(recipient, newMessage);

                        System.out.println("Message sent successfully to " + recipient.getFirstName() + ".");
                    } catch (IllegalArgumentException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                }

                case 2 -> {
                    System.out.println("Approving Complaints...");

                    // Fetch all unsigned complaints from DatabaseManager
                    List<String> unsignedComplaints = DatabaseManager.getInstance().getAllUnsignedComplaints();

                    // Check if there are any unsigned complaints
                    if (unsignedComplaints.isEmpty()) {
                        System.out.println("No unsigned complaints to approve.");
                        break;
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
                        break;
                    }

                    // Get the selected complaint text
                    String selectedComplaint = unsignedComplaints.get(complaintChoice - 1);

                    // Retrieve the complaint from the database
                    Complaints complaint = DatabaseManager.getInstance().getComplaintByText(selectedComplaint);
                    if (complaint != null) {
                        // Approve the complaint
                        boolean result = manager.signComplaint(selectedComplaint); // Pass only the complaint text
                        if (result) {
                            System.out.println("Complaint approved successfully.");
                        } else {
                            System.out.println("Failed to approve the complaint.");
                        }
                    } else {
                        System.out.println("Complaint not found.");
                    }

                }

                case 3 -> {
                        System.out.println("Viewing teacher ratings...");

                        // Get all teachers from DatabaseManager
                        List<Teacher> teachers = DatabaseManager.getInstance().getAllTeachers();

                        // Check if there are teachers
                        if (teachers.isEmpty()) {
                            System.out.println("No teachers found.");
                        } else {
                            // Display ratings of each teacher
                            for (Teacher teacher : teachers) {
                                double averageRating = teacher.getAverageRating();
                                System.out.println("Teacher: " + teacher.getFirstName() + " " + teacher.getSurname() + " | Average Rating: " + averageRating);
                            }
                    }
                }

                case 4 -> {
                    System.out.println("Adding news...");

                    // Get user input for the topic and content
                    System.out.print("Enter news topic: ");
                    String topic = scanner.nextLine();
                    System.out.print("Enter news content: ");
                    String content = scanner.nextLine();

                    // Add the news using the manager's addNews method
                    manager.addNews(topic, content);
                }

                case 5 -> {
                    manager.removeAllNews();  // Call the manager's removeAllNews method
                    System.out.println("All news removed.");
                }

                case 6 -> System.out.println("\nMessages:\n" + manager.viewMessages());
                case 7 -> {
                    // needs to be implemented
                }
                case 8 -> System.out.println("\nLatest News:\n" + manager.viewNews());
                case 9 -> {
                    System.out.println("Logging out...");
                    return;
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }





    private static void initializeExampleData(DatabaseManager db) {
        Teacher teacher1 = new Teacher("T1", "Alice", "Smith", "alice@school.com", "password123", DepartmentsOfEmployees.Teacher, 500, TeacherTypes.Lector, "Math");
        Teacher teacher2 = new Teacher("T2", "Bob", "Johnson", "bob@school.com", "password123", DepartmentsOfEmployees.Teacher, 100, TeacherTypes.Professor, "Physics");

        Course course1 = new Course("CS101", "Intro to Programming", "PP1", "PP2", "none");
        Course course2 = new Course("CS102", "Data Structures", "DiscreteMath", "Math", "none");
        db.addCourse(course1);
        db.addCourse(course2);

        course1.assignTeacher(teacher1);
        course2.assignTeacher(teacher2);

        Student student1 = new Student("S1", "John", "Doe", "john@student.com", "pass123", null, new Transcript(), null, 1);
        Student student2 = new Student("S2", "Jane", "Doe", "jane@student.com", "pass123", null, new Transcript(), null, 2);

        student1.registerForCourses(course1);
        student2.registerForCourses(course2);

        Files file1 = new Files(teacher1, "CS101 Lecture Notes");
        Files file2 = new Files(teacher2, "CS102 Lecture Notes");
        db.addFolder(file1);
        db.addFolder(file2);

        Admin admin1 = new Admin("A1", "Emma", "Williams", "emma@admin.com", "admin123");

        db.addUser(student1);
        db.addUser(student2);
        db.addUser(teacher1);
        db.addUser(teacher2);
        db.addUser(admin1);
    }
}
