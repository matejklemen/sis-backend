package entities;

import interfaces.Codelistable;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "study_program")
@NamedQueries(
        value = {
                @NamedQuery(name = "StudyProgram.getByName", query = "SELECT sp FROM study_program sp WHERE sp.name = :name"),
                @NamedQuery(name = "StudyProgram.getAll", query = "SELECT sp FROM study_program sp"),
                @NamedQuery(name = "StudyProgram.getById", query = "SELECT sp FROM study_program sp WHERE sp.id = :id"),
        }
)
public class StudyProgram implements Serializable, Codelistable {

    @Id
    @Column(length = 10)
    private String id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "id_study_degree")
    private StudyDegree studyDegree;

    private int semesters;

    @Column(name = "evs_code")
    private int evsCode;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public StudyDegree getStudyDegree() {
        return studyDegree;
    }

    public void setStudyDegree(StudyDegree studyDegree) {
        this.studyDegree = studyDegree;
    }

    public int getSemesters() {
        return semesters;
    }

    public void setSemesters(int semesters) {
        this.semesters = semesters;
    }

    public int getEvsCode() {
        return evsCode;
    }

    public void setEvsCode(int evsCode) {
        this.evsCode = evsCode;
    }

    @Override
    public String[] getColumnNames() {
        return new String[]{"id", "name", "studyDegree", "semesters", "evsCode"};
    }

    @Override
    public String[] getColumnTypes() {
        return new String[]{TYPE_STRING, TYPE_STRING, "study_degree", TYPE_NUMBER, TYPE_NUMBER};
    }
}
