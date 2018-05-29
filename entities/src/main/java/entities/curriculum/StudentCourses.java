package entities.curriculum;

import entities.Enrolment;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.DateFormat;

@XmlAccessorType(XmlAccessType.FIELD)
@Entity(name = "student_courses")
@NamedQueries(
        value = {
                @NamedQuery(name = "StudentCourses.getByEnrolmentId", query = "SELECT c FROM student_courses c WHERE c.enrolment = :enrolment")
        }
)
public class StudentCourses implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_student_courses")
    private int idStudentCourses;

    @XmlTransient
    @ManyToOne
    @JoinColumn(name = "id_enrolment")
    private Enrolment enrolment;

    @ManyToOne
    @JoinColumn(name = "id_course")
    private Course course;

    private Integer grade;

    @Transient
    private Timestamp dateOfGrade;

    @Transient
    private int year;

    public int getIdStudentCourses() {
        return idStudentCourses;
    }

    public void setIdStudentCourses(int idStudentCourses) {
        this.idStudentCourses = idStudentCourses;
    }

    public Enrolment getEnrolment() {
        return enrolment;
    }

    public void setEnrolment(Enrolment enrolment) {
        this.enrolment = enrolment;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public Timestamp getDateOfGrade() {
        return dateOfGrade;
    }

    public void setDateOfGrade(Timestamp dateOfGrade) {
        this.dateOfGrade = dateOfGrade;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
