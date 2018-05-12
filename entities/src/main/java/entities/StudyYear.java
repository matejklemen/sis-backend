package entities;

import interfaces.Codelistable;

import javax.persistence.*;
import java.io.Serializable;

@NamedQueries(
        value = {
                @NamedQuery(name = "StudyYear.getByName", query = "SELECT sy FROM study_year sy WHERE sy.name = :name")
        }
)
@Entity(name = "study_year")
public class StudyYear implements Serializable, Codelistable {

    @Id
    private int id;

    private String name;

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

    @Override
    public String[] getColumnNames() {
        return new String[]{"id", "name"};
    }

    @Override
    public String[] getColumnTypes() {
        return new String[]{TYPE_NUMBER, TYPE_STRING};
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
