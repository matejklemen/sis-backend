package beans.crud;

import entities.Enrolment;
import entities.curriculum.StudentCourses;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class StudentCoursesBean {

    private final Logger log = Logger.getLogger(this.getClass().getName());

    @PersistenceContext(unitName = "sis-jpa")
    private EntityManager em;

    @Transactional
    public List<StudentCourses> getAllStudentCourses() {
        TypedQuery<StudentCourses> q = em.createNamedQuery("StudentCourses.getAll", StudentCourses.class);
        return q.getResultList();
    }

    @Transactional
    public List<StudentCourses> getStudentCoursesByEnrolmentId(int id_enrolment) {
        Enrolment e = em.find(Enrolment.class, id_enrolment);
        TypedQuery<StudentCourses> q = em.createNamedQuery("StudentCourses.getByEnrolmentId", StudentCourses.class);
        q.setParameter("enrolment", e);
        return q.getResultList();
    }
}
