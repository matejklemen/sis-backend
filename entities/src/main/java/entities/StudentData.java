package entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "student_data")
@NamedQueries(
        value = {
                @NamedQuery(name = "StudentData.GetAll", query = "SELECT st FROM student_data st")
        }
)
public class StudentData implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(length = 30, nullable = false)
    private String name;
    @Column(length = 30, nullable = false)
    private String surname;
    @Column(length = 7, nullable = false)
    private String course;
    @Column(length = 60, nullable = false)
    private String email;
    @Column(nullable = false, name = "student_id")
    private int studentId;
    @Column(name = "login_id")
    private int loginId;

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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }
    public int getLoginId() {
        return loginId;
    }

    public void setLoginId(int loginId) {
        this.loginId = loginId;
    }
}
