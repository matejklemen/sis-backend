package beans.crud;

import entities.StudyYear;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.logging.Logger;

@ApplicationScoped
public class StudyYearBean {
    private final Logger log = Logger.getLogger(this.getClass().getName());

    @PersistenceContext(unitName = "sis-jpa")
    private EntityManager em;

    public StudyYear getStudyYearById(int id) {
        return em.find(StudyYear.class, id);
    }

    public StudyYear getStudyYearByName(String name) {
        try {
            return em.createNamedQuery("StudyProgram.getByName", StudyYear.class).setParameter("name", name).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Transactional
    public StudyYear getOrCreateStudyYear(String name) {
        StudyYear sp = getStudyYearByName(name);
        if(sp == null) {
            sp = new StudyYear();
            sp.setName(name);
            em.persist(sp);
            em.flush();
            return sp;
        }
        return sp;
    }
}
