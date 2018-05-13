package beans.crud;

import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;
import entities.Enrolment;
import entities.curriculum.StudentCourses;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class StudentCoursesBean {

    private final Logger log = Logger.getLogger(this.getClass().getName());

    @PersistenceContext(unitName = "sis-jpa")
    private EntityManager em;

    @Transactional
    public List<StudentCourses> getAllStudentCourses(QueryParameters query) {
        List<StudentCourses> studentCourses = JPAUtils.queryEntities(em, StudentCourses.class, query);
        return studentCourses;
    }

    @Transactional
    public StudentCourses getStudentCourses(Integer id) {
        StudentCourses sc = em.find(StudentCourses.class, id);
        if(sc == null) throw new NoResultException("No student courses by this id");
        return sc;
    }

    @Transactional
    public List<StudentCourses> getStudentCoursesByEnrolmentId(int id_enrolment) {
        Enrolment e = em.find(Enrolment.class, id_enrolment);
        TypedQuery<StudentCourses> q = em.createNamedQuery("StudentCourses.getByEnrolmentId", StudentCourses.class);
        q.setParameter("enrolment", e);
        return q.getResultList();
    }

    @Transactional
    public void deleteStudentCourse(StudentCourses course){
        log.info("Will delete StudentCourse: "+course.getIdStudentCourses());

        StudentCourses c = em.find(StudentCourses.class, course.getIdStudentCourses());

        em.remove(c);
        em.flush();
    }

    @Transactional
    public StudentCourses insertCourse(StudentCourses sc){
        log.info("Will insert new StudentCourse: "+sc.getCourse().getId()+" + "+sc.getEnrolment().getId());

        em.persist(sc);
        em.flush();
        return sc;
    }

    @Transactional
    public StudentCourses updateCourse(StudentCourses studentCourse) {
        log.info("Will update student course with id: " + studentCourse.getIdStudentCourses());

        em.merge(studentCourse);
        em.flush();
        return studentCourse;
    }
}
