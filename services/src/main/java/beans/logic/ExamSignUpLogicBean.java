package beans.logic;

import beans.crud.*;
import entities.Enrolment;
import entities.Student;
import entities.curriculum.CourseExamTerm;
import entities.curriculum.ExamSignUp;
import entities.curriculum.StudentCourses;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class ExamSignUpLogicBean {
    private final Logger log = Logger.getLogger(this.getClass().getName());

    @Inject StudentBean sb;
    @Inject EnrolmentBean enb;
    @Inject StudentCoursesBean scb;
    @Inject
    ExamSignUpBean esub;
    @Inject
    CourseExamTermBean cetb;

    public List<StudentCourses> getCoursesByRegisterNumber(String registerNumber) {
        /*
            Throws exceptions if there are no results found for the queries preceding the last one.
            If the last query obtains no result, then null is returned.
        */
        Student student = sb.getStudentByRegisterNumber(registerNumber);

        if(student == null)
            throw new NoResultException(String.format("No student with given register number: %s", registerNumber));

        Enrolment en = enb.getLastEnrolmentByStudentId(student.getId());

        if(en == null)
            throw new NoResultException(String.format("No enrolment found for student with given id: %d", student.getId()));

        List<StudentCourses> courses = scb.getStudentCoursesByEnrolmentId(en.getId());

        return courses;
    }

    public ExamSignUp addExamSignUp(Integer studentId, Integer studentCoursesId, Integer courseExamTermId) {
        Enrolment en = enb.getLastEnrolmentByStudentId(studentId);
        StudentCourses sc = scb.getStudentCourses(studentCoursesId);
        CourseExamTerm cet = cetb.getExamTermById(courseExamTermId);

        if(en.getId() != sc.getEnrolment().getId()) {
            throw new NotFoundException(String.format("Invalid studentId and studentCoursesId combination"));
        }

        ExamSignUp esu = new ExamSignUp();
        esu.setStudentCourses(sc);
        esu.setExamTerm(cet);

        esub.addExamSignUp(esu);
        return esu;
    }
}
