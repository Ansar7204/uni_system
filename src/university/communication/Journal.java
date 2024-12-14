package university.communication;

import university.research.ResearchPaper;
import university.research.Researcher;
import university.users.Student;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;

public class Journal {
    private List<Student> subscribers;
    private List<ResearchPaper> researchPapers;

    public Journal() {
        this.subscribers = new ArrayList<>();
        this.researchPapers = new ArrayList<>();
    }

    public void printAllResearchPapers(Comparator<ResearchPaper> comparator, List<Researcher> researchers) {

        for (Researcher researcher : researchers) {
            researchPapers.addAll(researcher.getPublications());
        }

        researchPapers.sort(comparator);

        researchPapers.forEach(System.out::println);
    }

    public static Researcher printTopCitedResearcherOfSchool(List<Researcher> researchers) {
        Researcher topCited = null;
        int maxCitations = 0;

        for (Researcher researcher : researchers) {
            int totalCitations = 0;
            for (ResearchPaper paper : researcher.getPublications()) {
                totalCitations += paper.getNumberOfCitations();
            }

            if (totalCitations > maxCitations) {
                maxCitations = totalCitations;
                topCited = researcher;
            }
        }

        if (topCited != null) {
            System.out.println("Top-Cited Researcher of the School: " + topCited.getFirstName() + topCited.getSurname());
            System.out.println("Total Citations: " + maxCitations);
        }
        else {
            System.out.println("No researchers found.");
        }
        return topCited;
    }

    public static Researcher printTopCitedResearcherOfYear(List<Researcher> researchers, int year) {
        Researcher topCited = null;
        int maxCitations = 0;

        for (Researcher researcher : researchers) {
            int yearlyCitations = 0;
            for (ResearchPaper paper : researcher.getPublications()) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(paper.getPublishedDate());
                int paperYear = calendar.get(Calendar.YEAR);

                if (paperYear == year) {
                    yearlyCitations += paper.getNumberOfCitations();
                }
            }

            if (yearlyCitations > maxCitations) {
                maxCitations = yearlyCitations;
                topCited = researcher;
            }
        }

        if (topCited != null) {
            System.out.println("Top-Cited Researcher of the Year (" + year + "): " + topCited.getFirstName() + topCited.getSurname());
            System.out.println("Citations in " + year + ": " + maxCitations);
        }
        else {
            System.out.println("No researchers found for the year " + year + ".");
        }
        return topCited;
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