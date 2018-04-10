package beans.crud;

import entities.curriculum.ExamSignUp;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class ExamSignUpBean {

    private final Logger log = Logger.getLogger(this.getClass().getName());

    @PersistenceContext(unitName = "sis-jpa")
    private EntityManager em;

    /* examDate is exam date in UNIX timestamp format (i.e. number of seconds since 1. 1. 1970) */
    public List<ExamSignUp> getExamSignUpsForExamTerm(int courseId, long examDate) {
        TypedQuery<ExamSignUp> q = em.createNamedQuery("ExamSignUp.getByCourseIdAndExamTermDate", ExamSignUp.class);

        q.setParameter("id_course", courseId);
        q.setParameter("exam_date", examDate);

        return q.getResultList();
    }

    public List<ExamSignUp> getExamSignUpsOnCourseForStudent(int courseId, String studentRegistration) {
        TypedQuery<ExamSignUp> q = em.createNamedQuery("ExamSignUp.getByCourseIdAndStudentRegisterNumber", ExamSignUp.class);

        log.info(String.format("Student registration: %s, course ID: %d", studentRegistration, courseId));

        q.setParameter("id_course", courseId);
        q.setParameter("student_registration", studentRegistration);

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
}
