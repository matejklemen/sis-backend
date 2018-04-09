package beans.crud;

import entities.StudyKind;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class StudyKindBean {

    @PersistenceContext(unitName = "sis-jpa")
    private EntityManager em;

    @Transactional
    public List<StudyKind> getStudyKinds() {
        TypedQuery<StudyKind> q = em.createNamedQuery("StudyKind.getAll", StudyKind.class);
        //q.setMaxResults(100);
        return q.getResultList();
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
        if(c != null){
            em.remove(c);
        }
    }

    @Transactional
    public StudyKind updateStudyKind(StudyKind c) {
        em.merge(c);
        em.flush();
        return c;
    }

}
