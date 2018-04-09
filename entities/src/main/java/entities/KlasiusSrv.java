package entities;

import interfaces.Codelistable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import java.io.Serializable;

@Entity(name = "klasius_srv")
@NamedQueries(
        value = {
                @NamedQuery(name = "KlasiusSrv.getAll", query = "SELECT ks FROM klasius_srv ks"),
                @NamedQuery(name = "KlasiusSrv.getById", query = "SELECT ks FROM klasius_srv ks WHERE ks.id = :id"),
        }
)
public class KlasiusSrv implements Serializable, Codelistable {

    @Id
    private int id;

    private String name;

    private String description;

    private String level;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    @Override
    public String[] getColumnNames() {
        return new String[]{"id", "name", "description", "level"};
    }

    @Override
    public String[] getColumnTypes() {
        return new String[]{TYPE_NUMBER, TYPE_STRING, TYPE_STRING, TYPE_STRING};
    }
}
