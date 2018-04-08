package beans.crud;

import entities.Professor;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class ProfessorBean {
    private final Logger log = Logger.getLogger(this.getClass().getName());

    @PersistenceContext(unitName = "sis-jpa")
    private EntityManager em;

    public List<Professor> getAllProfessors() {
        TypedQuery<Professor> q = em.createNamedQuery("Professor.getAllProfessors", Professor.class);

        return q.getResultList();
    }

    public Professor getProfessorById(int id) {
        TypedQuery<Professor> q = em.createNamedQuery("Professor.getById", Professor.class);

        return q.getSingleResult();
    }

    public List<Professor> getProfessorByFirstAndLastName(String fName, String lName) {
        TypedQuery<Professor> q = em.createNamedQuery("Professor.getByFirstAndLastName", Professor.class);
        q.setParameter("fname", fName);
        q.setParameter("lname", lName);

        return q.getResultList();
    }

    public List<Professor> getProfessorByFirstAndLastName2(String fName, String lName1, String lName2) {
        TypedQuery<Professor> q = em.createNamedQuery("Professor.getByFirstAndLastName2", Professor.class);

        q.setParameter("fname", fName);
        q.setParameter("lname1", lName1);
        q.setParameter("lname2", lName2);

        return q.getResultList();
    }

    @Transactional
    public Professor addProfessor(Professor p) {
        try {
            em.persist(p);
            log.info(String.format("Successfully inserted %s", p.toString()));

            return p;
        }
        catch (Exception e) {
            log.severe("Something went wrong when trying to insert new Professor entity!");
            log.severe(e.getMessage());

            return null;
        }
    }
}
