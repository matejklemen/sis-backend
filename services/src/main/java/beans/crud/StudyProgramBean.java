package beans.crud;

import entities.StudyProgram;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class StudyProgramBean {

    @PersistenceContext(unitName = "sis-jpa")
    private EntityManager em;

    @Transactional
    public StudyProgram getStudyProgram(String id) {
        StudyProgram pa = em.find(StudyProgram.class, id);
        if(pa == null) throw new NoResultException("No study degree by this id");
        return pa;
    }

    @Transactional
    public List<StudyProgram> getStudyPrograms() {
        return em.createNamedQuery("StudyProgram.getAll", StudyProgram.class).getResultList();
    }

    @Transactional
    public boolean existsStudyProgram(String id) {
        return em.find(StudyProgram.class, id) != null;
    }

    @Transactional
    public StudyProgram insertStudyProgram(StudyProgram e) {
        em.persist(e);
        em.flush();
        return e;

    }

    @Transactional
    public void deleteStudyProgram(String id) {
        StudyProgram e = em.find(StudyProgram.class, id);
        if(e != null){
            em.remove(e);
        }
    }

    @Transactional
    public StudyProgram updateStudyProgram(StudyProgram e) {
        em.merge(e);
        em.flush();
        return e;
    }

}
