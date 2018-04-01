package beans.crud;

import entities.StudyProgram;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.logging.Logger;

import static java.lang.Integer.parseInt;

@ApplicationScoped
public class StudyProgramBean {
    private final Logger log = Logger.getLogger(this.getClass().getName());

    @PersistenceContext(unitName = "sis-jpa")
    private EntityManager em;

    public StudyProgram getStudyProgramById(String id) {
        try {
            return em.createNamedQuery("StudyProgram.getById", StudyProgram.class).setParameter("id", id).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Transactional
    public List<StudyProgram> getStudyPrograms() {
        return em.createNamedQuery("StudyProgram.getAll", StudyProgram.class).getResultList();

    }
}
