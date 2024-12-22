package university;


import university.communication.Language;
import university.communication.Languages;
import university.communication.Log;
import university.courses.Course;
import university.courses.Files;
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
                System.out.println("Please enter your email:");
                String email = scanner.nextLine();
                System.out.println("Please enter your password:");
                String password = scanner.nextLine();

                User user = db.getUsers().stream()
                        .filter(u -> u.logIn(email, password))
                        .findFirst()
                        .orElse(null);

                if (user == null) {
                    System.out.println("Invalid credentials, please try again.");
                    continue;
                }

                System.out.println(" You are logged in as " + user.getRole());

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
                System.out.println("Please enter your role (Student, Teacher, Admin,Manager,Librarian):");
                String role = scanner.nextLine();
                System.out.println("Please enter your first name:");
                String firstName = scanner.nextLine();
                System.out.println("Please enter your surname:");
                String surname = scanner.nextLine();
                System.out.println("Please enter your email:");
                String email = scanner.nextLine();
                String password = null;
                while (true) {
                    System.out.println("Please enter your password:");
                    password = scanner.nextLine();
                    if(DatabaseManager.getInstance().isPasswordAlreadyExists(password)){
                        System.out.println("Password already exists. Please choose a different password.");
                    } else {
                        break;
                    }
                }


                User newUser = null;

                switch (role.toLowerCase()) {
                    case "manager":
                        System.out.println("Please enter your salary:");
                        int managerSalary = scanner.nextInt();
                        scanner.nextLine();

                        ManagerTypes managerType;
                        while (true) {
                            try {
                                System.out.println("Please specify the manager type (OR or Dean):");
                                String managerTypeInput = scanner.nextLine().trim().toUpperCase();
                                managerType = ManagerTypes.valueOf(managerTypeInput);
                                break;
                            } catch (IllegalArgumentException e) {
                                System.out.println("Invalid manager type. Please enter 'OR' or 'Dean'.");
                            }
                        }

                        newUser = new Manager(
                                "M" + (db.getUsers().size() + 1),
                                firstName,
                                surname,
                                email,
                                password,
                                DepartmentsOfEmployees.Manager,
                                managerSalary,
                                managerType
                        );

                        System.out.println("Manager successfully registered.");
                        break;

                    case "librarian":

                        System.out.println("Please enter your salary:");
                        int librarianSalary = scanner.nextInt();
                        scanner.nextLine();


                        newUser = new Librarian(
                                "L" + (db.getUsers().size() + 1),
                                firstName,
                                surname,
                                email,
                                password,
                                DepartmentsOfEmployees.Librarian,
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
                                scanner.nextLine();
                            }
                        }
                        scanner.nextLine();

                        System.out.println("Existing student organizations:");
                        if (db.getOrganizations().isEmpty()) {
                            System.out.println("No organizations available.");
                        } else {
                            for (int i = 0; i < db.getOrganizations().size(); i++) {
                                System.out.println((i + 1) + ". " + db.getOrganizations().get(i).getName());
                            }
                        }

                        System.out.println("Enter the number of an existing organization to join, or type a new organization name to create one (leave empty if none):");
                        String organizationMembershipInput = scanner.nextLine();

                        StudentOrganization organizationMembership = null;
                        List<StudentOrganization> myOrgs = new ArrayList<>();

                        if (!organizationMembershipInput.isEmpty()) {
                            try {
                                int selectedIndex = Integer.parseInt(organizationMembershipInput) - 1;
                                if (selectedIndex >= 0 && selectedIndex < db.getOrganizations().size()) {
                                    organizationMembership = db.getOrganizations().get(selectedIndex);
                                    System.out.println("Joined existing organization: " + organizationMembership.getName());
                                    myOrgs.add(organizationMembership);
                                } else {
                                    System.out.println("Invalid selection. Creating a new organization.");
                                }
                            } catch (NumberFormatException e) {
                                organizationMembership = new StudentOrganization(organizationMembershipInput);
                                db.addOrganization(organizationMembership);
                                myOrgs.add(organizationMembership);
                                System.out.println("Created and became head of the new organization: " + organizationMembership.getName());
                            }
                        }



                        Transcript transcript = new Transcript();

                        newUser = new Student(
                                "S" + (db.getUsers().size() + 1),
                                firstName,
                                surname,
                                email,
                                password,
                                school,
                                transcript,
                                myOrgs,
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
                        scanner.nextLine();

                        TeacherTypes teacherType;
                        while (true) {
                            try {
                                System.out.println("Please enter your teacher type (Tutor, Lector, SeniorLector, Professor):");
                                String teacherTypeInput = scanner.nextLine().trim();
                                teacherType = TeacherTypes.valueOf(teacherTypeInput);
                                break;
                            } catch (IllegalArgumentException e) {
                                System.out.println("Invalid teacher type. Please enter one of the following: Tutor, Lector, SeniorLector, Professor.");
                            }
                        }
                        newUser = new Teacher("T" + (db.getUsers().size() + 1), firstName, surname, email, password, DepartmentsOfEmployees.Teacher, salary, teacherType);
                        if (db.getCourses().isEmpty()) {
                            System.out.println("No courses available in the system.");
                        } else {
                            System.out.println("Available Courses:");
                            db.listCourses();
                        }

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
                    Log log = new Log(newUser.getFirstName() + " " + newUser.getSurname(),"REGISTRATION");
                    db.addLog(log);
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
        Log log = new Log(student.getFirstName() + " " + student.getSurname(),"LOGIN");
        db.addLog(log);
        Languages pref = student.getPreferredLanguage();
        Language lang = Language.getInstance(pref);
        ConsoleHelper.clearScreen();
        System.out.println(lang.getLocalizedMessage("Welcome, " + student.getFirstName() + "!","Добро пожаловать, " + student.getFirstName() + "!",student.getFirstName()+ " қош келдіңіз!"));
        List<Course> courses = db.getCourses();
        ConsoleHelper.clearScreenAfterDelay();

        while (true) {
            ConsoleHelper.clearScreen();
            System.out.println(lang.getLocalizedMessage("""
    \nStudent Menu:
    1. View My Files
    2. View My Courses
    3. View Transcript
    4. Register for a Course
    5. Rate teacher
    6. Borrow book
    7. View my books
    8. Send message
    9. View my messages
    10. View news
    11. Join student organizations
    12. View my student organization
    13. Change language
    14. Logout
    Choose an option:\s
   \s""", """
    \nМеню студента:
    1. Посмотреть мои файлы
    2. Посмотреть мои курсы
    3. Посмотреть транскрипт
    4. Зарегистрироваться на курс
    5. Оценить учителя
    6. Занять книгу
    7. Посмотреть мои книги
    8. Отправить сообщение
    9. Посмотреть мои сообщения
    10. Посмотреть новости
    11. Присодениться к студенческим организациям
    12. Посмотреть свою студенческую организацию
    13. Поменять язык
    14. Выйти
    Выберите опцию:\s
   \s""", """
    \nСтудент менюсы:
    1. Файлдарды қарау
    2. Курстарды қарау
    3. Транскрипт қарау
    4. Курсқа тіркелі
    5. Мұғалімді бағалау
    6. Кітапты алу
    7. Кітаптарымды қарау
    8. Жолдама жіберу
    9. Жолдамаларды қарау
    10. Жаңалық көру
    11. Студенттік ұжымға қосылу
    12. Студенттік ұжымымды көру
    13. Тілді ауыстыру
    14. Шығу
    Опцияны таңдаңыз:\s
   \s"""));


            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> System.out.println(lang.getLocalizedMessage("\nViewing Your Files:\n" + student.viewFiles(),"\nСмотрим файлы:\n" + student.viewFiles(),"\nФайларды қарауда:\n" + student.viewFiles()));
                case 2 -> System.out.println(lang.getLocalizedMessage("\nYour Courses:\n" + student.viewCourses(),"\nВаши курсы:\n" + student.viewCourses(),"\nСіздің курстарыңыз:\n" + student.viewCourses()));
                case 3 -> System.out.println(student.viewTranscript());
                case 4 -> {
                    System.out.println(lang.getLocalizedMessage("\nAvailable Courses for Registration:","\n Доступные курсы для регистраций: ","\n Тіркелуге болатын курстар: "));
                    for (int i = 0; i < courses.size(); i++) {
                        System.out.println((i + 1) + ". " + courses.get(i).getCourseName() + " (" + courses.get(i).getCredits() + " credits)");
                    }

                    System.out.print(lang.getLocalizedMessage("Enter the number of the course you want to register for: ","Выберите число курса для регистраций: ","Тіркелетін курстың санын таңдаңыз: "));
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
                        System.out.println(lang.getLocalizedMessage("Invalid course selection. Please try again.","Неверный выбор курса попробуйте еще","Қате курсты таңдадыңыз тағы таңдаңыз"));
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

                    List<Book> borrowedBooks = student.getBorrowedBooks();

                    if (borrowedBooks.isEmpty()) {
                        System.out.println(lang.getLocalizedMessage("You have not borrowed any books yet.","Вы еще не брали никаких книг","Сізде алынған кітап жоқ"));
                    } else {
                        System.out.println(lang.getLocalizedMessage("Your Borrowed Books: ","Ваши взятые книги: ","Сіздің алынған кітаптарыңыз: "));
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
                    System.out.println(lang.getLocalizedMessage("\nMessages:\n" + student.viewMessages(),"\nСообщения:\n" + student.viewMessages(),"\nЖолдамалар:\n" + student.viewMessages()));
                }
                case 10 -> {
                    System.out.println(lang.getLocalizedMessage("\nLatest News:\n" + student.viewNews(scanner),"\nПоследние новости:\n" + student.viewNews(scanner),"\nСоңғы жаңалықтар:\n" + student.viewNews(scanner)));
                }
                case 11 -> {student.joinOrganization(db);}
                case 12 -> {student.viewAndUpdateOrganization(scanner,db);}
                case 13 -> {
                    System.out.println(lang.getLocalizedMessage("Choose preffered language","Выберите желаемый язык","Тілді таңдаңыз"));
                    System.out.println("1. English, 2. Русский, 3. Қазақша");
                    int langChoice = scanner.nextInt();
                    scanner.nextLine();
                    switch(langChoice) {
                        case 1: student.setPreferredLanguage(Languages.EN);
                        break;
                        case 2: student.setPreferredLanguage(Languages.RU);
                        break;
                        case 3: student.setPreferredLanguage(Languages.KZ);
                        break;
                    }

                }
                case 14 -> {
                    System.out.println(lang.getLocalizedMessage("Logging out...","Выход из системы...","Жүйеден шығуда..."));
                    log = new Log(student.getFirstName() + " " + student.getSurname(),"LOGGED OUT");
                    db.addLog(log);
                    return;
                }
                default -> System.out.println(lang.getLocalizedMessage("Invalid option. Please try again.","Неверный выбор попробуйте еще","Қате таңдау тағы таңдаңыз"));
            }
        }
    }

    private static void teacherMenu(Teacher teacher, Scanner scanner, DatabaseManager db) throws IOException {
        Log log = new Log(teacher.getFirstName() + " " + teacher.getSurname(),"LOGIN");
        db.addLog(log);
        Languages pref = teacher.getPreferredLanguage();
        Language lang = Language.getInstance(pref);
        ConsoleHelper.clearScreen();
        System.out.println(lang.getLocalizedMessage("Welcome, " + teacher.getFirstName() + "!","Добро пожаловать, " + teacher.getFirstName() + "!",teacher.getFirstName()+ " қош келдіңіз!"));
        ConsoleHelper.clearScreenAfterDelay();
        while (true) {
            ConsoleHelper.clearScreen();
            System.out.println(lang.getLocalizedMessage(
                    "\nSelect an option:\n" +
                            "1. Send message\n" +
                            "2. View messages\n" +
                            "3. View news\n" +
                            "4. View Courses\n" +
                            "5. Put Marks for a Student\n" +
                            "6. Send Complaint to Student\n" +
                            "7. Change language\n" +
                            "8. Add files to course\n"+
                            "9. Delete all my files\n"+
                            "10. View my folders and files\n"+
                            "11. Update folder\n"+
                            "12. Logout\n",

                    "\nВыберите действие:\n" +
                            "1. Отправить сообщение\n" +
                            "2. Просмотреть сообщения\n" +
                            "3. Просмотреть новости\n" +
                            "4. Просмотреть курсы\n" +
                            "5. Поставить оценки студенту\n" +
                            "6. Отправить жалобу студенту\n" +
                            "7. Изменить язык\n" +
                            "8. Добавить файлы в папку\n"+
                            "9. Удалить все мои файлы\n"+
                            "10. Посмотреть все мои папки и файлы\n"+
                            "11. Обновить папку\n"+
                            "12. Выйти\n",

                    "\nӘрекетті таңдаңыз:\n" +
                            "1. Хабарлама жіберу\n" +
                            "2. Хабарламаларды көру\n" +
                            "3. Жаңалықтарды көру\n" +
                            "4. Курстарды көру\n" +
                            "5. Студентке баға қою\n" +
                            "6. Студентке шағым жіберу\n" +
                            "7. Тілді өзгерту\n" +
                            "8. Жаңа файлдарды қалтаға қосу\n"+
                            "9. Файлдарды жою\n"+
                            "10. Барлық файлдар мен қалталарды көру\n"+
                            "11. Қалтаны өзгерті\n"+
                            "12. Шығу\n"
            ));


            int choice = new Scanner(System.in).nextInt();

            switch (choice) {
                case 1 -> {
                    String result = teacher.sendMessage(scanner, db);
                    System.out.println(result);
                }
                case 2-> {
                    System.out.println(lang.getLocalizedMessage(
                            "\nMessages:\n" + teacher.viewMessages(),
                            "\nСообщения:\n" + teacher.viewMessages(),
                            "\nХабарламалар:\n" + teacher.viewMessages()
                    ));

                }
                case 3 -> {
                    System.out.println(lang.getLocalizedMessage(
                            "\nLatest News:\n" + teacher.viewNews(scanner),
                            "\nПоследние новости:\n" + teacher.viewNews(scanner),
                            "\nСоңғы жаңалықтар:\n" + teacher.viewNews(scanner)
                    ));

                }
                case 4 -> {
                    List<Course> courses = teacher.getCourses();
                    if (courses.isEmpty()) {
                        System.out.println(lang.getLocalizedMessage("You are not teaching any courses currently.","Вы не преподаете никакие курсы на данный момен","Сіз ешқандай курстан сабақ беріп жатқан жоқсыз"));
                    } else {
                        System.out.println(lang.getLocalizedMessage("Courses you are teaching:","Курсы которые вы преподаете: ","Сабақ беретін курстар: "));
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

                case 7 -> {
                    System.out.println(lang.getLocalizedMessage("Choose preffered language","Выберите желаемый язык","Тілді таңдаңыз"));
                    System.out.println("1. English, 2. Русский, 3. Қазақша");
                    int langChoice = scanner.nextInt();
                    scanner.nextLine();
                    switch(langChoice) {
                        case 1: teacher.setPreferredLanguage(Languages.EN);
                            break;
                        case 2: teacher.setPreferredLanguage(Languages.RU);
                            break;
                        case 3: teacher.setPreferredLanguage(Languages.KZ);
                            break;
                    }
                }
                case 8 -> {
                    teacher.addFiles(scanner, db);


                }
                case 9 -> {
                    teacher.deleteFiles(scanner, db);
                }
                case 10 -> {
                    teacher.viewAllFoldersAndFiles();
                }
                case 11 -> {
                    teacher.updateFolders(scanner, db);
                }
                case 12 -> {
                    System.out.println(lang.getLocalizedMessage("Logging out...","Выход из системы...","Жүйеден шығуда..."));
                    log = new Log(teacher.getFirstName() + " " + teacher.getSurname(),"LOGGED OUT");
                    db.addLog(log);
                    return;
                }
                default -> System.out.println(lang.getLocalizedMessage("Invalid option. Please try again.","Неверный выбор попробуйте еще","Қате таңдау тағы таңдаңыз"));
            }
        }
    }

    private static void adminMenu(Admin admin, Scanner scanner, DatabaseManager db) throws IOException {
        Log log = new Log(admin.getFirstName() + " " + admin.getSurname(),"LOGIN");
        db.addLog(log);
        Languages pref = admin.getPreferredLanguage();
        Language lang = Language.getInstance(pref);
        ConsoleHelper.clearScreen();
        System.out.println(lang.getLocalizedMessage("Welcome admin, " + admin.getFirstName() + "!","Добро пожаловать админ, " + admin.getFirstName() + "!","Админ " + admin.getFirstName()+ " қош келдіңіз!"));
        ConsoleHelper.clearScreenAfterDelay();

        while (true) {
            ConsoleHelper.clearScreen();
            System.out.println(lang.getLocalizedMessage(
                    """
                    \nAdmin Menu:
                    1. Add User
                    2. Update User
                    3. Remove User
                    4. View All Users
                    5. Add course
                    6. Remove course
                    7. Update course
                    8. View all courses
                    9. Change language
                    10. View logs
                    11. Clear logs
                    12. Add student organization
                    13. Logout
                    Choose an option: 
                    """,
                    """
                    \nМеню администратора:
                    1. Добавить пользователя
                    2. Обновить пользователя
                    3. Удалить пользователя
                    4. Просмотреть всех пользователей
                    5. Добавить курс
                    6. Удалить курс
                    7. Обновить курс
                    8. Просмотреть все курсы
                    9. Поменять язык
                    10. Посмотреть журнал системы
                    11. Очистить журнал системы
                    12. Добавить студентеческую организацию
                    13. Выйти
                    Выберите опцию: 
                    """,
                    """
                    \nӘкімші мәзірі:
                    1. Пайдаланушы қосу
                    2. Пайдаланушыны жаңарту
                    3. Пайдаланушыны жою
                    4. Барлық пайдаланушыларды қарау
                    5. Курс қосу
                    6. Курсты жою
                    7. Курсты жаңарту
                    8. Барлық курстарды қарау
                    9. Тілді ауыстыру
                    10. Жүйе журналын қарау
                    11. Жүйе журналын тазалау
                    12. Студенттік ұжымды қосу
                    13. Шығу
                    Опцияны таңдаңыз: 
                    """
            ));


            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> {
                    admin.addUser(scanner);
                }

                case 2 -> {
                    System.out.print(lang.getLocalizedMessage(
                            "Enter the User ID of the user you want to update: ",
                            "Введите идентификатор пользователя, которого вы хотите обновить: ",
                            "Өзгерткіңіз келетін пайдаланушының ID нөмірін енгізіңіз: "
                    ));

                    String userId = scanner.nextLine();

                    System.out.println(lang.getLocalizedMessage(
                            "Enter new details (leave blank to keep unchanged):",
                            "Введите новые данные (оставьте пустым, чтобы не изменять):",
                            "Жаңа мәліметтерді енгізіңіз (өзгеріссіз қалдыру үшін бос қалдырыңыз):"
                    ));

                    System.out.print(lang.getLocalizedMessage(
                            "New First Name: ",
                            "Новое имя: ",
                            "Жаңа аты: "
                    ));
                    String newFirstName = scanner.nextLine();

                    System.out.print(lang.getLocalizedMessage(
                            "New Last Name: ",
                            "Новая фамилия: ",
                            "Жаңа тегі: "
                    ));
                    String newSurName = scanner.nextLine();

                    System.out.print(lang.getLocalizedMessage(
                            "New Email: ",
                            "Новый адрес электронной почты: ",
                            "Жаңа электрондық пошта: "
                    ));
                    String newEmail = scanner.nextLine();

                    System.out.print(lang.getLocalizedMessage(
                            "New Password: ",
                            "Новый пароль: ",
                            "Жаңа құпия сөз: "
                    ));
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
                    System.out.print(lang.getLocalizedMessage(
                            "Enter the User ID of the user to remove: ",
                            "Введите ID пользователя, которого нужно удалить: ",
                            "Жойылатын пайдаланушының ID нөмірін енгізіңіз: "
                    ));
                    String userId = scanner.nextLine();
                    User userToRemove = db.getUsers().stream()
                            .filter(u -> u.getId().equals(userId))
                            .findFirst()
                            .orElse(null);

                    if (userToRemove != null) {
                        admin.removeUser(userToRemove);
                    } else {
                        System.out.println(lang.getLocalizedMessage(
                                "User not found. Please try again.",
                                "Пользователь не найден. Попробуйте еще раз.",
                                "Пайдаланушы табылмады. Қайтадан көріңіз."
                        ));
                    }
                }
                case 4 -> {
                    System.out.println(lang.getLocalizedMessage(
                            "\nAll Users in the System:",
                            "\nВсе пользователи в системе:",
                            "\nЖүйедегі барлық пайдаланушылар:"
                    ));
                    for (User user : db.getUsers()) {
                        System.out.println(user);
                    }
                }
                case 5 -> admin.addCourse(scanner);
                case 6 -> admin.removeCourse(scanner);
                case 7 -> admin.updateCourse(scanner);
                case 8 -> admin.viewAllCourses();
                case 9 -> {
                    System.out.println(lang.getLocalizedMessage("Choose preffered language","Выберите желаемый язык","Тілді таңдаңыз"));
                    System.out.println("1. English, 2. Русский, 3. Қазақша");
                    int langChoice = scanner.nextInt();
                    scanner.nextLine();
                    switch(langChoice) {
                        case 1: admin.setPreferredLanguage(Languages.EN);
                            break;
                        case 2: admin.setPreferredLanguage(Languages.RU);
                            break;
                        case 3: admin.setPreferredLanguage(Languages.KZ);
                            break;
                    }
                }
                case 10 -> { System.out.println(lang.getLocalizedMessage("Opening log files...","Открываем журнал системы...","Жүйе журналын ашуда..."));
                    db.printLogs();
                }
                case 11 -> { db.clearAllLogs();}
                case 12 -> { admin.addStudentOrganization();}
                case 13 -> {
                    System.out.println(lang.getLocalizedMessage(
                            "Logging out...",
                            "Выход из системы...",
                            "Жүйеден шығу..."
                    ));
                    log = new Log(admin.getFirstName() + " " + admin.getSurname(),"LOGGED OUT");
                    db.addLog(log);
                    return;
                }
                default -> System.out.println(lang.getLocalizedMessage(
                        "Invalid option. Please try again.",
                        "Неверный вариант. Попробуйте еще раз.",
                        "Жарамсыз нұсқа. Қайтадан көріңіз."
                ));

            }
        }
    }

    private static void librarianMenu(Librarian librarian, Scanner scanner, DatabaseManager db) throws IOException {
        Log log = new Log(librarian.getFirstName() + " " + librarian.getSurname(),"LOGIN");
        db.addLog(log);
        Languages pref = librarian.getPreferredLanguage();
        Language lang = Language.getInstance(pref);
        ConsoleHelper.clearScreen();
        System.out.println(lang.getLocalizedMessage(
                "Welcome, Librarian " + librarian.getFirstName() + "!",
                "Добро пожаловать, библиотекарь " + librarian.getFirstName() + "!",
                "Қош келдіңіз, кітапханашы " + librarian.getFirstName() + "!"
        ));
        ConsoleHelper.clearScreenAfterDelay();

        while (true) {
            ConsoleHelper.clearScreen();
            System.out.println(
                    lang.getLocalizedMessage(
                            "\nLibrarian Menu:\n" +
                                    "1. Send Message\n" +
                                    "2. View Books\n" +
                                    "3. View Borrowed Books\n" +
                                    "4. Add Books\n" +
                                    "5. Remove Books\n" +
                                    "6. View Messages\n" +
                                    "7. Change Language\n" +
                                    "8. View News\n" +
                                    "9. View Incoming Borrow Requests\n" +
                                    "10. Process Borrow Request\n" +
                                    "11. Logout\n" +
                                    "Choose an option: ",

                            "\nМеню библиотекаря:\n" +
                                    "1. Отправить сообщение\n" +
                                    "2. Просмотр книг\n" +
                                    "3. Просмотр взятых книг\n" +
                                    "4. Добавить книги\n" +
                                    "5. Удалить книги\n" +
                                    "6. Просмотр сообщений\n" +
                                    "7. Изменить язык\n" +
                                    "8. Просмотр новостей\n" +
                                    "9. Просмотр входящих запросов на заимствование\n" +
                                    "10. Обработать запрос на заимствование\n" +
                                    "11. Выйти\n" +
                                    "Выберите опцию: ",

                            "\nКітапханашы мәзірі:\n" +
                                    "1. Хабар жіберу\n" +
                                    "2. Кітаптарды қарау\n" +
                                    "3. Қарызға алынған кітаптарды қарау\n" +
                                    "4. Кітап қосу\n" +
                                    "5. Кітапты жою\n" +
                                    "6. Хабарларды қарау\n" +
                                    "7. Тілді өзгерту\n" +
                                    "8. Жаңалықтарды қарау\n" +
                                    "9. Келіп түскен қарыз сұрауларын қарау\n" +
                                    "10. Қарыз сұрауын өңдеу\n" +
                                    "11. Шығу\n" +
                                    "Нұсқаны таңдаңыз: "
                    )
            );


            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> {
                    String result = librarian.sendMessage(scanner, db);
                    System.out.println(result);
                }

                case 2 -> librarian.viewBooks();
                case 3 -> librarian.viewBorrowedBooks();
                case 4 -> {
                    System.out.print(lang.getLocalizedMessage(
                            "Enter the book ID: ",
                            "Введите ID книги: ",
                            "Кітап ID нөмірін енгізіңіз: "
                    ));
                    String bookId = scanner.nextLine();

                    System.out.print(lang.getLocalizedMessage(
                            "Enter the book title: ",
                            "Введите название книги: ",
                            "Кітаптың атауын енгізіңіз: "
                    ));
                    String bookTitle = scanner.nextLine();

                    System.out.print(lang.getLocalizedMessage(
                            "Enter the author's name: ",
                            "Введите имя автора: ",
                            "Автордың атын енгізіңіз: "
                    ));
                    String author = scanner.nextLine();

                    System.out.print(lang.getLocalizedMessage(
                            "Enter the number of pages: ",
                            "Введите количество страниц: ",
                            "Беттердің санын енгізіңіз: "
                    ));
                    int numberOfPages = scanner.nextInt();
                    scanner.nextLine();

                    librarian.addBook(bookId, bookTitle, author, numberOfPages);
                    System.out.println(lang.getLocalizedMessage(
                            "Book added successfully.",
                            "Книга успешно добавлена.",
                            "Кітап сәтті қосылды."
                    ));
                }
                case 5 -> {
                    System.out.print(lang.getLocalizedMessage(
                            "Enter the book title to remove: ",
                            "Введите название книги для удаления: ",
                            "Жою үшін кітаптың атауын енгізіңіз: "
                    ));
                    String bookTitle = scanner.nextLine();
                    librarian.removeBook(bookTitle);
                    System.out.println(lang.getLocalizedMessage(
                            "Book removed successfully.",
                            "Книга успешно удалена.",
                            "Кітап сәтті жойылды."
                    ));
                }

                case 6 -> System.out.println(lang.getLocalizedMessage(
                        "\nMessages:\n" + librarian.viewMessages(),
                        "\nСообщения:\n" + librarian.viewMessages(),
                        "\nХабарлар:\n" + librarian.viewMessages()
                ));
                case 7 -> {
                    System.out.println(lang.getLocalizedMessage("Choose preffered language","Выберите желаемый язык","Тілді таңдаңыз"));
                    System.out.println("1. English, 2. Русский, 3. Қазақша");
                    int langChoice = scanner.nextInt();
                    scanner.nextLine();
                    switch(langChoice) {
                        case 1: librarian.setPreferredLanguage(Languages.EN);
                            break;
                        case 2: librarian.setPreferredLanguage(Languages.RU);
                            break;
                        case 3: librarian.setPreferredLanguage(Languages.KZ);
                            break;
                    }
                }
                case 8 -> {
                    System.out.println(lang.getLocalizedMessage(
                            "\nLatest News:\n" + librarian.viewNews(scanner),
                            "\nПоследние новости:\n" + librarian.viewNews(scanner),
                            "\nЖаңалықтар:\n" + librarian.viewNews(scanner)
                    ));
                }
                case 9 -> {
                    System.out.println(lang.getLocalizedMessage(
                            "\nIncoming Borrow Requests:\n" + librarian.viewIncomingRequests(),
                            "\nВходящие запросы на заимствование:\n" + librarian.viewIncomingRequests(),
                            "\nКеліп түскен қарыз сұраулар:\n" + librarian.viewIncomingRequests()
                    ));
                }
                case 10 -> {
                    System.out.print(lang.getLocalizedMessage(
                            "Enter the number of the request you want to process: ",
                            "Введите номер запроса, который вы хотите обработать: ",
                            "Өңдегіңіз келетін сұраудың нөмірін енгізіңіз: "
                    ));
                    int requestIndex = scanner.nextInt();
                    scanner.nextLine();

                    System.out.print(lang.getLocalizedMessage(
                            "Approve request? (yes/no): ",
                            "Одобрить запрос? (да/нет): ",
                            "Сұрауды мақұлдайсыз ба? (иә/жоқ): "
                    ));
                    String approval = scanner.nextLine();
                    boolean approve = approval.equalsIgnoreCase("yes");

                    librarian.handleBorrowRequest(requestIndex, approve);
                }
                case 11 -> {
                    System.out.println(lang.getLocalizedMessage(
                            "Logging out...",
                            "Выход из системы...",
                            "Жүйеден шығу..."
                    ));
                    log = new Log(librarian.getFirstName() + " " + librarian.getSurname(),"LOGGED OUT");
                    db.addLog(log);
                    return;
                }
                default -> System.out.println(lang.getLocalizedMessage(
                        "Invalid option. Please try again.",
                        "Неверный вариант. Попробуйте еще раз.",
                        "Жарамсыз нұсқа. Қайтадан көріңіз."
                ));
            }
        }
    }

    private static void managerMenu(Manager manager, Scanner scanner, DatabaseManager db) throws IOException {
        Log log = new Log(manager.getFirstName() + " " + manager.getSurname(),"LOGIN");
        db.addLog(log);
        Languages pref = manager.getPreferredLanguage();
        Language lang = Language.getInstance(pref);
        ConsoleHelper.clearScreen();
        System.out.println("Welcome, Manager " + manager.getFirstName() + "!");
        ConsoleHelper.clearScreenAfterDelay();

        while (true) {
            ConsoleHelper.clearScreen();
            System.out.println(
                    lang.getLocalizedMessage(
                            "\nManager Menu:\n" +
                                    "1. Send Message\n" +
                                    "2. Approve Complaint\n" +
                                    "3. View Teacher Ratings\n" +
                                    "4. Add News\n" +
                                    "5. Remove All News\n" +
                                    "6. View Messages\n" +
                                    "7. Change Language\n" +
                                    "8. View News\n" +
                                    "9. Assign teacher for course\n"+
                                    "10. Logout\n" +
                                    "Choose an option: ",

                            "\nМеню менеджера:\n" +
                                    "1. Отправить сообщение\n" +
                                    "2. Одобрить жалобу\n" +
                                    "3. Просмотр оценок преподавателей\n" +
                                    "4. Добавить новость\n" +
                                    "5. Удалить все новости\n" +
                                    "6. Просмотр сообщений\n" +
                                    "7. Изменить язык\n" +
                                    "8. Просмотр новостей\n" +
                                    "9. Назначить учителя на курс\n"+
                                    "10. Выйти\n" +
                                    "Выберите опцию: ",

                            "\nМенеджер мәзірі:\n" +
                                    "1. Хабар жіберу\n" +
                                    "2. Шағымды мақұлдау\n" +
                                    "3. Мұғалімдердің бағаларын қарау\n" +
                                    "4. Жаңалық қосу\n" +
                                    "5. Барлық жаңалықтарды жою\n" +
                                    "6. Хабарларды қарау\n" +
                                    "7. Тілді өзгерту\n" +
                                    "8. Жаңалықтарды қарау\n" +
                                    "9. Мұғалімді курсқа тағайындау \n"+
                                    "10. Шығу\n" +
                                    "Нұсқаны таңдаңыз: "
                    )
            );


            int choice = scanner.nextInt();
            scanner.nextLine();

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
                    System.out.println(lang.getLocalizedMessage(
                            "Viewing teacher ratings...",
                            "Просмотр оценок преподавателей...",
                            "Мұғалімдердің бағаларын қарау..."
                    ));

                    List<Teacher> teachers = db.getAllTeachers();

                    if (teachers.isEmpty()) {
                        System.out.println(lang.getLocalizedMessage(
                                "No teachers found.",
                                "Преподаватели не найдены.",
                                "Мұғалімдер табылмады."
                        ));
                    } else {
                        for (Teacher teacher : teachers) {
                            double averageRating = teacher.getAverageRating();
                            System.out.println(lang.getLocalizedMessage(
                                    "Teacher: " + teacher.getFirstName() + " " + teacher.getSurname() + " | Average Rating: " + averageRating,
                                    "Преподаватель: " + teacher.getFirstName() + " " + teacher.getSurname() + " | Средняя оценка: " + averageRating,
                                    "Мұғалім: " + teacher.getFirstName() + " " + teacher.getSurname() + " | Орташа баға: " + averageRating
                            ));
                        }
                    }
                }

                case 4 -> {
                    System.out.println(lang.getLocalizedMessage(
                            "Adding news...",
                            "Добавление новостей...",
                            "Жаңалықтар қосу..."
                    ));

                    System.out.print(lang.getLocalizedMessage(
                            "Enter news topic: ",
                            "Введите тему новости: ",
                            "Жаңалық тақырыбын енгізіңіз: "
                    ));
                    String topic = scanner.nextLine();

                    System.out.print(lang.getLocalizedMessage(
                            "Enter news content: ",
                            "Введите содержание новости: ",
                            "Жаңалық мазмұнын енгізіңіз: "
                    ));
                    String content = scanner.nextLine();

                    manager.addNews(topic, content);
                }

                case 5 -> {
                    manager.removeAllNews();
                    System.out.println(lang.getLocalizedMessage(
                            "All news removed.",
                            "Все новости удалены.",
                            "Барлық жаңалықтар жойылды."
                    ));
                }

                case 6 -> System.out.println(lang.getLocalizedMessage(
                        "\nMessages:\n" + manager.viewMessages(),
                        "\nСообщения:\n" + manager.viewMessages(),
                        "\nХабарламалар:\n" + manager.viewMessages()
                ));

                case 7 -> {
                    System.out.println(lang.getLocalizedMessage("Choose preffered language","Выберите желаемый язык","Тілді таңдаңыз"));
                    System.out.println("1. English, 2. Русский, 3. Қазақша");
                    int langChoice = scanner.nextInt();
                    scanner.nextLine();
                    switch(langChoice) {
                        case 1: manager.setPreferredLanguage(Languages.EN);
                            break;
                        case 2: manager.setPreferredLanguage(Languages.RU);
                            break;
                        case 3: manager.setPreferredLanguage(Languages.KZ);
                            break;
                    }
                }

                case 8 -> System.out.println(lang.getLocalizedMessage(
                        "\nLatest News:\n" + manager.viewNews(scanner),
                        "\nПоследние новости:\n" + manager.viewNews(scanner),
                        "\nСоңғы жаңалықтар:\n" + manager.viewNews(scanner)
                ));
                case 9 -> {
                    manager.assignTeachersToCourses(scanner,db.getCourses(),db.getAllTeachers());

                }

                case 10 -> {
                    System.out.println(lang.getLocalizedMessage(
                            "Logging out...",
                            "Выход...",
                            "Шығу..."
                    ));
                    log = new Log(manager.getFirstName() + " " + manager.getSurname(),"LOGGED OUT");
                    db.addLog(log);
                    return;
                }

                default -> System.out.println(lang.getLocalizedMessage(
                        "Invalid option. Please try again.",
                        "Неверный выбор. Пожалуйста, попробуйте снова.",
                        "Қате опция. Қайтадан таңдаңыз."
                ));
            }
        }
    }

}