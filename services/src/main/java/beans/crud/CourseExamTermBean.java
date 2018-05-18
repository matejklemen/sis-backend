package beans.crud;

import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;
import entities.curriculum.CourseExamTerm;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class CourseExamTermBean {

    private final Logger log = Logger.getLogger(this.getClass().getName());

    @PersistenceContext(unitName = "sis-jpa")
    private EntityManager em;

    @Inject
    private ExamSignUpBean signUpBean;

    public List<CourseExamTerm> getAllExamTerms(QueryParameters query) {
        List<CourseExamTerm> examTerms = JPAUtils.queryEntities(em, CourseExamTerm.class, query);
        for(CourseExamTerm e : examTerms) {
            e.setSignedUpCount(signUpBean.getExamSignUpsByExamTerm(e.getId()).size());
        }
        return examTerms;
    }

    public CourseExamTerm getExamTermById(int idCourseExamTerm) {
        TypedQuery<CourseExamTerm> q = em.createQuery("SELECT cet FROM course_exam_term cet WHERE " +
                "cet.id = :id_course_exam_term AND cet.deleted = false", CourseExamTerm.class);

        q.setParameter("id_course_exam_term", idCourseExamTerm);
        q.setMaxResults(1);

        List<CourseExamTerm> res = q.getResultList();

        if(res != null && !res.isEmpty()) {
            CourseExamTerm e = res.get(0);
            e.setSignedUpCount(signUpBean.getExamSignUpsByExamTerm(idCourseExamTerm).size());
            return e;
        } else {
            return null;
        }
    }

    public List<CourseExamTerm> getExamTermsByCourse(int idCourseOrganization) {
        TypedQuery<CourseExamTerm> q = em.createQuery("SELECT cet FROM course_exam_term cet WHERE " +
                "cet.courseOrganization.id = :id_course_organization AND cet.deleted = false",
                CourseExamTerm.class);

        q.setParameter("id_course_organization", idCourseOrganization);

        return q.getResultList();
    }

    @Transactional
    public CourseExamTerm insertExamTerm(CourseExamTerm cet) {
        try {
            log.info(String.format("Inserting new exam term with ID %d...", cet.getId()));
            em.persist(cet);
            return cet;
        }
        catch(Exception e) {
            log.severe("An error occurred while inserting new exam term!");
            log.severe(e.getMessage());
            return null;
        }
    }

    @Transactional
    public CourseExamTerm updateExamTerm(CourseExamTerm cet) {
        try {
            log.info("Updating exam term...");
            em.merge(cet);
            return cet;
        }
        catch(Exception e) {
            log.severe("An error occurred while updating exam term!");
            log.severe(e.getMessage());
            return null;
        }
    }

    @Transactional
    public void deleteExamTerm(int id) {
        CourseExamTerm cet = em.find(CourseExamTerm.class, id);
        if(cet != null) {
            cet.setDeleted(!cet.isDeleted());
            em.merge(cet);
        } else {
            throw new NoResultException("CourseExamTerm by ID doesn't exist");
        }
    }
}
