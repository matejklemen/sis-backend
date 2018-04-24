package beans.crud;

import entities.EnrolmentToken;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.logging.Logger;

@ApplicationScoped
public class EnrolmentTokenBean {

    private final Logger log = Logger.getLogger(this.getClass().getName());


    @PersistenceContext(unitName = "sis-jpa")
    private EntityManager em;

    @Transactional
    public EnrolmentToken putEnrolmentToken(EnrolmentToken et){
        log.info("Putting enrolment token with student id: " + et.getStudent().getId());
        em.persist(et);
        em.flush();
        return et;

    }

    @Transactional
    public EnrolmentToken getLastEnrolmentTokenByStudentId(int id){
        log.info("Geting last enrolment token for student id: " + id);
        return em.createNamedQuery("EnrolmentToken.getLastByStudentId", EnrolmentToken.class)
                .setParameter("id", id)
                .setMaxResults(1)
                .getSingleResult();
    }

    @Transactional
    public EnrolmentToken getEnrolmentTokenById(int id){
        log.info("Geting enrolment token for id: " + id);
        return em.createNamedQuery("EnrolmentToken.getById", EnrolmentToken.class).setParameter("id", id).getSingleResult();
    }

    @Transactional
    public void deleteEnrolmentToken(int id){
        log.info("Deleting enrolment token with id: " + id);
        EnrolmentToken et = em.find(EnrolmentToken.class, id);
        if(et != null){
            em.remove(et);
        }
    }

    @Transactional
    public EnrolmentToken updateEnrolmentToken(EnrolmentToken et){
        log.info("Posting enrolment token with id: " + et.getStudent());
        em.merge(et);
        em.flush();
        return et;
    }
}
