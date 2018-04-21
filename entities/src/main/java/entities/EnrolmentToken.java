package entities;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlIDREF;
import java.io.Serializable;

@Entity(name = "enrolment_token")
@NamedQueries(
        value = {
                @NamedQuery(name = "EnrolmentToken.getByStudentId", query = "SELECT et FROM enrolment_token et WHERE et.student.id = :id"),
                @NamedQuery(name = "EnrolmentToken.getById", query = "SELECT et FROM enrolment_token et WHERE et.id = :id"),
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
    private boolean used; // porabi student

    @Column(nullable = false)
    private boolean freeChoice; // ima prosto izbiro predmetov v 3 letniku

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

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean confirmed) {
        this.used = confirmed;
    }

    public boolean isFreeChoice() {
        return freeChoice;
    }

    public void setFreeChoice(boolean freeChoice) {
        this.freeChoice = freeChoice;
    }

    public static EnrolmentToken createEnrolmentToken(Enrolment e){
        EnrolmentToken et = new EnrolmentToken();
        et.setKind(e.getKind());
        et.setStudent(e.getStudent());
        et.setStudyProgram(e.getStudyProgram());
        et.setStudyYear(e.getStudyYear());
        et.setType(e.getType());
        et.setYear(e.getYear());
        et.setForm(e.getForm());
        et.setKlasiusSrv(e.getKlasiusSrv());
        et.setFreeChoice(false);
        return et;
    }

    public static EnrolmentToken createFirstEnrolmentToken(Student s, StudyKind sk, StudyType st, StudyYear sy, StudyForm sf){
        EnrolmentToken et = new EnrolmentToken();
        et.setKind(sk); //1 redni
        et.setStudent(s);
        et.setStudyProgram(s.getStudyProgram());
        et.setStudyYear(sy);//5 -> zdaj
        et.setType(st); //1 -> prvi vpis
        et.setYear(1); //1 prvi letnik
        et.setForm(sf); //1 na lokaciji
        et.setFreeChoice(false);
        return et;
    }

    public boolean validEnrolmentToken (EnrolmentToken e) {
        if(this.kind.getId() == e.getKind().getId() &&
           this.studyProgram.getId().equals(e.getStudyProgram().getId()) &&
           this.studyYear.getId() == e.getStudyYear().getId() &&
           this.type.getId() == e.getType().getId() &&
           this.form.getId() == e.getForm().getId() &&
           this.year == e.getYear() &&
           !this.used
        ) {
            return true;
        } else {
            return false;
        }
    }

}
