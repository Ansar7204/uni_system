package university.library;

import university.users.Student;

import java.io.Serializable;

public class Request implements Serializable {
    private Student student;
    private Book book;
    private boolean isProcessed;

    public Request(Student student, Book book) {
        this.student = student;
        this.book = book;
        this.isProcessed = false;
    }

    public Student getStudent() {
        return student;
    }

    public Book getBook() {
        return book;
    }

    public boolean isProcessed() {
        return isProcessed;
    }

    public void setProcessed(boolean processed) {
        isProcessed = processed;
    }
}

