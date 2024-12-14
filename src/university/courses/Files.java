package university.courses;

import university.users.Teacher;

import java.util.ArrayList;
import java.util.List;

import java.util.ArrayList;
import java.util.List;

public class Files {
    private Teacher teacher;             // Teacher who owns this folder
    private String nameOfFile;           // Name of the folder/file
    private List<String> filesInFolder;  // List of file names in this folder


    public Files(Teacher teacher, String nameOfFile) {
        this.teacher = teacher;
        this.nameOfFile = nameOfFile;
        this.filesInFolder = new ArrayList<>();
    }


    public Teacher getTeacher() {
        return teacher;
    }


    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }


    public String getNameOfFile() {
        return nameOfFile;
    }


    public void setNameOfFile(String nameOfFile) {
        this.nameOfFile = nameOfFile;
    }


    public List<String> getFilesInFolder() {
        return new ArrayList<>(filesInFolder); // Return a copy to maintain encapsulation
    }


    public void addFile(String fileName) {
        if (!filesInFolder.contains(fileName)) {
            filesInFolder.add(fileName);
        } else {
            System.out.println("File already exists in the folder.");
        }
    }


    public void removeFile(String fileName) {
        if (filesInFolder.remove(fileName)) {
            System.out.println("File removed successfully.");
        } else {
            System.out.println("File not found in the folder.");
        }
    }


    public boolean containsFile(String fileName) {
        return filesInFolder.contains(fileName);
    }


    @Override
    public String toString() {
        StringBuilder details = new StringBuilder("Folder: ").append(nameOfFile)
                .append(" (Owner: ").append(teacher.getFirstName() + " " + teacher.getSurname()).append(")\nFiles:\n");

        if (filesInFolder.isEmpty()) {
            details.append("  No files available.\n");
        } else {
            for (String file : filesInFolder) {
                details.append("  - ").append(file).append("\n");
            }
        }
        return details.toString();
    }
}

