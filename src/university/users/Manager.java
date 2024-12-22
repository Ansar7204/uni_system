package university.users;

import university.communication.Complaint;
import university.communication.Language;
import university.communication.Log;
import university.communication.News;
import university.courses.Course;
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
            Log log = new Log(this.getFirstName() + " " + this.getSurname(),"SIGNED COMPLAINT");
            DatabaseManager.getInstance().addLog(log);
            return true;

        }

        return false;
    }

    public String approveComplaint(Scanner scanner, DatabaseManager db) {
        Language lang = Language.getInstance();
        System.out.println(lang.getLocalizedMessage(
                "Approving Complaints...",
                "Утверждение жалоб...",
                "Шағымдарды бекіту..."
        ));

        List<String> unsignedComplaints = db.getAllUnsignedComplaints();

        if (unsignedComplaints.isEmpty()) {
            System.out.println(lang.getLocalizedMessage(
                    "No unsigned complaints to approve.",
                    "Нет незаверенных жалоб для утверждения.",
                    "Бекітілмеген шағымдар жоқ."
            ));
        }

        System.out.println(lang.getLocalizedMessage(
                "Unsigned Complaints:",
                "Незаверенные жалобы:",
                "Бекітілмеген шағымдар:"
        ));
        for (int i = 0; i < unsignedComplaints.size(); i++) {
            System.out.println((i + 1) + ". " + unsignedComplaints.get(i));
        }

        // Ask the manager to select a complaint to approve
        System.out.print(lang.getLocalizedMessage(
                "Select a complaint to approve (enter the number): ",
                "Выберите жалобу для утверждения (введите номер): ",
                "Бекіту үшін шағымды таңдаңыз (нөмірді енгізіңіз): "
        ));
        int complaintChoice = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        if (complaintChoice < 1 || complaintChoice > unsignedComplaints.size()) {
            System.out.println(lang.getLocalizedMessage(
                    "Invalid selection.",
                    "Неверный выбор.",
                    "Қате таңдау."
            ));
        }

        String selectedComplaint = unsignedComplaints.get(complaintChoice - 1);

        Complaint complaint = db.getComplaintByText(selectedComplaint);
        if (complaint != null) {
            boolean result = this.signComplaint(selectedComplaint);
            if (result) {
                return lang.getLocalizedMessage(
                        "Complaint approved successfully.",
                        "Жалоба успешно утверждена.",
                        "Шағым сәтті бекітілді."
                );
            } else {
                return lang.getLocalizedMessage(
                        "Failed to approve the complaint.",
                        "Не удалось утвердить жалобу.",
                        "Шағымды бекіту мүмкін болмады."
                );
            }
        } else {
            return lang.getLocalizedMessage(
                    "Complaint not found.",
                    "Жалоба не найдена.",
                    "Шағым табылмады."
            );
        }
    }


    public void approveComplaints() {
        Language lang = Language.getInstance();
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

    public void assignTeachersToCourses(Scanner scanner, List<Course> courses, List<Teacher> teachers) {
        Language lang = Language.getInstance();

        // Check if there are any courses available
        if (courses.isEmpty()) {
            System.out.println(lang.getLocalizedMessage(
                    "No available courses.",
                    "Нет доступных курсов.",
                    "Қолжетімді курстар жоқ."
            ));
            return;
        }

        // Display all available courses
        System.out.println(lang.getLocalizedMessage(
                "Available courses:",
                "Доступные курсы:",
                "Қолжетімді курстар:"
        ));
        for (int i = 0; i < courses.size(); i++) {
            System.out.println((i + 1) + ". " + courses.get(i).getCourseName());
        }

        // Ask the manager to choose a course
        System.out.println(lang.getLocalizedMessage(
                "Enter the number of the course to assign teachers (or type 'back' to return): ",
                "Введите номер курса для назначения учителей (или введите 'назад' для возврата): ",
                "Мұғалімдерді тағайындау үшін курстың нөмірін енгізіңіз (немесе 'артқа' деп жазыңыз): "
        ));
        String courseChoice = scanner.nextLine();

        if (courseChoice.equalsIgnoreCase("back") || courseChoice.equalsIgnoreCase("назад") || courseChoice.equalsIgnoreCase("артқа")) {
            return; // Exit the method
        }

        // Validate course choice
        int courseIndex;
        try {
            courseIndex = Integer.parseInt(courseChoice) - 1;
            if (courseIndex < 0 || courseIndex >= courses.size()) {
                throw new IndexOutOfBoundsException();
            }
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            System.out.println(lang.getLocalizedMessage(
                    "Invalid course selection. Please try again.",
                    "Неверный выбор курса. Пожалуйста, попробуйте снова.",
                    "Қате курс таңдауы. Қайта көріңіз."
            ));
            return;
        }

        Course selectedCourse = courses.get(courseIndex);

        // Check if there are any teachers available
        if (teachers.isEmpty()) {
            System.out.println(lang.getLocalizedMessage(
                    "No available teachers.",
                    "Нет доступных учителей.",
                    "Қолжетімді мұғалімдер жоқ."
            ));
            return;
        }

        // Display all available teachers
        System.out.println(lang.getLocalizedMessage(
                "Available teachers:",
                "Доступные учителя:",
                "Қолжетімді мұғалімдер:"
        ));
        for (int i = 0; i < teachers.size(); i++) {
            System.out.println((i + 1) + ". " + teachers.get(i).getFirstName() + " " + teachers.get(i).getSurname());
        }

        // Ask the manager to choose a teacher
        System.out.println(lang.getLocalizedMessage(
                "Enter the number of the teacher to assign to the course (or type 'back' to return): ",
                "Введите номер учителя, которого хотите назначить на курс (или введите 'назад' для возврата): ",
                "Курсқа тағайындау үшін мұғалімнің нөмірін енгізіңіз (немесе 'артқа' деп жазыңыз): "
        ));
        String teacherChoice = scanner.nextLine();

        if (teacherChoice.equalsIgnoreCase("back") || teacherChoice.equalsIgnoreCase("назад") || teacherChoice.equalsIgnoreCase("артқа")) {
            return; // Exit the method
        }

        // Validate teacher choice
        int teacherIndex;
        try {
            teacherIndex = Integer.parseInt(teacherChoice) - 1;
            if (teacherIndex < 0 || teacherIndex >= teachers.size()) {
                throw new IndexOutOfBoundsException();
            }
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            System.out.println(lang.getLocalizedMessage(
                    "Invalid teacher selection. Please try again.",
                    "Неверный выбор учителя. Пожалуйста, попробуйте снова.",
                    "Қате мұғалім таңдауы. Қайта көріңіз."
            ));
            return;
        }

        Teacher selectedTeacher = teachers.get(teacherIndex);

        // Check if the teacher is already assigned to the course
        if (selectedCourse.getCourseTeachers().contains(selectedTeacher)) {
            System.out.println(lang.getLocalizedMessage(
                    "This teacher is already assigned to the selected course.",
                    "Этот учитель уже назначен на выбранный курс.",
                    "Бұл мұғалім таңдалған курсқа қазірдің өзінде тағайындалған."
            ));
            return;
        }

        // Assign the teacher to the course
        selectedCourse.assignTeacher(selectedTeacher);
        selectedTeacher.getCourses().add(selectedCourse);

        System.out.println(lang.getLocalizedMessage(
                "Teacher assigned to the course successfully.",
                "Учитель успешно назначен на курс.",
                "Мұғалім курсқа сәтті түрде тағайындалды."
        ));
    }



    public String getTypeOfManager() {
        return managerType.toString();
    }
    public void addNews(String topic, String content) {
        Language lang = Language.getInstance();
        News newNews = new News(topic, content, new ArrayList<>(), false); // Create a new News object
        DatabaseManager.getInstance().addNews(newNews);  // Add the news to the DatabaseManager
        System.out.println(lang.getLocalizedMessage(
                "News added successfully: " + topic,
                "Новости успешно добавлены: " + topic,
                "Жаңалықтар сәтті қосылды: " + topic
        ));
        Log log = new Log(this.getFirstName() + " " + this.getSurname(),"ADDED NEWS");
        DatabaseManager.getInstance().addLog(log);
    }
    public void removeAllNews() {
        DatabaseManager.getInstance().removeAllNews(); // Call the DatabaseManager to remove all news
        Log log = new Log(this.getFirstName() + " " + this.getSurname(),"REMOVED NEWS");
        DatabaseManager.getInstance().addLog(log);
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


