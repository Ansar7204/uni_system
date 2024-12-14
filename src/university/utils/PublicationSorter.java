package university.utils;

import university.research.ResearchPaper;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PublicationSorter {
    private final Comparator<ResearchPaper> comparator;

    // Constructor to set the comparator
    public PublicationSorter(Comparator<ResearchPaper> comparator) {
        this.comparator = comparator;
    }

    // Method to sort the list of research papers
    public void sort(List<ResearchPaper> publications) {
        if (publications == null || publications.isEmpty()) {
            System.out.println("No publications to sort.");
            return;
        }
        // Sorting using the provided comparator
        Collections.sort(publications, comparator);
    }
}
