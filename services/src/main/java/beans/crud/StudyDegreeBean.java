package beans.crud;

import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;
import entities.StudyDegree;
import utils.SearchAllCriteriaFilter;

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
        return JPAUtils.queryEntities(em, StudyDegree.class, query);
    }

    @Transactional
    public List<StudyDegree> getStudyDegrees(QueryParameters query, String searchQuery) {
        if(searchQuery == null) return getStudyDegrees(query);
        return JPAUtils.queryEntities(em, StudyDegree.class, query, new SearchAllCriteriaFilter<>(searchQuery));
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
