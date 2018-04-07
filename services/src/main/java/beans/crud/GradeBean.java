package beans.crud;

import entities.Grade;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class GradeBean {

    private final Logger log = Logger.getLogger(this.getClass().getName());

    @PersistenceContext(unitName = "sis-jpa")
    private EntityManager em;

    @Transactional
    public List<Grade> getGradesByStudentId(int studentId) {
        TypedQuery<Grade> q = em.createNamedQuery("Grade.getByStudentId", Grade.class).setParameter("id",studentId);
        return q.getResultList();
    }
}
