package beans.crud;

import entities.ExamSignUpHistory;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class ExamSignUpHistoryBean {

    private final Logger log = Logger.getLogger(this.getClass().getName());

    @PersistenceContext(unitName = "sis-jpa")
    private EntityManager em;

    @Transactional
    public ExamSignUpHistory insertNewExamSignUpHistory(ExamSignUpHistory esuh){
        log.info("Will insert new ExamSIgnUpHistory");

        em.persist(esuh);
        em.flush();
        return esuh;
    }

    @Transactional
    public List<ExamSignUpHistory> getExamSignUpHistoryByExamSignUpId(int id, int limit){
        log.info("Will get ExamSignUpHistory by ExamSignUpId: " + id);

        TypedQuery<ExamSignUpHistory> q = em.createNamedQuery("ExamSignUpHistory.getByExamSignUpId", ExamSignUpHistory.class);
        q.setParameter("exam_sign_up_id", id)
        .setMaxResults(limit);
        return q.getResultList();
    }
}


