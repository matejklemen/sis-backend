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
        log.info("Putting: " + et.toString());
        em.persist(et);
        em.flush();
        return et;

    }

}
