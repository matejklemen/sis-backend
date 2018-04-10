package entities.address;

import interfaces.Codelistable;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "municipality")
@NamedQueries(
        value = {
                @NamedQuery(name = "Municipality.getAll", query = "SELECT m FROM municipality m"),
        }
)
public class Municipality implements Serializable, Codelistable {

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
}
