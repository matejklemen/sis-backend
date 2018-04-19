package beans.crud;

import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;
import entities.curriculum.PartOfCurriculum;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class PartOfCurriculumBean {

    private final Logger log = Logger.getLogger(this.getClass().getName());

    @PersistenceContext(unitName = "sis-jpa")
    private EntityManager em;

    @Transactional
    public List<PartOfCurriculum> getAllPOC(QueryParameters query) {
        List<PartOfCurriculum> pocs = JPAUtils.queryEntities(em, PartOfCurriculum.class, query);
        return pocs;
    }

    @Transactional
    public PartOfCurriculum insertNewModule(String moduleName) {
        PartOfCurriculum poc = new PartOfCurriculum();

        poc.setType("mod");
        poc.setModuleName(moduleName);

        try {
            em.persist(poc);
            log.info("Successfully added new module.");

            // returns the same entity with the addition of newly generated ID
            return poc;
        }
        catch (Exception e) {
            log.info("Something went wrong when trying to insert new module (PartOfCurriculum)!");
            return null;
        }
    }

    @Transactional
    public boolean existsPartOfCurriculum(int id) {
        return em.find(PartOfCurriculum.class, id) != null;
    }

    @Transactional
    public PartOfCurriculum insertPartOfCurriculum(PartOfCurriculum c) {
        em.persist(c);
        em.flush();
        return c;
    }

    @Transactional
    public void deletePartOfCurriculum(int id) {
        PartOfCurriculum c = em.find(PartOfCurriculum.class, id);
        if(c != null) {
            c.setDeleted(!c.getDeleted());
            em.merge(c);
        } else {
            throw new NoResultException("Course by ID doesn't exist");
        }
    }

    @Transactional
    public PartOfCurriculum updatePartOfCurriculum(PartOfCurriculum c) {
        em.merge(c);
        em.flush();
        return c;
    }
}
