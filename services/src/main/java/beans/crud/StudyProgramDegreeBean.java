package beans.crud;

import entities.StudyDegree;
import entities.StudyProgram;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class StudyProgramDegreeBean {
    private final Logger log = Logger.getLogger(this.getClass().getName());

    @PersistenceContext(unitName = "sis-jpa")
    private EntityManager em;

    @Transactional
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

    @Transactional
    public StudyDegree getStudyDegreeById(String id) {
        try {
            return em.createNamedQuery("StudyDegree.getById", StudyDegree.class).setParameter("id", id).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Transactional
    public List<StudyDegree> getStudyDegrees() {
        return em.createNamedQuery("StudyDegree.getAll", StudyDegree.class).getResultList();
    }

}
