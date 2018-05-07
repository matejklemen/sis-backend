package beans.logic;

import beans.crud.*;
import entities.Enrolment;
import entities.Student;
import entities.curriculum.CourseExamTerm;
import entities.curriculum.ExamSignUp;
import entities.curriculum.StudentCourses;
import org.eclipse.persistence.jpa.jpql.parser.DateTime;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.ws.rs.NotFoundException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

    public List<String> addExamSignUp(Integer studentId, Integer studentCoursesId, Integer courseExamTermId) {
        List<String> errors = new ArrayList<>();

        Enrolment en = enb.getLastEnrolmentByStudentId(studentId);
        StudentCourses sc = scb.getStudentCourses(studentCoursesId);
        CourseExamTerm cet = cetb.getExamTermById(courseExamTermId);
        if(en.getId() != sc.getEnrolment().getId()) {
            throw new NotFoundException("Invalid studentId and studentCoursesId combination");
        }

        if(cet.getCourseOrganization().getCourse().getId() != sc.getCourse().getId()) {
            throw new NotFoundException("Invalid studentCoursesId and courseExamTermId combination");
        }

        /* preveri ce je potekel rok za prijavo (2 dni prej do polnoci)*/
        if(ExamSignUpDeadlineReached(cet.getDatetimeObject())) {
            errors.add("Rok za prijavo je potekel");
        }

        log.info(String.valueOf(esub.getNumberOfExamTakingsInLatestEnrolment(sc.getIdStudentCourses())));

        if(errors.isEmpty()) {
            ExamSignUp esu = new ExamSignUp();
            esu.setStudentCourses(sc);
            esu.setExamTerm(cet);

            esub.addExamSignUp(esu);
        }

        return errors;
    }

    private boolean ExamSignUpDeadlineReached(Timestamp deadline) {
        Timestamp twoDaysBeforeDeadline = new Timestamp(deadline.getTime() - (1000 * 60 * 60 * 24 * 2));
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(twoDaysBeforeDeadline.getTime());
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        twoDaysBeforeDeadline.setTime(c.getTimeInMillis());
        Calendar t = Calendar.getInstance();
        Timestamp today = new Timestamp(t.getTimeInMillis());
        if(today.after(twoDaysBeforeDeadline)) {
            return true;
        } else {
            return false;
        }
    }
}
