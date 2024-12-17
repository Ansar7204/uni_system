package university.library;

import university.users.Student;

public class Request {
    private Student student;
    private Book book;
    private boolean isProcessed;  // Indicates if the request has been processed

    public Request(Student student, Book book) {
        this.student = student;
        this.book = book;
        this.isProcessed = false; // Initially, the request is pending
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

