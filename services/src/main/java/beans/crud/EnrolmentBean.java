package beans.crud;

import entities.Enrolment;
import entities.Student;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class EnrolmentBean {

    private final Logger log = Logger.getLogger(this.getClass().getName());

    @PersistenceContext(unitName = "sis-jpa")
    private EntityManager em;

    @Transactional
    public Enrolment putEnrolment(Enrolment en){
        log.info("Putting: " + en.toString());
        em.persist(en);
        em.flush();
        return en;
    }

    @Transactional
    public Enrolment getEnrolmentById(int id) {
        log.info("Getting by id: " + id);
        Enrolment en = em.find(Enrolment.class, id);
        if(en == null) throw new NoResultException("No enrolment by this id");
        return en;
    }



}
