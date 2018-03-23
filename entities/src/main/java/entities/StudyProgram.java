package entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "study_program")
@NamedQueries(
        value = {
                @NamedQuery(name = "StudyProgram.getByName", query = "SELECT sp FROM study_program sp WHERE sp.name = :name")
        }
)
public class StudyProgram implements Serializable {

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

}
