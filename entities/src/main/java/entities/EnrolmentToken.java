package entities;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlIDREF;
import java.io.Serializable;

@Entity(name = "enrolment_token")
@NamedQueries(
        value = {

        }
)
public class EnrolmentToken implements Serializable {

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
    private boolean used; // porabi student

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

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean confirmed) {
        this.used = confirmed;
    }

    public static EnrolmentToken createEnrolmentToken(Enrolment e){
        EnrolmentToken et = new EnrolmentToken();
        et.setKind(e.getKind());
        et.setStudent(e.getStudent());
        et.setStudyProgram(e.getStudyProgram());
        et.setStudyYear(e.getStudyYear());
        et.setType(e.getType());
        et.setYear(e.getYear());
        return et;
    }

}
