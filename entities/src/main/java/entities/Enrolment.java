package entities;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlIDREF;
import java.io.Serializable;
import java.util.List;

@Entity(name = "enrolment")
@NamedQueries(
        value = {
                @NamedQuery(name = "Enrolment.getLastByStudentId", query = "SELECT e FROM enrolment e WHERE e.student.id=:id ORDER BY e.studyYear.id DESC")
        }
)
public class Enrolment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "id_student")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "id_study_year")
    private StudyYear studyYear;

    @ManyToOne
    @JoinColumn(name = "id_study_program")
    private StudyProgram studyProgram;

    private int year; // letnik

    @Column(length = 20)
    private String type; // prvi vpis, ponovni vpis, absolvent

    @Column(length = 20)
    private String kind; // redni, izredni

    @Column(nullable = false)
    private boolean confirmed; // potrdi referentka

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @XmlIDREF // pass by id prevents infinite stack
    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public StudyYear getStudyYear() {
        return studyYear;
    }

    public void setStudyYear(StudyYear studyYear) {
        this.studyYear = studyYear;
    }

    public StudyProgram getStudyProgram() {
        return studyProgram;
    }

    public void setStudyProgram(StudyProgram studyProgram) {
        this.studyProgram = studyProgram;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

}
