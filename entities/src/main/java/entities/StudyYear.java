package entities;

import interfaces.Codelistable;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "study_year")
@NamedQueries(
        value = {
                @NamedQuery(name = "StudyYear.getByName", query = "SELECT sy FROM study_year sy WHERE sy.name = :name AND sy.deleted = false"),
                @NamedQuery(name = "StudyYear.getAll", query = "SELECT sy FROM study_year sy WHERE sy.deleted = false"),
                @NamedQuery(name = "StudyYear.getDeleted", query = "SELECT sy FROM study_year sy WHERE sy.deleted = true"),
        }
)
public class StudyYear implements Serializable, Codelistable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
        return new String[]{"name"};
    }

    @Override
    public String[] getColumnTypes() {
        return new String[]{TYPE_STRING};
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
