package university.courses;

public class Mark {
    private Course course;
    private double firstAttestation;
    private double secondAttestation;
    private double finalExam;

    public Mark(Course course, double firstAttestation, double secondAttestation, double finalExam) {
        this.course = course;
        this.firstAttestation = (firstAttestation >= 0 && firstAttestation <= 100) ? firstAttestation : 0;
        this.secondAttestation = (secondAttestation >= 0 && secondAttestation <= 100) ? secondAttestation : 0;
        this.finalExam = (finalExam >= 0 && finalExam <= 100) ? finalExam : 0;
    }


    public Course getCourse() {
        return course;
    }


    public double getFinal() {
        return finalExam;
    }

    public double getValue() {
        return firstAttestation + secondAttestation + finalExam;
    }

    public double getFirstAttestation() {
        return firstAttestation;
    }

    public double getSecondAttestation() {
        return secondAttestation;
    }


    public double getGpaValue() {
        double totalScore = getValue();

        if (totalScore >= 90) return 4.0; // A
        if (totalScore >= 85) return 3.7; // A-
        if (totalScore >= 80) return 3.3; // B+
        if (totalScore >= 75) return 3.0; // B
        if (totalScore >= 70) return 2.7; // B-
        if (totalScore >= 65) return 2.3; // C+
        if (totalScore >= 60) return 2.0; // C
        if (totalScore >= 55) return 1.7; // C-
        if (totalScore >= 50) return 1.0; // D
        return 0.0; // F
    }

    @Override
    public String toString() {
        return String.format("Course: %s, Final Score: %.1f, GPA: %.1f",
                course.getCourseName(), getValue(), getGpaValue());
    }
}


