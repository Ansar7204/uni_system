package university;


import university.courses.Course;
import university.courses.StudentOrganization;
import university.courses.Transcript;
import university.database.DatabaseManager;
import university.exceptions.CreditLimitExceededException;
import university.library.Book;
import university.users.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws CreditLimitExceededException, IOException {


        String filePath = "src/university/database/database.txt";

        // Load data at startup
        try {
            DatabaseManager.loadFromFile(filePath);
            System.out.println("Data loaded successfully.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No saved data found. Starting fresh.");
        }


        DatabaseManager db = DatabaseManager.getInstance();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            ConsoleHelper.clearScreen();
            System.out.println("Welcome to the system!");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
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
                    teacherMenu((Teacher) user,scanner, db);
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
                System.out.println("Please enter your role (Student, Teacher, Admin,Manager,Librarian):");
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
                    case "manager":
                        // Prompt for salary
                        System.out.println("Please enter your salary:");
                        int managerSalary = scanner.nextInt();
                        scanner.nextLine(); // Consume newline

                        // Prompt for manager type
                        ManagerTypes managerType;
                        while (true) {
                            try {
                                System.out.println("Please specify the manager type (OR or Dean):");
                                String managerTypeInput = scanner.nextLine().trim().toUpperCase();
                                managerType = ManagerTypes.valueOf(managerTypeInput);
                                break; // Break the loop if valid input is given
                            } catch (IllegalArgumentException e) {
                                System.out.println("Invalid manager type. Please enter 'OR' or 'Dean'.");
                            }
                        }

                        // Create a new Manager object
                        newUser = new Manager(
                                "M" + (db.getUsers().size() + 1),  // Unique ID
                                firstName,
                                surname,
                                email,
                                password,
                                DepartmentsOfEmployees.Manager,  // Fixed as "Manager"
                                managerSalary,
                                managerType
                        );

                        System.out.println("Manager successfully registered.");
                        break;

                    case "librarian":

                        System.out.println("Please enter your salary:");
                        int librarianSalary = scanner.nextInt();
                        scanner.nextLine(); // Consume newline


                        newUser = new Librarian(
                                "L" + (db.getUsers().size() + 1),
                                firstName,
                                surname,
                                email,
                                password,
                                DepartmentsOfEmployees.Librarian,                              // Department is always null for librarians
                                librarianSalary
                        );

                        System.out.println("Librarian successfully registered.");
                        break;

                    case "student":
                        System.out.println("Please enter your school (Available options: SITE, SEOGI, SNSS, SAM, SMSGT, ISE, BS, KMA, SCE, SG):");
                        School school = null;
                        while (school == null) {
                            try {
                                String schoolInput = scanner.nextLine().toUpperCase();
                                school = School.valueOf(schoolInput);
                            } catch (IllegalArgumentException e) {
                                System.out.println("Invalid school. Please enter one of the following options: SITE, SEOGI, SNSS, SAM, SMSGT, ISE, BS, KMA, SCE, SG:");
                            }
                        }

                        System.out.println("Please enter your year of study (1-4):");
                        int yearOfStudy = -1;
                        while (yearOfStudy < 1 || yearOfStudy > 4) {
                            try {
                                yearOfStudy = scanner.nextInt();
                                if (yearOfStudy < 1 || yearOfStudy > 4) {
                                    System.out.println("Year of study must be between 1 and 4. Please try again:");
                                }
                            } catch (InputMismatchException e) {
                                System.out.println("Invalid input. Please enter a number between 1 and 4:");
                                scanner.nextLine(); // Consume invalid input
                            }
                        }
                        scanner.nextLine(); // Consume leftover newline

                        // Display list of existing student organizations
                        System.out.println("Existing student organizations:");
                        if (db.getOrganizations().isEmpty()) {
                            System.out.println("No organizations available.");
                        } else {
                            for (int i = 0; i < db.getOrganizations().size(); i++) {
                                System.out.println((i + 1) + ". " + db.getOrganizations().get(i).getName());
                            }
                        }

// Prompt user to select an existing organization or create a new one
                        System.out.println("Enter the number of an existing organization to join, or type a new organization name to create one (leave empty if none):");
                        String organizationMembershipInput = scanner.nextLine();

                        StudentOrganization organizationMembership = null;

                        if (!organizationMembershipInput.isEmpty()) {
                            try {
                                int selectedIndex = Integer.parseInt(organizationMembershipInput) - 1;
                                if (selectedIndex >= 0 && selectedIndex < db.getOrganizations().size()) {
                                    organizationMembership = db.getOrganizations().get(selectedIndex);
                                    System.out.println("Joined existing organization: " + organizationMembership.getName());
                                } else {
                                    System.out.println("Invalid selection. Creating a new organization.");
                                }
                            } catch (NumberFormatException e) {
                                // If input is not a number or invalid, assume it's a new organization name
                                organizationMembership = new StudentOrganization(organizationMembershipInput);
                                db.addOrganization(organizationMembership); // Add the new organization to the database
                                System.out.println("Created and became head of the new organization: " + organizationMembership.getName());
                            }
                        }



                        Transcript transcript = new Transcript(); // Assuming Transcript has a default constructor

                        newUser = new Student(
                                "S" + (db.getUsers().size() + 1),  // Unique student ID
                                firstName,
                                surname,
                                email,
                                password,
                                school,
                                transcript,
                                organizationMembership,
                                yearOfStudy
                        );
                        if (organizationMembership != null && organizationMembership.getHead() == null) {
                            organizationMembership.setHead((Student) newUser);
                        }

                        System.out.println("Student registered successfully!");
                        break;
                    case "teacher":
                        System.out.println("Please enter your salary:");
                        int salary = scanner.nextInt();
                        scanner.nextLine(); // Consume newline

                        TeacherTypes teacherType;
                        while (true) {
                            try {
                                System.out.println("Please enter your teacher type (Tutor, Lector, SeniorLector, Professor):");
                                String teacherTypeInput = scanner.nextLine().trim();
                                teacherType = TeacherTypes.valueOf(teacherTypeInput); // Convert input to enum
                                break; // Exit loop if input is valid
                            } catch (IllegalArgumentException e) {
                                System.out.println("Invalid teacher type. Please enter one of the following: Tutor, Lector, SeniorLector, Professor.");
                            }
                        }
                        newUser = new Teacher("T" + (db.getUsers().size() + 1), firstName, surname, email, password, DepartmentsOfEmployees.Teacher, salary, teacherType);
                        if (db.getCourses().isEmpty()) {
                            System.out.println("No courses available in the system.");
                        } else {
                            System.out.println("Available Courses:");
                            db.listCourses(); // List all available courses
                        }

                        // Ask which courses the teacher will teach
                        List<Course> teacherCourses = new ArrayList<>();
                        System.out.println("Enter course IDs (comma separated) that this teacher will teach, or press Enter to skip:");

                        String courseInput = scanner.nextLine().trim();
                        if (!courseInput.isEmpty()) {
                            String[] courseIDs = courseInput.split(",");
                            for (String courseID : courseIDs) {
                                Course course = db.findCourseByID(courseID.trim());
                                if (course != null) {
                                    teacherCourses.add(course);
                                    course.assignTeacher((Teacher) newUser);
                                } else {
                                    System.out.println("Course with ID " + courseID.trim() + " not found.");
                                }
                            }
                        }

                        ((Teacher) newUser).courseList = teacherCourses;
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
            } else if (choice == 3) {
                try {
                    db.saveToFile(filePath);
                    System.out.println("Data saved successfully. Goodbye!");
                    break;
                } catch (IOException e) {
                    System.err.println("Error saving data: " + e.getMessage());
                }

            } else {
                System.out.println("Invalid option, please try again.");
            }
        }
    }

    private static void studentMenu(Student student, Scanner scanner, DatabaseManager db) throws IOException {
        ConsoleHelper.clearScreen();
        System.out.println("Welcome, " + student.getFirstName() + "!");
        List<Course> courses = db.getCourses();
        ConsoleHelper.clearScreenAfterDelay();

        while (true) {
            ConsoleHelper.clearScreen();
            System.out.println("\nStudent Menu:");
            System.out.println("1. View My Files");
            System.out.println("2. View My Courses");
            System.out.println("3. View Transcript");
            System.out.println("4. Register for a Course");
            System.out.println("5. Rate teacher");
            System.out.println("6. Borrow book");
            System.out.println("7. View my books");
            System.out.println("8. Send message");
            System.out.println("9. View my messages");
            System.out.println("10. View news");
            System.out.println("11. Change language");
            System.out.println("12. Logout");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> System.out.println("\nViewing Your Files:\n" + student.viewFiles());
                case 2 -> System.out.println("\nYour Courses:\n" + student.viewCourses());
                case 3 -> System.out.println("\nYour Transcript:\n" + student.viewTranscript());
                case 4 -> {
                    System.out.println("\nAvailable Courses for Registration:");
                    for (int i = 0; i < courses.size(); i++) {
                        System.out.println((i + 1) + ". " + courses.get(i).getCourseName() + " (" + courses.get(i).getCredits() + " credits)");
                    }

                    System.out.print("Enter the number of the course you want to register for: ");
                    int courseChoice = scanner.nextInt();
                    scanner.nextLine();

                    if (courseChoice > 0 && courseChoice <= courses.size()) {
                        Course selectedCourse = courses.get(courseChoice - 1);

                        try {
                            String result = student.registerForCourses(selectedCourse);
                            System.out.println(result);
                        } catch (CreditLimitExceededException e) {
                            System.out.println("Error: " + e.getMessage());
                        }
                    } else {
                        System.out.println("Invalid course selection. Please try again.");
                    }

                }
                case 5 -> {
                    String result = student.rateTeacher(scanner);
                    System.out.println(result);
                }

                case 6 -> {
                    String result = student.borrowBook(scanner);
                    System.out.println(result);
                }

                case 7 -> {
                    // Case 7: View Borrowed Books

                    List<Book> borrowedBooks = student.getBorrowedBooks();

                    if (borrowedBooks.isEmpty()) {
                        System.out.println("You have not borrowed any books yet.");
                    } else {
                        System.out.println("Your Borrowed Books:");
                        for (Book book : borrowedBooks) {
                            System.out.println("- " + book.getTitle());
                        }
                    }
                }
                case 8 -> {
                    String result = student.sendMessage(scanner, db);
                    System.out.println(result);
                }
                case 9 -> {
                    System.out.println("\nMessages:\n" + student.viewMessages());
                }
                case 10 -> {
                    System.out.println("\nLatest News:\n" + student.viewNews(scanner));
                }
                case 11 -> {}
                case 12 -> {
                    System.out.println("Logging out...");
                    return;
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void teacherMenu(Teacher teacher, Scanner scanner, DatabaseManager db) throws IOException {
        ConsoleHelper.clearScreen();
        System.out.println("Welcome, " + teacher.getFirstName() + "!");
        ConsoleHelper.clearScreenAfterDelay();
        while (true) {
            ConsoleHelper.clearScreen();
            System.out.println("\nSelect an option:");
            System.out.println("1. Send message");
            System.out.println("2. View messages");
            System.out.println("3. View news");
            System.out.println("4. View Courses");
            System.out.println("5. Put Marks for a Student");
            System.out.println("6. Send Complaint to Student");
            System.out.println("7. Change language");
            System.out.println("8. Logout");

            int choice = new Scanner(System.in).nextInt();

            switch (choice) {
                case 1 -> {
                    String result = teacher.sendMessage(scanner, db);
                    System.out.println(result);
                }
                case 2-> {
                    System.out.println("\nMessages:\n" + teacher.viewMessages());
                }
                case 3 -> {
                    System.out.println("\nLatest News:\n" + teacher.viewNews(scanner));
                }
                case 4 -> {
                    List<Course> courses = teacher.getCourses();
                    if (courses.isEmpty()) {
                        System.out.println("You are not teaching any courses currently.");
                    } else {
                        System.out.println("Courses you are teaching:");
                        for (Course course : courses) {
                            System.out.println(course.getCourseName());
                        }
                    }}
                case 5 -> {
                    teacher.putMarkForStudent(scanner);
                }
                case 6 -> {
                    String result = teacher.sendComplaintForStudent(scanner, db);
                    System.out.println(result);
                }

                case 7 -> {}
                case 8 -> {
                    System.out.println("Logging out...");
                    return;
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void adminMenu(Admin admin, Scanner scanner, DatabaseManager db) throws IOException {
        ConsoleHelper.clearScreen();
        System.out.println("Welcome, Admin " + admin.getFirstName() + "!");
        ConsoleHelper.clearScreenAfterDelay();

        while (true) {
            ConsoleHelper.clearScreen();
            System.out.println("\nAdmin Menu:");
            System.out.println("1. Add User");
            System.out.println("2. Update User");
            System.out.println("3. Remove User");
            System.out.println("4. View All Users");
            System.out.println("5. Add course");
            System.out.println("6. Remove course");
            System.out.println("7. Update course");
            System.out.println("8. View all courses");
            System.out.println("9. Logout");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            switch (choice) {
                case 1 -> {
                    admin.addUser(scanner);
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
                case 5 -> admin.addCourse(scanner);
                case 6 -> admin.removeCourse(scanner);
                case 7 -> admin.updateCourse(scanner);
                case 8 -> admin.viewAllCourses();
                case 9 -> {
                    System.out.println("Logging out...");
                    return;
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void librarianMenu(Librarian librarian, Scanner scanner, DatabaseManager db) throws IOException {
        ConsoleHelper.clearScreen();
        System.out.println("Welcome, Librarian " + librarian.getFirstName() + "!");
        ConsoleHelper.clearScreenAfterDelay();

        while (true) {
            ConsoleHelper.clearScreen();
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
                    String result = librarian.sendMessage(scanner, db);
                    System.out.println(result);
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
                    System.out.println("\nLatest News:\n" + librarian.viewNews(scanner));
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

    private static void managerMenu(Manager manager, Scanner scanner, DatabaseManager db) throws IOException {
        ConsoleHelper.clearScreen();
        System.out.println("Welcome, Manager " + manager.getFirstName() + "!");
        ConsoleHelper.clearScreenAfterDelay();

        while (true) {
            ConsoleHelper.clearScreen();
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
                    String result = manager.sendMessage(scanner, db);
                    System.out.println(result);
                }

                case 2 -> {
                    String res = manager.approveComplaint(scanner, db);
                    System.out.println(res);
                }

                case 3 -> {
                        System.out.println("Viewing teacher ratings...");

                        // Get all teachers from DatabaseManager
                        List<Teacher> teachers = db.getAllTeachers();

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
                case 8 -> System.out.println("\nLatest News:\n" + manager.viewNews(scanner));
                case 9 -> {
                    System.out.println("Logging out...");
                    return;
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }

}
