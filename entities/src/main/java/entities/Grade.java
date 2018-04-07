package entities;

import entities.curriculum.Course;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlIDREF;
import java.io.Serializable;

@Entity(name = "grade")
@NamedQueries(
        value = {
                @NamedQuery(name = "Grade.getByStudentId", query = "SELECT g FROM grade g WHERE g.student.id = :id"),
        }
)
public class Grade implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "id_student")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "course")
    private Course course;

    @Column(nullable = false)
    private int grade;

    @Column(nullable = false)
    private int attempts;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @XmlIDREF
    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    @XmlIDREF
    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public int getAttempts() {
        return attempts;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }
}
