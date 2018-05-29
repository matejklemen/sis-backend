package entities.curriculum;

import entities.Enrolment;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
@Entity(name = "student_courses")
@NamedQueries(
        value = {
                @NamedQuery(name = "StudentCourses.getByEnrolmentId", query = "SELECT c FROM student_courses c WHERE c.enrolment = :enrolment"),
                @NamedQuery(name = "StudentCourses.getNumberOfStudentsForEachCourse", query = "SELECT COUNT(sc) FROM student_courses sc WHERE sc.course.id = :course_id AND sc.enrolment.studyYear.id = :study_year_id AND sc.enrolment.studyProgram.id = :study_program_id AND sc.enrolment.year = :year")
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
}
