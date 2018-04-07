package entities.curriculum;

import entities.StudyProgram;
import entities.StudyYear;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlIDREF;
import java.io.Serializable;

/*
    Information about a course that has at some point been or is still being run.
*/
@Entity(name = "curriculum")
public class Curriculum implements Serializable {
    @Id
    private int idCurriculum;
    @ManyToOne
    @JoinColumn(name = "part_of_curriculum")
    private PartOfCurriculum POC;

    @ManyToOne
    @JoinColumn(name = "id_course")
    private Course idCourse;

    @XmlIDREF
    @ManyToOne
    @JoinColumn(name = "id_study_program")
    private StudyProgram idStudyProgram;

    @ManyToOne
    @JoinColumn(name = "study_year")
    private StudyYear studyYear;

    public int getIdCurriculum() {
        return idCurriculum;
    }

    public void setIdCurriculum(int idCurriculum) {
        this.idCurriculum = idCurriculum;
    }

    public PartOfCurriculum getPOC() {
        return POC;
    }

    public void setPOC(PartOfCurriculum POC) {
        this.POC = POC;
    }

    public Course getIdCourse() {
        return idCourse;
    }

    public void setIdCourse(Course idCourse) {
        this.idCourse = idCourse;
    }

    public StudyProgram getIdStudyProgram() {
        return idStudyProgram;
    }

    public void setIdStudyProgram(StudyProgram idStudyProgram) {
        this.idStudyProgram = idStudyProgram;
    }

    public StudyYear getStudyYear() {
        return studyYear;
    }

    public void setStudyYear(StudyYear studyYear) {
        this.studyYear = studyYear;
    }
}
