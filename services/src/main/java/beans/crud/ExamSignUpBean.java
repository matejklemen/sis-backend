package beans.crud;

import beans.logic.ExamSignUpLogicBean;
import entities.Enrolment;
import entities.curriculum.ExamSignUp;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class ExamSignUpBean {
    /* Warning: untested */

    private final Logger log = Logger.getLogger(this.getClass().getName());

    @PersistenceContext(unitName = "sis-jpa")
    private EntityManager em;

    @Inject private EnrolmentBean eb;
    @Inject private StudyYearBean syb;
    @Inject private ExamSignUpLogicBean signUpLogicBean;


    public List<ExamSignUp> getExamSignUpsForCourse(int idCourse) {
        TypedQuery<ExamSignUp> q = em.createQuery("SELECT esu FROM exam_sign_up esu WHERE " +
                "esu.studentCourses.course.id = :id_course AND esu.returned = false", ExamSignUp.class);

        q.setParameter("id_course", idCourse);

        return q.getResultList();
    }

    public Integer getNumberOfExamTakingsInLatestEnrolment(int studentCoursesId) {
        int num = ((Number)em.createNamedQuery("ExamSignUp.getNumberOfExamTakingsInLatestEnrolment", Integer.class).setParameter("student_courses_id", studentCoursesId).getSingleResult()).intValue();
        return num;
    }

    public Integer getNumberOfExamTakingsBeforeDatetime(int studentCoursesId, Timestamp dt) {
        // includes the current taking of exam!
        TypedQuery<Integer> q = em.createQuery("SELECT COUNT(es) FROM exam_sign_up es WHERE " +
                "es.studentCourses.idStudentCourses = :student_courses_id AND " +
                "es.courseExamTerm.datetime <= :specified_datetime AND " +
                "es.returned = false", Integer.class);

        q.setParameter("student_courses_id", studentCoursesId);
        q.setParameter("specified_datetime", dt);

        Integer res = ((Number)q.getSingleResult()).intValue();

        return res;
    }

    public Integer getNumberOfExamTakingsBeforeStudyYear(int studentId, int courseId, int studyYearId){
        TypedQuery<Integer> q = em.createNamedQuery("ExamSignUp.getNumberOfExamTakingsBeforeStudyYear", Integer.class)
                .setParameter("student_id", studentId)
                .setParameter("course_id", courseId)
                .setParameter("studyYearId", studyYearId);

        return ((Number)q.getSingleResult()).intValue();
    }

    public Integer getNumberOfExamTakingsInAllEnrolments(int studentId, int courseId) {
        Enrolment originalYearOfRetryYearEnrolment = eb.getOriginalYearOfRetryYearEnrolment(studentId);
        int num = ((Number)em.createNamedQuery("ExamSignUp.getNumberOfExamTakingsInAllEnrolments", Integer.class).setParameter("student_id", studentId).setParameter("course_id", courseId).setParameter("enrolment_id", originalYearOfRetryYearEnrolment == null ? -1 : originalYearOfRetryYearEnrolment.getId()).getSingleResult()).intValue();
        log.info("takings: "+String.valueOf(num));
        return num;
    }

    public ExamSignUp getLastSignUp(int courseId, int studentId) {
        List<ExamSignUp> e = em.createNamedQuery("ExamSignUp.getLastSignUp", ExamSignUp.class)
                .setParameter("course_id", courseId)
                .setParameter("student_id", studentId)
                .setMaxResults(1)
                .getResultList();

        if(e == null || e.size() == 0)
            return null;

        return e.get(0);
    }

    public boolean checkIfAlreadySignedUpAndNotReturned(int courseExamTermId, int studentCourseId) {
        TypedQuery<ExamSignUp> q = em.createNamedQuery("ExamSignUp.checkIfAlreadySignedUpAndNotReturned", ExamSignUp.class);
        q.setParameter("course_exam_term_id", courseExamTermId);
        q.setParameter("student_course_id", studentCourseId);
        List<ExamSignUp> e = q.getResultList();

        if(e.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    public ExamSignUp getLastSignUpForStudentCourse(int studentCourseId) {
        TypedQuery<ExamSignUp> q = em.createQuery("SELECT esu FROM exam_sign_up esu WHERE " +
                "esu.studentCourses.idStudentCourses = :id_student_course AND esu.returned = false " +
                "ORDER BY esu.courseExamTerm.datetime DESC", ExamSignUp.class);

        q.setParameter("id_student_course", studentCourseId);

        List<ExamSignUp> ret = q.getResultList();

        for(ExamSignUp esu: ret)
            log.info(String.format("Obtained exam sign up (date): %s", esu.getCourseExamTerm().getDatetimeObject().toString()));

        if(ret != null && ret.size() > 0)
            return ret.get(0);

        return null;
    }

    @Transactional
    public ExamSignUp getExamSignUp(int courseExamTermId, int studentCourseId){
        TypedQuery<ExamSignUp> q = em.createNamedQuery("ExamSignUp.getExamSignUp", ExamSignUp.class)
                .setParameter("course_exam_term_id", courseExamTermId)
                .setParameter("student_course_id", studentCourseId);
        List<ExamSignUp> e = q.getResultList();

        if(!e.isEmpty()) {
            return e.get(0);
        } else {
            return null;
        }
    }

    @Transactional
    public ExamSignUp getExamSignUp(int examSignUpId) {
        ExamSignUp e = em.find(ExamSignUp.class, examSignUpId);
        if(e == null) throw new NoResultException("No exam sign up by this id");
        return e;
    }

    @Transactional
    public List<ExamSignUp> getExamSignUpsByExamTerm(int idExamTerm) {
        TypedQuery<ExamSignUp> q = em.createQuery("SELECT esu FROM exam_sign_up esu WHERE " +
                "esu.courseExamTerm.id = :id_exam_term AND " +
                "esu.returned = false ORDER BY " +
                "esu.studentCourses.enrolment.student.surname, " +
                "esu.studentCourses.enrolment.student.name", ExamSignUp.class);

        q.setParameter("id_exam_term", idExamTerm);

        return q.getResultList();
    }

    @Transactional
    public List<ExamSignUp> getExamSignUpsByExamTermWithReturned(int idExamTerm) {
        TypedQuery<ExamSignUp> q = em.createQuery("SELECT esu FROM exam_sign_up esu WHERE " +
                "esu.courseExamTerm.id = :id_exam_term " +
                "ORDER BY " +
                "esu.studentCourses.enrolment.student.surname, " +
                "esu.studentCourses.enrolment.student.name", ExamSignUp.class);

        q.setParameter("id_exam_term", idExamTerm);

        return q.getResultList();
    }

    @Transactional
    public ExamSignUp getExamSignUpWithReturn(int courseExamTermId, int studentCourseId, Boolean returned) {
        TypedQuery<ExamSignUp> q = em.createNamedQuery("ExamSignUp.getExamSignUpWithReturn", ExamSignUp.class)
        .setParameter("course_exam_term_id", courseExamTermId)
        .setParameter("student_course_id", studentCourseId)
        .setParameter("returned", returned);
        List<ExamSignUp> e = q.getResultList();

        if(!e.isEmpty()) {
            return e.get(0);
        } else {
            return null;
        }
    }

    @Transactional
    public List<ExamSignUp> getByStudentIdAndCourseIdAndGrade(Integer studentId, Integer courseId, Integer grade) {
        TypedQuery<ExamSignUp> q = em.createNamedQuery("ExamSignUp.getByStudentIdAndCourseIdAndGrade", ExamSignUp.class);
        q.setParameter("student_id", studentId);
        q.setParameter("course_id", courseId);
        q.setParameter("grade", grade);
        return q.getResultList();
    }

    public List<ExamSignUp> getExamSignUpsOnCourseForStudent(int courseId, int studentId) {
        TypedQuery<ExamSignUp> q = em.createNamedQuery("ExamSignUp.getByCourseIdAndStudentId", ExamSignUp.class);

        log.info(String.format("Get exam sign-ups for studentId=%d and courseId=%d", studentId, courseId));

        q.setParameter("id_course", courseId);
        q.setParameter("id_student", studentId);

        return q.getResultList();
    }

    @Transactional
    public boolean addExamSignUp(ExamSignUp es) {
        try {
            em.persist(es);
            return true;
        }
        catch(Exception e) {
            log.severe("Something went wrong when trying to insert new ExamSignUp!");
            log.severe(e.getMessage());
            return false;
        }
    }

    public List<ExamSignUp> getExamSignUpsForStudentCourse(int idStudentCourse) {
        TypedQuery<ExamSignUp> q = em.createQuery("SELECT esu FROM exam_sign_up esu WHERE " +
                "esu.studentCourses.idStudentCourses = :id_student_course AND esu.returned = false " +
                "ORDER BY esu.courseExamTerm.datetime DESC", ExamSignUp.class);

        q.setParameter("id_student_course", idStudentCourse);

        return q.getResultList();
    }

    public List<ExamSignUp> getExamSignUpsForStudentCourseWithPositiveGrade(int idStudentCourse) {
        TypedQuery<ExamSignUp> q = em.createQuery("SELECT esu FROM exam_sign_up esu WHERE " +
                "esu.studentCourses.idStudentCourses = :id_student_course AND esu.returned = false AND esu.suggestedGrade > 5 " +
                "ORDER BY esu.courseExamTerm.datetime DESC", ExamSignUp.class);

        q.setParameter("id_student_course", idStudentCourse);

        return q.getResultList();
    }

    @Transactional
    public ExamSignUp updateExamSignUp(ExamSignUp esu){
        log.info("Will update exam sign up with id: "+esu.getId());

        esu = em.merge(esu);
        em.flush();
        return esu;
    }

    @Transactional
    public void removeExamSignUps(List<ExamSignUp> signUps) {
        for(ExamSignUp esu: signUps) {
            log.info(String.format("Removing exam sign up with ID=%d...", esu.getId()));
            em.persist(esu);
            em.remove(esu);
        }
    }

    @Transactional
    public List<String> updateScoreAndGrade(int examSignUpId, int loginId, Integer writtenScore, Integer suggestedGrade, Boolean isReturned) {
        ArrayList<String> errors = new ArrayList<>();

        ExamSignUp signUp = getExamSignUp(examSignUpId);

        if(isReturned != null) {
            if (isReturned) {
                // if sign up was returned, remove both score & grade
                signUp.setReturned(true);
                signUp.setWrittenScore(null);
                signUp.setSuggestedGrade(null);
                updateExamSignUp(signUp);
                signUpLogicBean.logExamSignUpHistory(signUp, loginId, "odjava (ob vnosu ocen)");
                return errors;
            } else {
                signUp.setReturned(false);
                updateExamSignUp(signUp);
                signUpLogicBean.logExamSignUpHistory(signUp, loginId, "prijava (ob vnosu ocen)");
                // proceed with adding score/grade
            }
        }

        boolean scoreOk = true, gradeOk = true;

        if(writtenScore != null) {
            if(writtenScore < 0 || writtenScore > 100) {
                errors.add("neveljaven vnos za točke pisnega izpita");
                scoreOk = false;
            }
        }

        if(suggestedGrade != null) {
            if(suggestedGrade < 1 || suggestedGrade > 10) {
                errors.add("neveljaven vnos za končno oceno");
                gradeOk = false;
            }
        }

        if(scoreOk) {
            signUp.setWrittenScore(writtenScore);
        }
        if(gradeOk) {
            signUp.setSuggestedGrade(suggestedGrade);
            ExamSignUp lastSignUp = getLastSignUpForStudentCourse(signUp.getStudentCourses().getIdStudentCourses());
            // if this sign up is the last sign up for the course, also write grade to studentcourses
            // TODO: what if the grade is set to null (removed)? Should then get the grade from one signup earlier, if it exists?
            if(lastSignUp.getId() == signUp.getId()) {
                signUp.getStudentCourses().setGrade(suggestedGrade);
            }
        }

        updateExamSignUp(signUp);

        return errors;
    }
}
