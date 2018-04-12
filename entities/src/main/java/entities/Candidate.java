package entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "candidate")
@NamedQueries(
        value = {
                @NamedQuery(name = "Candidate.getByLoginId", query = "SELECT c FROM candidate c WHERE c.loginData.id = :loginId"),
        }
)
public class Candidate implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "register_number", length = 8, nullable = false, unique = true)
    private String registerNumber;

    @Column(length = 30, nullable = false)
    private String name;

    @Column(length = 30, nullable = false)
    private String surname;

    @ManyToOne
    @JoinColumn(name = "id_study_program")
    private StudyProgram studyProgram;

    @Column(length = 60, nullable = false)
    private String email;

    @OneToOne
    @JoinColumn(name = "id_login")
    private UserLogin loginData;

    private boolean used;

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

    public StudyProgram getStudyProgram() {
        return studyProgram;
    }

    public void setStudyProgram(StudyProgram studyProgram) {
        this.studyProgram = studyProgram;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public UserLogin getLoginData() {
        return loginData;
    }

    public void setLoginData(UserLogin loginData) {
        this.loginData = loginData;
    }
}
