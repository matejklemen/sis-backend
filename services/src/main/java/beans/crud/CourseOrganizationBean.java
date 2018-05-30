package beans.crud;

import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;
import entities.curriculum.CourseOrganization;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import javax.ws.rs.NotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class CourseOrganizationBean {

    private final Logger log = Logger.getLogger(this.getClass().getName());

    @PersistenceContext(unitName = "sis-jpa")
    private EntityManager em;

    public List<CourseOrganization> getCourseOrganizations(QueryParameters query) {
        try {
            List<CourseOrganization> courseOrganizations = JPAUtils.queryEntities(em, CourseOrganization.class, query);

            return courseOrganizations;
        } catch (NotFoundException e) {
            log.warning(e.toString());
            throw e;
        }
    }

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

    public CourseOrganization getCourseOrganizationsByCourseIdAndYear(int courseId, int studyYearId) {
        TypedQuery<CourseOrganization> q = em.createNamedQuery("CourseOrganization.getByCourseIdAndStudyYearId", CourseOrganization.class);
        q.setParameter("idCourse", courseId);
        q.setParameter("idStudyYear", studyYearId);

        List<CourseOrganization> lco = q.getResultList();

        if( lco != null && lco.size() > 0)
            return lco.get(0);
        else
            return null;
    }

    @Transactional
    public CourseOrganization updateCourseOrganization(CourseOrganization co) {
        em.merge(co);
        em.flush();
        return co;
    }
}
