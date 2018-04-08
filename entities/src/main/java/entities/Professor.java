package entities;

import javax.persistence.*;

@Entity(name = "professor")
@NamedQueries(value = {
        @NamedQuery(name = "Professor.getAllProfessors", query = "SELECT p FROM professor p"),
        @NamedQuery(name = "Professor.getById", query = "SELECT p FROM professor p WHERE p.idProfessor = :id"),
        @NamedQuery(name = "Professor.getByFirstAndLastName", query = "SELECT p FROM professor p WHERE p.firstName = :fname AND (p.lastName1 = :lname OR p.lastName2 = :lname)"),
        /* search professors with 2 surnames */
        @NamedQuery(name = "Professor.getByFirstAndLastName2", query = "SELECT p FROM professor p WHERE p.firstName = :fname AND p.lastName1 = :lname1 AND p.lastName2 = :lname2")
})
public class Professor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_professor")
    private int idProfessor;

    // TODO probably: academic title (prof./doc./izr. prof./...)
    // TODO probably: professional title (http://www.pisrs.si/Pis.web/pregledPredpisa?id=DRUG171)

    @Column(name = "first_name", nullable = false)
    private String firstName;
    @Column(name = "last_name1", nullable = false)
    private String lastName1;
    @Column(name = "last_name2")
    private String lastName2;

    public int getIdProfessor() {
        return idProfessor;
    }

    public void setIdProfessor(int idProfessor) {
        this.idProfessor = idProfessor;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName1() {
        return lastName1;
    }

    public void setLastName1(String lastName1) {
        this.lastName1 = lastName1;
    }

    public String getLastName2() {
        return lastName2;
    }

    public void setLastName2(String lastName2) {
        this.lastName2 = lastName2;
    }

    @Override
    public String toString() {
        return String.format("Professor(%s %s%s)", firstName, lastName1, lastName2 != null ? lastName2: "");
    }
}
