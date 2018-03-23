package entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "study_year")
@NamedQueries(
        value = {
                //@NamedQuery(name = "Student.GetAll", query = "SELECT st FROM student_data st")
        }
)
public class StudyYear implements Serializable {

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
