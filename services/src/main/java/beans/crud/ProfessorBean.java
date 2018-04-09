package beans.crud;

import entities.Professor;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
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

    public Professor getProfessor(int id) {
        Professor p = em.find(Professor.class, id);
        if(p == null) throw new NoResultException("No professor by this id");
        return p;
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
    public boolean existsProfessor(int id) {
        return em.find(Professor.class, id) != null;
    }

    @Transactional
    public Professor insertProfessor(Professor c) {
        em.persist(c);
        em.flush();
        return c;
    }

    @Transactional
    public void deleteProfessor(int id) {
        Professor c = em.find(Professor.class, id);
        if(c != null){
            em.remove(c);
        }
    }

    @Transactional
    public Professor updateProfessor(Professor c) {
        em.merge(c);
        em.flush();
        return c;
    }
}
