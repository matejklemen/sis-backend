package beans.logic;

import entities.curriculum.Course;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class CodelistBean {

    private final Logger log = Logger.getLogger(this.getClass().getName());

    @PersistenceContext(unitName = "sis-jpa")
    private EntityManager em;

    @Transactional
    public List<Course> getCourses() {
        TypedQuery<Course> q = em.createNamedQuery("Course.getAll", Course.class);
        //q.setMaxResults(100);
        return q.getResultList();
    }

    @Transactional
    public Course getCourse(int id) {
        log.info("Getting by id: " + id);
        Course c = em.find(Course.class, id);
        if(c == null) throw new NoResultException("No course by this id");
        return c;
    }

}
