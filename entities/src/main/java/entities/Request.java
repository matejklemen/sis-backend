package entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "enrolment_conformation_request")
@NamedQueries(
        value = {
                @NamedQuery(name = "Request.getAllRequests", query = "SELECT r FROM enrolment_conformation_request r"),
        }
)
public class EnrolmentConfirmationRequest implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "id_student")
    private Student student;

    private int numberOfCopies;

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
}
