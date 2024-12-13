package university.research;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static java.util.Collections.sort;


public class ResearchPaper
{
	public String title;
	public String authors;
	public String citations;
	public int numberOfCitations;
	public String journal;
	public int pages;
	public String doi;
	public Date publishedDate;
	public List<String> papers;


	public ResearchPaper(String title, String authors, String citations, int numberOfCitations, String journal, int pages, String doi, Date publishedDate, List<String> papers) {
		this.title = title;
		this.authors = authors;
		this.citations = citations;
		this.numberOfCitations = numberOfCitations;
		this.journal = journal;
		this.pages = pages;
		this.doi = doi;
		this.publishedDate = publishedDate;
		this.papers = papers;
	}

	
	public String getCitation(Format f) {
		return "";
	}

	public void SortByDate(List<String> papers) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // Adjust format as needed
		Collections.sort(papers, (p1, p2) -> {
			try {
				Date date1 = sdf.parse(p1);
				Date date2 = sdf.parse(p2);
				return date1.compareTo(date2);
			}
			catch (ParseException e) {
				throw new RuntimeException("Invalid date format: " + e.getMessage());
			}
		});
	}
		
	public void SortByCitations(List<String> papers) {
		Collections.sort(papers, (p1, p2) -> {
			try {
				int citations1 = Integer.parseInt(p1);
				int citations2 = Integer.parseInt(p2);
				return Integer.compare(citations1, citations2);
			} catch (NumberFormatException e) {
				throw new RuntimeException("Invalid citation format: " + e.getMessage());
			}
		});
	}


		public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthors() { return authors; }
	public void setAuthors(String authors) { this.authors = authors; }

	public String getCitations() { return citations; }
	public void setCitations(String citations) { this.citations = citations; }
	public int getNumberOfCitations(){
		return numberOfCitations;
	}

	public void setNumberOfCitations(int numberOfCitations){
		this.numberOfCitations = numberOfCitations;
	}

	public String getJournal() { return journal; }
	public void setJournal(String journal) { this.journal = journal; }

	public int getPages() { return pages; }
	public void setPages(int pages) { this.pages = pages; }

	public String getDoi() { return doi; }
	public void setDoi(String doi) { this.doi = doi; }

	public Date getPublishedDate() { return publishedDate; }
	public void setPublishedDate(Date publishedDate) { this.publishedDate = publishedDate; }

	public List<String> getPapers() { return papers; }
	public void setPapers(List<String> papers) { this.papers = papers; }

	public String toString() {
		return "ResearchPaper{" +
				"title='" + title + '\'' +
				", authors='" + authors + '\'' +
				", numberOfCitations='" + numberOfCitations + '\'' +
				", citations='" + citations + '\'' +
				", journal='" + journal + '\'' +
				", pages=" + pages +
				", doi='" + doi + '\'' +
				", publishedDate=" + publishedDate +
				", papers=" + papers +
				'}';
	}
}

