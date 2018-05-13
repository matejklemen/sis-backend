package beans.crud;

import entities.Enrolment;
import entities.curriculum.ExamSignUp;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class ExamSignUpBean {
    /* Warning: untested */

    private final Logger log = Logger.getLogger(this.getClass().getName());

    @PersistenceContext(unitName = "sis-jpa")
    private EntityManager em;

    @Inject
    private EnrolmentBean eb;

    @Inject StudyYearBean syb;

    public List<ExamSignUp> getExamSignUpsForCourse(int idCourse) {
        TypedQuery<ExamSignUp> q = em.createQuery("SELECT esu FROM exam_sign_up esu WHERE " +
                "esu.studentCourses.course.id = :id_course AND esu.returned = false", ExamSignUp.class);

        q.setParameter("id_course", idCourse);

        return q.getResultList();
    }

    // TODO: rewrite this (examDate used to be written in unix timestamp)
    public List<ExamSignUp> getExamSignUpsForExamTerm(int courseId, long examDate) {
        TypedQuery<ExamSignUp> q = em.createNamedQuery("ExamSignUp.getByCourseIdAndExamTermDate", ExamSignUp.class);

        q.setParameter("id_course", courseId);
        q.setParameter("exam_date", examDate);

        return q.getResultList();
    }

    public Integer getNumberOfExamTakingsInLatestEnrolment(int studentCoursesId) {
        int num = ((Number)em.createNamedQuery("ExamSignUp.getNumberOfExamTakingsInLatestEnrolment", Integer.class).setParameter("student_courses_id", studentCoursesId).getSingleResult()).intValue();
        return num;
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

    /*
        TODO: rewrite this (and update doc)
        - examDate is exam date in UNIX timestamp format (i.e. number of seconds since 1. 1. 1970)
        - we do not physically delete exam sign ups, we invalidate them
    */
    @Transactional
    public void invalidateExamSignUp(long examDate, String studentRegistration) {
        TypedQuery<ExamSignUp> q = em.createNamedQuery("ExamSignUp.getStudentTermSignUp", ExamSignUp.class);

        q.setParameter("student_registration", studentRegistration);
        q.setParameter("exam_date", examDate);

        // should only be 1 -> otherwise, there is a mistake (2 non-invalidated sign ups for same exam term can not exist)
        ExamSignUp es = q.getSingleResult();
        es.setReturned(true);

        try {
            em.merge(es);
        }
        catch(Exception e) {
            log.severe("Something went wrong when trying to insert new ExamSignUp!");
        }
    }

    @Transactional
    public ExamSignUp updateExamSignUp(ExamSignUp esu){
        log.info("Will update exam sign up with id: "+esu.getId());

        em.merge(esu);
        em.flush();
        return esu;
    }
}
