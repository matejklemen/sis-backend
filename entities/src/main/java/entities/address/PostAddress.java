package entities.address;

import interfaces.Codelistable;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "post_address")
@NamedQueries(
        value = {
                @NamedQuery(name = "PostAddress.getAll", query = "SELECT pa FROM post_address pa WHERE pa.deleted = false"),
                @NamedQuery(name = "PostAddress.getDeleted", query = "SELECT pa FROM post_address pa WHERE pa.deleted = true"),
        }
)
public class PostAddress implements Serializable, Codelistable {

    @Id
    private int id;

    @Column(nullable = false)
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
