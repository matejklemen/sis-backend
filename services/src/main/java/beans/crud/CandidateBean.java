package beans.crud;

import entities.Candidate;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class CandidateBean {

    private final Logger log = Logger.getLogger(this.getClass().getName());

    @PersistenceContext(unitName = "sis-jpa")
    private EntityManager em;


    @Transactional
    public Candidate getCandidateByLoginId(int loginId) {
        log.info("Getting candidate by loginId");
        try{
            Query q = em.createNamedQuery("Candidate.getByLoginId", Candidate.class);
            q.setParameter("loginId", loginId);
            return (Candidate) q.getSingleResult();
        } catch(NoResultException e) {
            return null;
        }
    }

    @Transactional
    public Candidate putCandidate(Candidate c) {
        log.info("Putting candidate with id: "+c.getId());
        em.persist(c);
        em.flush();
        return c;
    }
}