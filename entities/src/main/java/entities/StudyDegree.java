package entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "study_degree")
@NamedQueries(
        value = {

        }
)
public class StudyDegree implements Serializable {

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

}
