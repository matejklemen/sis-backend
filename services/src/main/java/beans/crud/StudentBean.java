package beans.crud;

import entities.Student;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class StudentBean {

    private final Logger log = Logger.getLogger(this.getClass().getName());

    @PersistenceContext(unitName = "sis-jpa")
    private EntityManager em;

    @Transactional
    public Student putStudent(Student st){
        log.info("Putting: " + st.toString());
        em.persist(st);
        em.flush();
        return st;
    }

    @Transactional
    public Student getStudentById(int id) {
        log.info("Getting by id: " + id);
        Student sd = em.find(Student.class, id);
        if(sd == null) throw new NoResultException("No student by this id");
        return sd;
    }

    @Transactional
    public Student getStudentByRegisterNumber(String regno) {
        log.info("Getting by student_id: " + regno);

        Query q = em.createNamedQuery("Student.getByRegisterNumber");
        q.setParameter("regno", regno);

        return (Student) q.getSingleResult();
    }

    @Transactional
    public List searchStudents(String query) {
        log.info("Searching: " + query);
        Query q = em.createNamedQuery("Student.searchStudents");
        q.setMaxResults(50);
        q.setParameter("sq", "%" + query + "%");
        return q.getResultList();
    }

}
