package entities.curriculum;

import entities.StudyProgram;
import entities.StudyYear;

import javax.persistence.*;

@Entity(name = "ects_distribution")
public class ECTSDistribution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "id_study_program")
    private StudyProgram studyProgram;

    @ManyToOne
    @JoinColumn(name = "id_study_year")
    private StudyYear studyYear;

    @Column(name = "year_of_program", nullable = false)
    private int yearOfProgram;

    @Column(name = "ects_obv", nullable = false)
    private int ectsObv;
    @Column(name = "ects_piz", nullable = false)
    private int ectsPiz;
    @Column(name = "ects_siz", nullable = false)
    private int ectsSiz;
    @Column(name = "ects_mod", nullable = false)
    private int ectsMod;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public StudyProgram getStudyProgram() {
        return studyProgram;
    }

    public void setStudyProgram(StudyProgram studyProgram) {
        this.studyProgram = studyProgram;
    }

    public StudyYear getStudyYear() {
        return studyYear;
    }

    public void setStudyYear(StudyYear studyYear) {
        this.studyYear = studyYear;
    }

    public int getYearOfProgram() {
        return yearOfProgram;
    }

    public void setYearOfProgram(int yearOfProgram) {
        this.yearOfProgram = yearOfProgram;
    }

    public int getEctsObv() {
        return ectsObv;
    }

    public void setEctsObv(int ectsObv) {
        this.ectsObv = ectsObv;
    }

    public int getEctsPiz() {
        return ectsPiz;
    }

    public void setEctsPiz(int ectsPiz) {
        this.ectsPiz = ectsPiz;
    }

    public int getEctsSiz() {
        return ectsSiz;
    }

    public void setEctsSiz(int ectsSiz) {
        this.ectsSiz = ectsSiz;
    }

    public int getEctsMod() {
        return ectsMod;
    }

    public void setEctsMod(int ectsMod) {
        this.ectsMod = ectsMod;
    }
}
