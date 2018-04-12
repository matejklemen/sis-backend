package entities;

import interfaces.Codelistable;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "study_degree")
@NamedQueries(
        value = {
                @NamedQuery(name = "StudyDegree.getByName", query = "SELECT sd FROM study_degree sd WHERE sd.name = :name AND sd.deleted = false"),
                @NamedQuery(name = "StudyDegree.getAll", query = "SELECT sd FROM study_degree sd WHERE sd.deleted = false"),
                @NamedQuery(name = "StudyDegree.getDeleted", query = "SELECT sd FROM study_degree sd WHERE sd.deleted = true"),
                @NamedQuery(name = "StudyDegree.getById", query = "SELECT sd FROM study_degree sd WHERE sd.id = :id AND sd.deleted = false"),
        }
)
public class StudyDegree implements Serializable, Codelistable {

    @Id
    @Column(length = 3)
    private String id;

    private String name;

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

    @Override
    public String[] getColumnNames() {
        return new String[]{"id", "name"};
    }

    @Override
    public String[] getColumnTypes() {
        return new String[]{TYPE_STRING, TYPE_STRING};
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
