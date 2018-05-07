package entities.curriculum;

import javax.persistence.*;
import java.io.Serializable;

/* Prijava */
@Entity(name = "exam_sign_up")
@NamedQueries(value = {
        @NamedQuery(name = "ExamSignUp.getByCourseIdAndExamTermDate", query = "SELECT es FROM exam_sign_up es WHERE es.studentCourses.course.id = :id_course AND es.examTerm.datetime = :exam_date"),
        @NamedQuery(name = "ExamSignUp.getByCourseIdAndStudentId", query = "SELECT es FROM exam_sign_up es WHERE es.studentCourses.course.id = :id_course AND es.studentCourses.enrolment.student.id = :id_student AND es.returned = false"),
        @NamedQuery(name = "ExamSignUp.getStudentTermSignUp", query = "SELECT es FROM exam_sign_up es WHERE es.studentCourses.enrolment.student.registerNumber = :student_registration AND es.examTerm.datetime = :exam_date AND es.returned = false")
})
public class ExamSignUp implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "id_course_exam_term")
    private CourseExamTerm examTerm;

    @ManyToOne
    @JoinColumn(name = "id_student_course")
    private StudentCourses studentCourses;

    // the student decided not to attend the exam and cancelled it in time
    private boolean returned;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public CourseExamTerm getExamTerm() {
        return examTerm;
    }

    public void setExamTerm(CourseExamTerm examTerm) {
        this.examTerm = examTerm;
    }

    public StudentCourses getStudentCourses() {
        return studentCourses;
    }

    public void setStudentCourses(StudentCourses studentCourses) {
        this.studentCourses = studentCourses;
    }

    public boolean isReturned() {
        return returned;
    }

    public void setReturned(boolean returned) {
        this.returned = returned;
    }
}
