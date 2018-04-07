package entities.curriculum;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "part_of_curriculum")
@NamedQueries(
        value = {
                @NamedQuery(name = "PartOfCurriculum.getByType", query = "SELECT poc FROM part_of_curriculum poc WHERE poc.type = :type"),
                @NamedQuery(name = "PartOfCurriculum.getByModuleName", query = "SELECT poc from part_of_curriculum poc WHERE poc.moduleName = :moduleName")
        }
)
public class PartOfCurriculum implements Serializable {
    @Id
    @Column(name = "id")
    private int idPOC;

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

    public int getIdPOC() {
        return idPOC;
    }

    public void setIdPOC(int idPOC) {
        this.idPOC = idPOC;
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
}
