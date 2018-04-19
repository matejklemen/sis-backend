package entities.curriculum;

import entities.Enrolment;
import interfaces.Codelistable;

import javax.persistence.*;
import java.io.Serializable;

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

    @ManyToOne
    @JoinColumn(name = "id_enrolment")
    private Enrolment enrolment;

    @ManyToOne
    @JoinColumn(name = "id_course")
    private Course course;

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

}
