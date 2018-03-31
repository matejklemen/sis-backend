package beans.crud;

import entities.StudyProgram;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class StudyProgramBean {
    private final Logger log = Logger.getLogger(this.getClass().getName());

    @PersistenceContext(unitName = "sis-jpa")
    private EntityManager em;

    public StudyProgram getStudyProgramById(int id) {
        return em.find(StudyProgram.class, id);
    }

    public StudyProgram getStudyProgramByName(String name) {
        try {
            return em.createNamedQuery("StudyProgram.getByName", StudyProgram.class).setParameter("name", name).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Transactional
    public StudyProgram getOrCreateStudyProgram(String name) {
        StudyProgram sp = getStudyProgramByName(name);
        if(sp == null) {
            sp = new StudyProgram();
            sp.setName(name);
            em.persist(sp);
            em.flush();
            return sp;
        }
        return sp;
    }

    @Transactional
    public List<StudyProgram> getStudyPrograms() {
        return em.createNamedQuery("StudyProgram.getAll", StudyProgram.class).getResultList();

    }
}
