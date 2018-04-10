package beans.crud;

import entities.StudyForm;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class StudyFormBean {

    @PersistenceContext(unitName = "sis-jpa")
    private EntityManager em;

    @Transactional
    public List<StudyForm> getStudyForms() {
        TypedQuery<StudyForm> q = em.createNamedQuery("StudyForm.getAll", StudyForm.class);
        //q.setMaxResults(100);
        return q.getResultList();
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
        if(c != null){
            em.remove(c);
        }
    }

    @Transactional
    public StudyForm updateStudyForm(StudyForm c) {
        em.merge(c);
        em.flush();
        return c;
    }

}
