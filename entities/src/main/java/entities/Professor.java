package entities;

import interfaces.Codelistable;

import javax.persistence.*;

@Entity(name = "professor")
@NamedQueries(value = {
        @NamedQuery(name = "Professor.getAllProfessors", query = "SELECT p FROM professor p WHERE p.deleted = false"),
        @NamedQuery(name = "Professor.getDeletedProfessors", query = "SELECT p FROM professor p WHERE p.deleted = true"),
        @NamedQuery(name = "Professor.getByFirstAndLastName", query = "SELECT p FROM professor p WHERE p.firstName = :fname AND (p.lastName1 = :lname OR p.lastName2 = :lname) AND p.deleted = false"),
        @NamedQuery(name = "Professor.getByLoginId", query = "SELECT p FROM professor p WHERE p.loginData.id = :loginId AND p.deleted = false"),
        /* search professors with 2 surnames */
        @NamedQuery(name = "Professor.getByFirstAndLastName2", query = "SELECT p FROM professor p WHERE p.firstName = :fname AND p.lastName1 = :lname1 AND p.lastName2 = :lname2 AND p.deleted = false")
})
public class Professor implements Codelistable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // TODO probably: academic title (prof./doc./izr. prof./...)
    // TODO probably: professional title (http://www.pisrs.si/Pis.web/pregledPredpisa?id=DRUG171)

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name1", nullable = false)
    private String lastName1;

    @Column(name = "last_name2")
    private String lastName2;

    @OneToOne
    @JoinColumn(name = "id_login")
    private UserLogin loginData;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public UserLogin getLoginData() {
        return loginData;
    }

    public void setLoginData(UserLogin loginData) {
        this.loginData = loginData;
    }

    @Override
    public String toString() {
        return String.format("Professor(%s %s%s)", firstName, lastName1, lastName2 != null ? lastName2: "");
    }

    @Override
    public String[] getColumnNames() {
        return new String[]{"firstName", "lastName1", "lastName2"};
    }

    @Override
    public String[] getColumnTypes() {
        return new String[]{TYPE_STRING, TYPE_STRING, TYPE_STRING};
    }

    @Column(columnDefinition = "BOOLEAN NOT NULL DEFAULT FALSE")
    private boolean deleted = false;

    @Override
    public boolean getDeleted() {
        return deleted;
    }

    @Override
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
