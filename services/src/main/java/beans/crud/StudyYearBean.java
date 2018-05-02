package beans.crud;

import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;
import entities.StudyYear;
import utils.SearchAllCriteriaFilter;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class StudyYearBean {

    @PersistenceContext(unitName = "sis-jpa")
    private EntityManager em;

    public StudyYear getStudyYearByName(String name) {
        try {
            return em.createNamedQuery("StudyYear.getByName", StudyYear.class).setParameter("name", name).getSingleResult();
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

    @Transactional
    public List<StudyYear> getStudyYears(QueryParameters query) {
        return JPAUtils.queryEntities(em, StudyYear.class, query);
    }

    @Transactional
    public List<StudyYear> getStudyYears(QueryParameters query, String searchQuery) {
        if(searchQuery == null) return getStudyYears(query);
        return JPAUtils.queryEntities(em, StudyYear.class, query, new SearchAllCriteriaFilter<>(searchQuery));
    }

    @Transactional
    public StudyYear getStudyYear(int id) {
        StudyYear pa = em.find(StudyYear.class, id);
        if(pa == null) throw new NoResultException("No study year by this post number");
        return pa;
    }

    @Transactional
    public boolean existsStudyYear(int id) {
        return em.find(StudyYear.class, id) != null;
    }

    @Transactional
    public StudyYear insertStudyYear(StudyYear e) {
        em.persist(e);
        em.flush();
        return e;
    }

    @Transactional
    public void deleteStudyYear(int id) {
        StudyYear c = em.find(StudyYear.class, id);
        if(c != null) {
            c.setDeleted(!c.getDeleted());
            em.merge(c);
        } else {
            throw new NoResultException("Course by ID doesn't exist");
        }
    }

    @Transactional
    public StudyYear updateStudyYear(StudyYear e) {
        em.merge(e);
        em.flush();
        return e;
    }

}
