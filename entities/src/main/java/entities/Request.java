package entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "requests")
@NamedQueries(
        value = {
                @NamedQuery(name = "Request.getAllRequests", query = "SELECT r FROM requests r"),
                @NamedQuery(name = "Request.getByType", query = "SELECT r FROM requests r WHERE r.type =:requesttype"),
        }
)
public class Request implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "id_student")
    private Student student;

    private int numberOfCopies;

    private String type;

    @Transient
    private String nameAndSurname;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public int getNumberOfCopies() {
        return numberOfCopies;
    }

    public void setNumberOfCopies(int numberOfCopies) {
        this.numberOfCopies = numberOfCopies;
    }

    public String getNameAndSurname() {
        return this.student.getName() + " " + this.student.getSurname();
    }

    public void setNameAndSurname(String nameAndSurname) {
        this.nameAndSurname = nameAndSurname;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
