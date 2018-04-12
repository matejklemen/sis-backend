package entities;

import interfaces.Codelistable;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "study_form")
@NamedQueries(
        value = {
                @NamedQuery(name = "StudyForm.getAll", query = "SELECT sf FROM study_form sf WHERE sf.deleted = false"),
                @NamedQuery(name = "StudyForm.getDeleted", query = "SELECT sf FROM study_form sf WHERE sf.deleted = true"),
        }
)
public class StudyForm implements Serializable, Codelistable {

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
