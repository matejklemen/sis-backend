package beans.crud;

import entities.curriculum.CourseExamTerm;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
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

    public CourseExamTerm getExamTermById(int idCourseExamTerm) {
        TypedQuery<CourseExamTerm> q = em.createQuery("SELECT cet FROM course_exam_term cet WHERE " +
                "cet.id = :id_course_exam_term", CourseExamTerm.class);

        q.setParameter("id_course_exam_term", idCourseExamTerm);
        q.setMaxResults(1);

        List<CourseExamTerm> res = q.getResultList();

        if(res != null && !res.isEmpty())
            return res.get(0);

        return null;
    }

    public List<CourseExamTerm> getExamTermsByCourse(int idCourseOrganization) {
        TypedQuery<CourseExamTerm> q = em.createQuery("SELECT cet FROM course_exam_term cet WHERE " +
                "cet.course.id = :id_course_organization",
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
}
