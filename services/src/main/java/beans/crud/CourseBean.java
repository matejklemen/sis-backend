package beans.crud;

import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;
import entities.curriculum.Course;
import entities.curriculum.CourseOrganization;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class CourseBean {

    private final Logger log = Logger.getLogger(this.getClass().getName());

    @PersistenceContext(unitName = "sis-jpa")
    private EntityManager em;

    @Transactional
    public List<Course> getCourses(QueryParameters query) {
        List<Course> courses = JPAUtils.queryEntities(em, Course.class, query);
        return courses;
    }

    @Transactional
    public Course getCourse(int id) {
        Course c = em.find(Course.class, id);
        if(c == null) throw new NoResultException("No course by this id");
        return c;
    }

    @Transactional
    public boolean existsCourse(int id) {
        return em.find(Course.class, id) != null;
    }

    @Transactional
    public Course insertCourse(Course c) {
        em.persist(c);
        em.flush();
        return c;
    }

    @Transactional
    public void deleteCourse(int id) {
        Course c = em.find(Course.class, id);
        if(c != null) {
            c.setDeleted(!c.getDeleted());
            em.merge(c);
        } else {
            throw new NoResultException("Course by ID doesn't exist");
        }
    }

    @Transactional
    public Course updateCourse(Course c) {
        em.merge(c);
        em.flush();
        return c;
    }

}
