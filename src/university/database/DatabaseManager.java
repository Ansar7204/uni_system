package university.database;

import university.communication.Complaint;
import university.communication.Language;
import university.communication.Log;
import university.communication.News;
import university.courses.Course;
import university.courses.Files;
import university.courses.StudentOrganization;
import university.courses.Transcript;
import university.users.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager implements Serializable {
    private static final long serialVersionUID = 1L;
    private static DatabaseManager instance;

    private List<User> users;
    private List<Course> courses;
    private List<Files> fileSystem;
    private List<Log> logs;
    private List<Transcript> transcripts;
    private List<News> newsList;
    private List<Complaint> complaints;
    private List<StudentOrganization> organizations;

    private DatabaseManager() {
        users = new ArrayList<>();
        courses = new ArrayList<>();
        fileSystem = new ArrayList<>();
        logs = new ArrayList<>();
        transcripts = new ArrayList<>();
        newsList = new ArrayList<>();
        complaints = new ArrayList<>();
        organizations = new ArrayList<>();
    }

    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }
    public boolean isPasswordAlreadyExists(String password) {
        for (User user : users) {
            if (user.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    public void addUser(User user) {
        users.add(user);
    }

    public List<User> getUsers() {
        return users;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void addCourse(Course course) {
        courses.add(course);
    }

    public void listCourses() {
        Language lang = Language.getInstance();
        System.out.println(lang.getLocalizedMessage(
                "Available courses:",
                "Доступные курсы:",
                "Қолжетімді курстар:"
        ));
        for (Course course : this.getCourses()) {
            System.out.println(lang.getLocalizedMessage(
                    "Course ID: " + course.courseID + ", Course Name: " + course.courseName,
                    "ID курса: " + course.courseID + ", Название курса: " + course.courseName,
                    "Курс ID: " + course.courseID + ", Курстың атауы: " + course.courseName
            ));
        }
    }

    public Course findCourseByID(String courseID) {
        for (Course course : this.getCourses()) {
            if (course.courseID.equals(courseID)) {
                return course;
            }
        }
        return null;
    }

    public void addFolder(Files folder) {
        fileSystem.add(folder);
    }

    public List<Files> getAllFolders() {
        return fileSystem;
    }

    public Files getFolderByName(String folderName) {
        for (Files folder : fileSystem) {
            if (folder.getNameOfFile().equalsIgnoreCase(folderName)) {
                return folder;
            }
        }
        return null;
    }

    public void addFileToFolder(String folderName, String fileName) {
        Language lang = Language.getInstance();
        Files folder = getFolderByName(folderName);
        if (folder != null) {
            folder.getFilesInFolder().add(fileName);
        } else {
            System.out.println(lang.getLocalizedMessage(
                    "Folder not found!",
                    "Папка не найдена!",
                    "Қалта табылмады!"
            ));
        }
    }

    public void printAllFolders() {
        Language lang = Language.getInstance();
        for (Files folder : fileSystem) {
            System.out.println(lang.getLocalizedMessage(
                    "Folder: " + folder.getNameOfFile() + " (Owned by: " + folder.getTeacher().getFirstName() + " " + folder.getTeacher().getSurname() + ")",
                    "Папка: " + folder.getNameOfFile() + " (Владелец: " + folder.getTeacher().getFirstName() + " " + folder.getTeacher().getSurname() + ")",
                    "Қалтанының атауы: " + folder.getNameOfFile() + " (Иесі: " + folder.getTeacher().getFirstName() + " " + folder.getTeacher().getSurname() + ")"
            ));
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
        return null;
    }

    public void addLog(Log log) {
        logs.add(log);
    }

    public List<Log> getLogs() {
        return logs;
    }

    public void printLogs() {
        if (logs.isEmpty()) {
            Language lang = Language.getInstance();
            System.out.println(lang.getLocalizedMessage(
                    "No logs available.",
                    "Журналдар жоқ.",
                    "Журналдар жоқ."
            ));
        } else {
            for (Log log : logs) {
                System.out.println(log);
            }
        }
    }
    public void clearAllLogs() {
        Language lang = Language.getInstance();
        if (logs.isEmpty()) {
            System.out.println(lang.getLocalizedMessage(
                    "No logs to clear.",
                    "Журналдарды жоюға болмайды, олар жоқ.",
                    "Нет журналов для удаления."
            ));
        } else {
            logs.clear();

            System.out.println(lang.getLocalizedMessage(
                    "All logs have been cleared.",
                    "Все журналы были очищены.",
                    "Барлық журналдар жойылды."
            ));
        }
    }



    public void addTranscript(Transcript transcript) {
        transcripts.add(transcript);
    }

    public List<Transcript> getTranscripts() {
        return transcripts;
    }

    public Transcript getTranscriptForStudent(String studentID) {
        for (User user : users) {
            if (user instanceof Student) {
                Student student = (Student) user;
                if (student.getStudentId().equals(studentID)) {
                    return student.getTranscript();
                }
            }
        }
        System.out.println("Student with ID " + studentID + " not found.");
        return null;
    }


    public void printTranscripts() {
        for (Transcript transcript : transcripts) {
            System.out.println(transcript);
        }
    }

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
        complaints.add(complaint);
    }


    public List<String> getAllUnsignedComplaints() {
        List<String> unsignedComplaints = new ArrayList<>();
        for (Complaint complaint : complaints) {
            if (!complaint.signedByManager) {
                unsignedComplaints.add(complaint.complaintText);
            }
        }
        return unsignedComplaints;
    }


    public List<Complaint> getAllComplaints(){
        return complaints;
    }

    public Complaint getComplaintByText(String complaintText) {
        for (Complaint complaint : complaints) {
            if (this.getAllComplaints().contains(complaintText)) {
                return complaint;
            }
        }
        return null;
    }

    public void addOrganization(StudentOrganization organization) {
        Language lang = Language.getInstance();
        if (!organizations.contains(organization)) {
            organizations.add(organization);
            System.out.println(lang.getLocalizedMessage(
                    "Organization added: " + organization.getName(),
                    "Организация добавлена: " + organization.getName(),
                    "Ұйым қосылды: " + organization.getName()
            ));
        } else {
            System.out.println(lang.getLocalizedMessage(
                    "Organization already exists.",
                    "Организация уже существует.",
                    "Ұйым бар."
            ));
        }
    }

    public boolean updateOrganization(String oldName, String newName) {
        Language lang = Language.getInstance();
        for (StudentOrganization organization : organizations) {
            if (organization.getName().equalsIgnoreCase(oldName)) {
                organization.setName(newName);
                System.out.println(lang.getLocalizedMessage(
                        "Organization updated: " + newName,
                        "Организация обновлена: " + newName,
                        "Ұйым жаңартылды: " + newName
                ));
                return true;
            }
        }
        System.out.println(lang.getLocalizedMessage(
                "Organization not found.",
                "Организация не найдена.",
                "Ұйым табылмады."
        ));
        return false;
    }

    public boolean deleteOrganization(String name) {
        Language lang = Language.getInstance();
        for (StudentOrganization organization : organizations) {
            if (organization.getName().equalsIgnoreCase(name)) {
                organizations.remove(organization);
                System.out.println(lang.getLocalizedMessage(
                        "Organization deleted: " + name,
                        "Организация удалена: " + name,
                        "Ұйым жойылды: " + name
                ));
                return true;
            }
        }
        System.out.println(lang.getLocalizedMessage(
                "Organization not found.",
                "Организация не найдена.",
                "Ұйым табылмады."
        ));
        return false;
    }

    public List<StudentOrganization> getOrganizations() {
        return organizations;
    }


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

