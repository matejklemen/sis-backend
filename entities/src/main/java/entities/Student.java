package entities;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import java.io.Serializable;
import java.util.List;

@Entity(name = "student")
@NamedQueries(
        value = {
                @NamedQuery(name = "Student.getAll", query = "SELECT st FROM student st"),
                @NamedQuery(name = "Student.getByRegisterNumber", query = "SELECT st FROM student st WHERE st.registerNumber = :regno"),
                @NamedQuery(name = "Student.searchStudents", query = "SELECT st FROM student st WHERE st.registerNumber LIKE :sq OR st.name LIKE :sq OR st.surname LIKE :sq"),
                @NamedQuery(name = "Student.getAllReverse", query = "SELECT st FROM student st ORDER BY st.id DESC")
        }
)
public class Student implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @XmlID
    @XmlElement
    private int id;

    @Column(name = "register_number", length = 8, nullable = false, unique = true)
    private String registerNumber;

    @Column(length = 30, nullable = false)
    private String name;

    @Column(length = 60, nullable = false)
    private String surname;

    @ManyToOne
    @JoinColumn(name = "id_address1")
    private Address address1;

    @ManyToOne
    @JoinColumn(name = "id_address2")
    private Address address2;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(length = 80, nullable = false)
    private String email;

    //@XmlIDREF // if enabled, JSON has only ID, not whole userlogin object
    @OneToOne
    @JoinColumn(name = "id_login")
    private UserLogin loginData;

    @OneToMany(mappedBy = "student", cascade = CascadeType.PERSIST)
    private List<Enrolment> enrolments;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRegisterNumber() {
        return registerNumber;
    }

    public void setRegisterNumber(String registerNumber) {
        this.registerNumber = registerNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Address getAddress1() {
        return address1;
    }

    public void setAddress1(Address address1) {
        this.address1 = address1;
    }

    public Address getAddress2() {
        return address2;
    }

    public void setAddress2(Address address2) {
        this.address2 = address2;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserLogin getLoginData() {
        return loginData;
    }

    public void setLoginData(UserLogin loginData) {
        this.loginData = loginData;
    }

    public List<Enrolment> getEnrolments() {
        return enrolments;
    }

    public void setEnrolments(List<Enrolment> enrolments) {
        this.enrolments = enrolments;
    }
}
