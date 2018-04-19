package entities;

import interfaces.Codelistable;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "study_program")
public class StudyProgram implements Serializable, Codelistable {

    @Id
    @Column(length = 10)
    private String id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "id_study_degree")
    private StudyDegree studyDegree;

    private int semesters;

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

    @Override
    public String[] getColumnNames() {
        return new String[]{"id", "name", "studyDegree", "semesters"};
    }

    @Override
    public String[] getColumnTypes() {
        return new String[]{TYPE_STRING, TYPE_STRING, "studydegrees", TYPE_NUMBER};
    }

    @Column(columnDefinition = "BOOLEAN NOT NULL DEFAULT FALSE")
    private boolean deleted = false;

    @Override
    public boolean getDeleted() {
        return deleted;
    }

    @Override
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
