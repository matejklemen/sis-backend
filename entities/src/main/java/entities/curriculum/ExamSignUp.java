package entities.curriculum;

import javax.persistence.*;
import java.io.Serializable;

/* Prijava */
@Entity(name = "exam_sign_up")
@NamedQueries(value = {
        @NamedQuery(name = "ExamSignUp.getByCourseIdAndExamTermDate", query = "SELECT es FROM exam_sign_up es WHERE es.studentCourses.course.id = :id_course AND es.courseExamTerm.datetime = :exam_date"),
        @NamedQuery(name = "ExamSignUp.getByCourseIdAndStudentId", query = "SELECT es FROM exam_sign_up es WHERE es.studentCourses.course.id = :id_course AND es.studentCourses.enrolment.student.id = :id_student AND es.returned = false ORDER BY es.courseExamTerm.datetime ASC"),
        @NamedQuery(name = "ExamSignUp.getStudentTermSignUp", query = "SELECT es FROM exam_sign_up es WHERE es.studentCourses.enrolment.student.registerNumber = :student_registration AND es.courseExamTerm.datetime = :exam_date AND es.returned = false"),
        @NamedQuery(name = "ExamSignUp.getLastSignUp", query = "SELECT es FROM exam_sign_up es WHERE es.studentCourses.course.id = :course_id AND es.studentCourses.enrolment.student.id = :student_id AND es.returned = false ORDER BY es.courseExamTerm.datetime DESC"),
        @NamedQuery(name = "ExamSignUp.getByStudentIdAndCourseIdAndGrade", query = "SELECT es FROM exam_sign_up es WHERE es.studentCourses.enrolment.student.id = :student_id AND es.studentCourses.course.id = :course_id AND es.writtenScore > :grade"),

        @NamedQuery(name = "ExamSignUp.getNumberOfExamTakingsInLatestEnrolment", query = "SELECT COUNT(es) FROM exam_sign_up es WHERE es.studentCourses.idStudentCourses = :student_courses_id AND es.returned = false"),
        @NamedQuery(name = "ExamSignUp.getNumberOfExamTakingsInAllEnrolments", query = "SELECT COUNT(es) FROM exam_sign_up es WHERE es.studentCourses.enrolment.student.id = :student_id AND es.studentCourses.course.id = :course_id AND es.returned = false AND es.studentCourses.enrolment.id <> :enrolment_id"),
        @NamedQuery(name = "ExamSignUp.getNumberOfExamTakingsBeforeStudyYear", query = "SELECT COUNT(es) FROM exam_sign_up es WHERE es.studentCourses.idStudentCourses = :student_courses_id AND es.studentCourses.enrolment.studyYear.id < :studyYearId AND es.returned = false"),


        @NamedQuery(name = "ExamSignUp.getExamSignUp", query = "SELECT es FROM exam_sign_up es WHERE es.courseExamTerm.id = :course_exam_term_id AND es.studentCourses.idStudentCourses = :student_course_id"),
        @NamedQuery(name = "ExamSignUp.getExamSignUpWithReturn", query = "SELECT es FROM exam_sign_up es WHERE es.courseExamTerm.id = :course_exam_term_id AND es.studentCourses.idStudentCourses = :student_course_id AND es.returned = :returned"),

        @NamedQuery(name = "ExamSignUp.checkIfAlreadySignedUpAndNotReturned", query = "SELECT es FROM exam_sign_up es WHERE es.courseExamTerm.id = :course_exam_term_id AND es.studentCourses.idStudentCourses = :student_course_id AND es.returned = false"),
})
public class ExamSignUp implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "id_course_exam_term")
    private CourseExamTerm courseExamTerm;

    @ManyToOne
    @JoinColumn(name = "id_student_course")
    private StudentCourses studentCourses;

    // Written exam score, in range [1 - 100], can be null
    @Column(name = "written_score")
    private Integer writtenScore;

    // Grade that was given according to written exam score and (sometimes) additional work during the semester,
    // a "suggested grade", if you will
    @Column(name = "suggested_grade")
    private Integer suggestedGrade;

    // the student decided not to attend the exam and cancelled it in time
    private boolean returned;

    @Column(columnDefinition="boolean default 'true'")
    private boolean confirmed = true;

    public CourseExamTerm getCourseExamTerm() {
        return courseExamTerm;
    }

    public void setCourseExamTerm(CourseExamTerm courseExamTerm) {
        this.courseExamTerm = courseExamTerm;
    }

    public Integer getWrittenScore() {
        return writtenScore;
    }

    public void setWrittenScore(Integer grade) {
        this.writtenScore = grade;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
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

    public Integer getSuggestedGrade() {
        return suggestedGrade;
    }

    public void setSuggestedGrade(Integer currFinalGrade) {
        this.suggestedGrade = currFinalGrade;
    }
}
