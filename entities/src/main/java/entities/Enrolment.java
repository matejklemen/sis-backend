package entities;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlIDREF;
import java.io.Serializable;

@Entity(name = "enrolment")
@NamedQueries(
        value = {
                @NamedQuery(name = "Enrolment.getLastByStudentId", query = "SELECT e FROM enrolment e WHERE e.student.id = :id ORDER BY e.id DESC"),
                @NamedQuery(name = "Enrolment.getFirstByStudentId", query = "SELECT e FROM enrolment e WHERE e.student.id=:id AND e.studyProgram.id = :studyProgramId ORDER BY e.studyYear.id ASC"),
                @NamedQuery(name = "Enrolment.getByStudentId", query = "SELECT e FROM enrolment e WHERE e.student.id = :id"),
                @NamedQuery(name = "Enrolment.getRetryYearEnrolment", query = "SELECT e FROM enrolment e WHERE e.student.id = :student_id AND e.type.id = 2"),
                @NamedQuery(name = "Enrolment.getOriginalYearOfRetryYearEnrolment", query = "SELECT e FROM enrolment e WHERE e.student.id = :student_id AND e.type.id <> 2 AND e.year = :year")
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

    @ManyToOne
    @JoinColumn(name = "id_study_type")
    private StudyType type; // prvi vpis, ponovni vpis, absolvent

    @ManyToOne
    @JoinColumn(name = "id_study_kind")
    private StudyKind kind; // redni, izredni

    @ManyToOne
    @JoinColumn(name = "id_study_form")
    private StudyForm form; //

    @ManyToOne
    @JoinColumn(name = "id_klasius_srv")
    private KlasiusSrv klasiusSrv;

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

    public StudyType getType() {
        return type;
    }

    public void setType(StudyType type) {
        this.type = type;
    }

    public StudyKind getKind() {
        return kind;
    }

    public void setKind(StudyKind kind) {
        this.kind = kind;
    }

    public StudyForm getForm() {
        return form;
    }

    public void setForm(StudyForm form) {
        this.form = form;
    }

    public KlasiusSrv getKlasiusSrv() {
        return klasiusSrv;
    }

    public void setKlasiusSrv(KlasiusSrv klasiusSrv) {
        this.klasiusSrv = klasiusSrv;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

}



