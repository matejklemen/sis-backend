package beans.crud;

import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;
import entities.StudyForm;
import utils.SearchAllCriteriaFilter;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class StudyFormBean {

    @PersistenceContext(unitName = "sis-jpa")
    private EntityManager em;

    @Transactional
    public List<StudyForm> getStudyForms(QueryParameters query) {
        return JPAUtils.queryEntities(em, StudyForm.class, query);
    }

    @Transactional
    public List<StudyForm> getStudyForms(QueryParameters query, String searchQuery) {
        if(searchQuery == null) return getStudyForms(query);
        return JPAUtils.queryEntities(em, StudyForm.class, query, new SearchAllCriteriaFilter<>(searchQuery));
    }

    @Transactional
    public StudyForm getStudyForm(int id) {
        StudyForm c = em.find(StudyForm.class, id);
        if(c == null) throw new NoResultException("No study kind by this id");
        return c;
    }

    @Transactional
    public boolean existsStudyForm(int id) {
        return em.find(StudyForm.class, id) != null;
    }

    @Transactional
    public StudyForm insertStudyForm(StudyForm c) {
        em.persist(c);
        em.flush();
        return c;
    }

    @Transactional
    public void deleteStudyForm(int id) {
        StudyForm c = em.find(StudyForm.class, id);
        if(c != null) {
            c.setDeleted(!c.getDeleted());
            em.merge(c);
        } else {
            throw new NoResultException("Course by ID doesn't exist");
        }
    }

    @Transactional
    public StudyForm updateStudyForm(StudyForm c) {
        em.merge(c);
        em.flush();
        return c;
    }

}
