package beans.crud;

import entities.StudentData;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class StudentDataBean {

    @PersistenceContext(unitName = "sis-jpa")
    private EntityManager em;

    @PostConstruct
    private void init() {
        log.info("Inicializacija " + getClass().getSimpleName());
    }

    private Logger log = Logger.getLogger(getClass().getSimpleName());

    @Transactional
    public StudentData putStudent(StudentData st){
        log.info("Putting: " + st.toString());
        em.persist(st);
        em.flush();
        return st;
    }

    @Transactional
    public StudentData getStudentById(int id) {
        log.info("Getting by id: " + id);
        StudentData sd = em.find(StudentData.class, id);
        if(sd == null) throw new NoResultException("No student by this id");
        return sd;
    }

    @Transactional
    public StudentData getStudentByStudentId(String sid) {
        log.info("Getting by student_id: " + sid);

        Query q = em.createQuery("SELECT st FROM student_data st WHERE st.studentId = :sid");
        q.setParameter("sid", sid);

        return (StudentData) q.getSingleResult();
    }

    @Transactional
    public List searchStudents(String query) {
        log.info("Searching: " + query);
        Query q = em.createQuery("SELECT st FROM student_data st WHERE st.studentId LIKE :sq OR st.name LIKE :sq OR st.surname LIKE :sq");
        q.setMaxResults(50);
        q.setParameter("sq", "%" + query + "%");
        return q.getResultList();
    }

}
