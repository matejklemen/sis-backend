package entities.curriculum;

import interfaces.Codelistable;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "part_of_curriculum")
@NamedQueries(
        value = {
                @NamedQuery(name = "PartOfCurriculum.getAll", query = "SELECT poc FROM part_of_curriculum poc")
        }
)
public class PartOfCurriculum implements Serializable, Codelistable {
    @Id
    @Column(name = "id")
    private int id;

    /*
    One of: {obv, siz, piz, mod} where:
    - obv ... obvezni predmet
    - siz ... strokovni izbirni predmet
    - piz ... prosti izbirni predmet
    - mod ... modulski predmet
    */
    @Column(name = "type_of_curriculum", length = 3)
    private String type;

    // will be NULL for non-module (not of type "mod") parts of curriculum
    @Column(name = "module_name")
    private String moduleName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    @Override
    public String[] getColumnNames() {
        return new String[]{"id", "type", "moduleName"};
    }

    @Override
    public String[] getColumnTypes() {
        return new String[]{TYPE_NUMBER, TYPE_STRING, TYPE_STRING};
    }
}
