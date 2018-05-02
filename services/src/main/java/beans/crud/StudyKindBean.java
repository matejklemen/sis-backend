package beans.crud;

import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;
import entities.StudyKind;
import utils.SearchAllCriteriaFilter;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class StudyKindBean {

    @PersistenceContext(unitName = "sis-jpa")
    private EntityManager em;

    @Transactional
    public List<StudyKind> getStudyKinds(QueryParameters query) {
        return JPAUtils.queryEntities(em, StudyKind.class, query);
    }

    @Transactional
    public List<StudyKind> getStudyKinds(QueryParameters query, String searchQuery) {
        if(searchQuery == null) return getStudyKinds(query);
        return JPAUtils.queryEntities(em, StudyKind.class, query, new SearchAllCriteriaFilter<>(searchQuery));
    }

    @Transactional
    public StudyKind getStudyKind(int id) {
        StudyKind c = em.find(StudyKind.class, id);
        if(c == null) throw new NoResultException("No study kind by this id");
        return c;
    }

    @Transactional
    public boolean existsStudyKind(int id) {
        return em.find(StudyKind.class, id) != null;
    }

    @Transactional
    public StudyKind insertStudyKind(StudyKind c) {
        em.persist(c);
        em.flush();
        return c;
    }

    @Transactional
    public void deleteStudyKind(int id) {
        StudyKind c = em.find(StudyKind.class, id);
        if(c != null) {
            c.setDeleted(!c.getDeleted());
            em.merge(c);
        } else {
            throw new NoResultException("Course by ID doesn't exist");
        }
    }

    @Transactional
    public StudyKind updateStudyKind(StudyKind c) {
        em.merge(c);
        em.flush();
        return c;
    }

}
