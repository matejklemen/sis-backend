package beans.crud;

import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;
import entities.StudyDegree;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class StudyDegreeBean {

    @PersistenceContext(unitName = "sis-jpa")
    private EntityManager em;

    @Transactional
    public StudyDegree getStudyDegree(String id) {
        StudyDegree pa = em.find(StudyDegree.class, id);
        if(pa == null) throw new NoResultException("No study degree by this id");
        return pa;
    }

    @Transactional
    public List<StudyDegree> getStudyDegrees(QueryParameters query) {
        List<StudyDegree> studyDegrees = JPAUtils.queryEntities(em, StudyDegree.class, query);

        return studyDegrees;
    }

    @Transactional
    public List<StudyDegree> getDeletedStudyDegrees() {
        return em.createNamedQuery("StudyDegree.getDeleted", StudyDegree.class).getResultList();
    }

    @Transactional
    public boolean existsStudyDegree(String id) {
        return em.find(StudyDegree.class, id) != null;
    }

    @Transactional
    public StudyDegree insertStudyDegree(StudyDegree e) {
        em.persist(e);
        em.flush();
        return e;

    }

    @Transactional
    public void deleteStudyDegree(String id) {
        StudyDegree c = em.find(StudyDegree.class, id);
        if(c != null) {
            c.setDeleted(!c.getDeleted());
            em.merge(c);
        } else {
            throw new NoResultException("Course by ID doesn't exist");
        }
    }

    @Transactional
    public StudyDegree updateStudyDegree(StudyDegree e) {
        em.merge(e);
        em.flush();
        return e;
    }

}
