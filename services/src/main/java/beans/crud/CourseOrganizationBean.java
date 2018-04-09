package beans.crud;

import entities.curriculum.CourseOrganization;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class CourseOrganizationBean {

    private final Logger log = Logger.getLogger(this.getClass().getName());

    @PersistenceContext(unitName = "sis-jpa")
    private EntityManager em;


    public List<CourseOrganization> getCourseOrganizationsByCourseId(int courseId) {
        TypedQuery<CourseOrganization> q = em.createNamedQuery("CourseOrganization.getByCourseId", CourseOrganization.class);
        q.setParameter("idCourse", courseId);

        return q.getResultList();
    }

    public List<CourseOrganization> getCourseOrganizationByProfessorId(int professorId) {
        TypedQuery<CourseOrganization> q = em.createNamedQuery("CourseOrganization.getByProfessorId", CourseOrganization.class);
        q.setParameter("profId", professorId);

        return q.getResultList();
    }

    /* Note: studyYear needs to be in format "XXXX/XXXX", for example "2017/2018" */
    public List<CourseOrganization> getCourseOrganizationByStudyYear(String studyYear) {
        TypedQuery<CourseOrganization> q = em.createNamedQuery("CourseOrganization.getByStudyYear", CourseOrganization.class);
        q.setParameter("studyYear", studyYear);

        return q.getResultList();
    }
}
