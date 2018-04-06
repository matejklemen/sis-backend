package beans.crud;

import entities.StudyYear;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class StudyYearBean {

    @PersistenceContext(unitName = "sis-jpa")
    private EntityManager em;

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

    @Transactional
    public List<StudyYear> getStudyYears() {
        TypedQuery<StudyYear> q = em.createNamedQuery("StudyYear.getAll", StudyYear.class);
        return q.getResultList();
    }

    @Transactional
    public StudyYear getStudyYear(int postNumber) {
        StudyYear pa = em.find(StudyYear.class, postNumber);
        if(pa == null) throw new NoResultException("No post address by this post number");
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
        StudyYear e = em.find(StudyYear.class, id);
        if(e != null){
            em.remove(e);
        }
    }

    @Transactional
    public StudyYear updateStudyYear(StudyYear e) {
        em.merge(e);
        em.flush();
        return e;
    }

}
