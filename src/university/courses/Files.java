package university.courses;

import university.users.Teacher;

import java.util.ArrayList;
import java.util.List;

public class Files {
    public Teacher teacher;
    public String nameOfFile;
    public List<String> filesInFolder;

    public Files(Teacher teacher, String nameOfFile) {
        this.teacher = teacher;
        this.nameOfFile = nameOfFile;
        this.filesInFolder = new ArrayList<>();
    }

    public List<String> getFilesInFolder() {
        return filesInFolder;
    }
}
