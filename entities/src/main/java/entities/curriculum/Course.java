package entities.curriculum;

import entities.StudyProgram;
import interfaces.Codelistable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity(name = "course")
@NamedQueries(
        value = {
                @NamedQuery(name = "Course.getAll", query = "SELECT c FROM course c"),
        }
)
public class Course implements Serializable, Codelistable {

    @Id
    private int id;

    private String name;

    @Column(name = "credit_points")
    private int creditPoints;

    private String semester;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCreditPoints() {
        return creditPoints;
    }

    public void setCreditPoints(int creditPoints) {
        this.creditPoints = creditPoints;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    @Override
    public String[] getColumnNames() {
        return new String[]{"id", "name", "credit_points", "semester"};
    }

    @Override
    public String[] getColumnTypes() {
        return new String[]{TYPE_NUMBER, TYPE_STRING, TYPE_NUMBER, TYPE_STRING};
    }
}
