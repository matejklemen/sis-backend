package beans.crud;

import entities.curriculum.PartOfCurriculum;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.NamedQuery;
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

    // type should be one of {"obv", "mod", "siz", "piz"}. See class PartOfCurriculum for details
    public List<PartOfCurriculum> getByType(String type) {
        TypedQuery<PartOfCurriculum> q = em.createNamedQuery("Enrolment.getLastByStudentId", PartOfCurriculum.class);

        return q.getResultList();
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
}
