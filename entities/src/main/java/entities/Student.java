package entities;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlIDREF;
import java.io.Serializable;

@Entity(name = "student")
@NamedQueries(
        value = {
                @NamedQuery(name = "Student.getAll", query = "SELECT st FROM student st"),
                @NamedQuery(name = "Student.getByRegisterNumber", query = "SELECT st FROM student st WHERE st.registerNumber = :regno"),
                @NamedQuery(name = "Student.searchStudents", query = "SELECT st FROM student st WHERE st.registerNumber LIKE :sq OR st.name LIKE :sq OR st.surname LIKE :sq")
        }
)
public class Student implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "register_number", length = 8, nullable = false, unique = true)
    private String registerNumber;

    @Column(length = 30, nullable = false)
    private String name;

    @Column(length = 60, nullable = false)
    private String surname;

    @Column(length = 80, nullable = false)
    private String email;

    //@XmlIDREF // if enabled, JSON has only ID, not whole userlogin object
    @OneToOne
    @JoinColumn(name = "id_login")
    private UserLogin loginData;

    @ManyToOne
    @JoinColumn(name = "id_study_program", nullable = false)
    private StudyProgram studyProgram;

    @ManyToOne
    @JoinColumn(name = "id_study_year", nullable = false)
    private StudyYear studyYear;

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

    public StudyProgram getStudyProgram() {
        return studyProgram;
    }

    public void setStudyProgram(StudyProgram studyProgram) {
        this.studyProgram = studyProgram;
    }

    public StudyYear getStudyYear() {
        return studyYear;
    }

    public void setStudyYear(StudyYear studyYear) {
        this.studyYear = studyYear;
    }

}
