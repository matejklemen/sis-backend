package beans.logic;

import beans.crud.*;
import entities.*;
import entities.curriculum.CourseExamTerm;
import entities.curriculum.ExamSignUp;
import entities.curriculum.StudentCourses;
import pojo.ResponseError;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.ws.rs.core.Response;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class ExamSignUpLogicBean {

    private final Logger log = Logger.getLogger(this.getClass().getName());

    @Inject private StudentBean sb;
    @Inject private EnrolmentBean enb;
    @Inject private StudentCoursesBean scb;
    @Inject private ExamSignUpBean esub;
    @Inject private CourseExamTermBean cetb;
    @Inject private UserLoginBean ulb;
    @Inject private ExamSignUpHistoryBean esuhb;
    @Inject private StaffBean stb;

    public List<StudentCourses> getCoursesByRegisterNumber(String registerNumber) {
        /*
            Throws exceptions if there are no results found for the queries preceding the last one.
            If the last query obtains no result, then null is returned.
        */
        Student student = sb.getStudentByRegisterNumber(registerNumber);

        if (student == null)
            throw new NoResultException(String.format("No student with given register number: %s", registerNumber));

        Enrolment en = enb.getLastEnrolmentByStudentId(student.getId());

        if (en == null)
            throw new NoResultException(String.format("No enrolment found for student with given id: %d", student.getId()));

        List<StudentCourses> courses = scb.getStudentCoursesByEnrolmentId(en.getId());

        return courses;
    }

    public List<String> addExamSignUp(Integer studentId, Integer studentCoursesId, Integer courseExamTermId, Integer userLoginId, Boolean force) {
        List<String> errors = new ArrayList<>();

        Enrolment en = enb.getLastEnrolmentByStudentId(studentId);
        StudentCourses sc = scb.getStudentCourses(studentCoursesId);
        CourseExamTerm cet = cetb.getExamTermById(courseExamTermId);

        boolean sudo = false;

        // Force
        if(userLoginId == null || ulb.getUserLoginInfoByUserLoginId(userLoginId).getRole().getId() != 4 || force == null || !force) {
            if(en == null) {
                errors.add("študent z id-jem "+studentId+" ne obstaja");
            }

            if(sc == null) {
                errors.add("študentov predmet z id-jem "+studentCoursesId+" ne obstaja");
            }

            if(cet == null) {
                errors.add("izpitni rok z id-jem "+courseExamTermId+" ne obstaja ali je izbrisan");
            }

            if(!errors.isEmpty()) {
                return errors;
            }

            if(en.getId() != sc.getEnrolment().getId()) {
                errors.add("napačna kombinacija studentId in studentCoursesId");
                return errors;
            }

            if (cet.getCourseOrganization().getCourse().getId() != sc.getCourse().getId()) {
                errors.add("napačna kombinacija studentCoursesId in courseExamTermId");
                return errors;
            }

            /* preveri ce je potekel rok za prijavo (2 dni prej do polnoci)*/
            if (examSignUpDeadlineReached(cet.getDatetimeObject())) {
                errors.add("rok za prijavo je potekel");
            }

            /* preveri ce je student ze porabil kvoto treh polaganj izpita za najnovejš vpis */
            if (esub.getNumberOfExamTakingsInLatestEnrolment(sc.getIdStudentCourses()) > 2) {
                errors.add("presežena kvota izvajanja izpitov za dani predmet v tem šolskem letu");
            }

            /* preveri ce je student ze porabil kvoto sestih polaganj izpita za vse njegove vpise */
            if (esub.getNumberOfExamTakingsInAllEnrolments(studentId, sc.getCourse().getId()) > 5) {
                errors.add("presežena kvota izvajanja izpitov za dani predmet");
            }

            /* preveri za prijavo na ze opravljen izpit */
            if (!esub.getByStudentIdAndCourseIdAndGrade(studentId, sc.getCourse().getId(), 5).isEmpty()) {
                errors.add("predmet je že opravljen in pozitivno zaključen");
            }

            /*preveri ce je od zadnjega polaganja minilo 14 dni */
            if (esub.getLastSignUp(sc.getCourse().getId(), studentId) != null) {
                Timestamp lastTermDate = esub.getLastSignUp(sc.getCourse().getId(), studentId).getCourseExamTerm().getDatetimeObject();
                Timestamp newTermDate = cet.getDatetimeObject();
                // ce se hoce prijaviti npr. na prvi rok medtem ko obstaja prijava na drugi rok, zavrni
                if(newTermDate.before(lastTermDate)) {
                    errors.add("obstaja poznejša prijava na izpit pri tem predmetu");
                } else if (!fortnitePassed(lastTermDate, newTermDate)) {
                    errors.add("ni še minilo 14 dni od polaganja zadnjega izpita iz tega predmeta");
                }
            }

            /*preveri ce je student ze prijavljen na dani rok */
            if (esub.checkIfAlreadySignedUpAndNotReturned(courseExamTermId, studentId)) {
                errors.add("študent je na ta izpit že prijavljen");
            }

            /*Preveri za prijavo, kjer za prejsnji rok se ni bila zakljucena ocena */
            if(esub.getLastSignUp(sc.getCourse().getId(), studentId) != null && esub.getLastSignUp(sc.getCourse().getId(), studentId).getGrade() == null) {
                errors.add("ocena za prejšnji rok še ni bila zaključena");
            }

        } else {
            sudo = true;
        }

        if(errors.isEmpty()) {
            ExamSignUp esu = esub.getExamSignUp(courseExamTermId, studentCoursesId);

            if (esu != null) {
                esu.setReturned(false);
                if(sudo) {
                    esu.setConfirmed(true);
                }
                esub.updateExamSignUp(esu);
            } else {
                esu = new ExamSignUp();

                /*nastavi confirmed flag, ce mora student placati izpit*/
                if(en.getKind().getName().equals("izredni") || esub.getNumberOfExamTakingsInAllEnrolments(studentId, sc.getCourse().getId()) > 2) {
                    esu.setConfirmed(false);
                }

                esu.setStudentCourses(sc);
                esu.setCourseExamTerm(cet);

                esub.addExamSignUp(esu);
            }

            ExamSignUpHistory esuh = new ExamSignUpHistory();
            esuh.setDatetime(new Timestamp(System.currentTimeMillis()));
            esuh.setExamSignUp(esu);
            if(sudo)
                esuh.setAction("prisilna prijava");
            else
                esuh.setAction("prijava");
            esuh.setUserLogin(ulb.getUserLoginById(userLoginId));
            esuhb.insertNewExamSignUpHistory(esuh);
        }
        return errors;
    }

    public List<String> returnExamSignUp(Integer courseExamTermId, Integer studentCourseId, Integer loginId, Boolean force){
        List<String> errors = new ArrayList<>();

        ExamSignUp esu = esub.getExamSignUp(courseExamTermId, studentCourseId);
        CourseExamTerm cet = cetb.getExamTermById(courseExamTermId);

        if(esu.getGrade() != null) {
            errors.add("ocena za ta izpitni rok je že vpisana");
        }

        if(esu.getStudentCourses().getGrade() != null){
            errors.add("ocena za ta predmet je že vpisana");
        }

        if(force == null || !force || loginId == null || loginId != 4) {
            if (!examSignUpDeadlineReached(cet.getDatetimeObject())) {
                esu.setReturned(true);
                esu = esub.updateExamSignUp(esu);

                ExamSignUpHistory esuh = new ExamSignUpHistory();
                esuh.setDatetime(new Timestamp(System.currentTimeMillis()));
                esuh.setExamSignUp(esu);
                esuh.setAction("odjava");

                esuh.setUserLogin(ulb.getUserLoginById(loginId));
                esuhb.insertNewExamSignUpHistory(esuh);
            } else {
                errors.add("rok za odjavo je potekel");
            }
        }

        return errors;
    }

    public List<ExamSignUpHistory> getExamSignUpHistry(ExamSignUp esu){
        List<ExamSignUpHistory> esuhl = esuhb.getExamSignUpHistoryByExamSignUpId(esu.getId(), 30);

        for (ExamSignUpHistory e : esuhl){
            //proba v referetu in potem v st
            int role = e.getUserLogin().getRole().getId();
            if(role == 2){
                Student s = sb.getStudentByLoginId(e.getUserLogin().getId());
                e.setName(s.getName() + " " + s.getSurname());
            }else if(role == 4){
                Staff st = stb.getStaffByLoginId(e.getUserLogin().getId());
                e.setName(st.getFirstName() + " " + st.getLastName1() + " " + st.getLastName2());
            }

        }
        return esuhl;
    }

    /* Helpers */

    private boolean examSignUpDeadlineReached(Timestamp deadline) {
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
        if (today.after(twoDaysBeforeDeadline)) {
            return true;
        } else {
            return false;
        }
    }

    private boolean fortnitePassed(Timestamp lastTaking, Timestamp newTaking) {
        Timestamp fortniteAfterLastTaking = new Timestamp(lastTaking.getTime() + (1000 * 60 * 60 * 24 * 14));
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(fortniteAfterLastTaking.getTime());
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        fortniteAfterLastTaking.setTime(c.getTimeInMillis());
        // true if new exam term date is more than 14 days after last taken exam term
        return newTaking.after(fortniteAfterLastTaking);
    }
}
