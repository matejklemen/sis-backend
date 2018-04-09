package entities;

import interfaces.Codelistable;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "study_type")
@NamedQueries(
        value = {
                @NamedQuery(name = "StudyType.getAll", query = "SELECT st FROM study_type st")
        }
)
public class StudyType implements Serializable, Codelistable {

    @Id
    private int id;

    @Column(name = "short_name")
    private String shortName;

    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String[] getColumnNames() {
        return new String[]{"id", "name", "shortName"};
    }

    @Override
    public String[] getColumnTypes() {
        return new String[]{TYPE_NUMBER, TYPE_STRING, TYPE_STRING};
    }
}