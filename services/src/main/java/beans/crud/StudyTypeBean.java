package beans.crud;

import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;
import entities.StudyType;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class StudyTypeBean {

    private final Logger log = Logger.getLogger(this.getClass().getName());

    @PersistenceContext(unitName = "sis-jpa")
    private EntityManager em;

    @Transactional
    public List<StudyType> getStudyTypes(QueryParameters query) {
        List<StudyType> studyTypes = JPAUtils.queryEntities(em, StudyType.class, query);

        return studyTypes;
    }

    @Transactional
    public List<StudyType> getDeletedStudyTypes() {
        TypedQuery<StudyType> q = em.createNamedQuery("StudyType.getDeleted", StudyType.class);
        //q.setMaxResults(100);
        return q.getResultList();
    }


    @Transactional
    public StudyType getStudyType(int id) {
        StudyType c = em.find(StudyType.class, id);
        if(c == null) throw new NoResultException("No study type by this id");
        return c;
    }

    @Transactional
    public boolean existsStudyType(int id) {
        return em.find(StudyType.class, id) != null;
    }

    @Transactional
    public StudyType insertStudyType(StudyType c) {
        em.persist(c);
        em.flush();
        return c;
    }

    @Transactional
    public void deleteStudyType(int id) {
        StudyType c = em.find(StudyType.class, id);
        if(c != null) {
            c.setDeleted(!c.getDeleted());
            em.merge(c);
        } else {
            throw new NoResultException("Course by ID doesn't exist");
        }
    }

    @Transactional
    public StudyType updateStudyType(StudyType c) {
        em.merge(c);
        em.flush();
        return c;
    }

}
