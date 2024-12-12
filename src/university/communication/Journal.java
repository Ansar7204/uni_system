package university.communication;

import university.research.ResearchPaper;
import university.users.Student;

import java.util.ArrayList;
import java.util.List;

public class Journal {
    private List<Student> subscribers;
    private List<ResearchPaper> researchPapers;

    public Journal() {
        this.subscribers = new ArrayList<>();
        this.researchPapers = new ArrayList<>();
    }

    public void subscribe(Student student) {
        subscribers.add(student);
    }

    public void unsubscribe(Student student) {
        subscribers.remove(student);
    }

    public void publishPaper(ResearchPaper paper) {
        researchPapers.add(paper);
        notifySubscribers(paper);
    }

    private void notifySubscribers(ResearchPaper paper) {
        for (Student student : subscribers) {
            student.notify(paper);
        }
    }
}