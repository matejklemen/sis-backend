package entities;

import interfaces.Codelistable;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "study_kind")
@NamedQueries(
        value = {
                @NamedQuery(name = "StudyKind.getAll", query = "SELECT sk FROM study_kind sk WHERE sk.deleted = false"),
                @NamedQuery(name = "StudyKind.getDeleted", query = "SELECT sk FROM study_kind sk WHERE sk.deleted = true"),
        }
)
public class StudyKind implements Serializable, Codelistable {

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
