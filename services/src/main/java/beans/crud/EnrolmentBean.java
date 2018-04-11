package beans.crud;

import entities.Enrolment;
import entities.EnrolmentToken;
import entities.curriculum.Course;
import entities.curriculum.StudentCourses;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class EnrolmentBean {

    private final Logger log = Logger.getLogger(this.getClass().getName());

    @PersistenceContext(unitName = "sis-jpa")
    private EntityManager em;

    @Transactional
    public Enrolment putEnrolment(EnrolmentToken ent, List<Integer> cl){
        Enrolment en = new Enrolment();
        en.setConfirmed(true);
        en.setForm(ent.getForm());
        en.setKind(ent.getKind());
        en.setStudent(ent.getStudent());
        en.setStudyProgram(ent.getStudyProgram());
        en.setStudyYear(ent.getStudyYear());
        en.setType(ent.getType());
        en.setYear(ent.getYear());
        em.persist(en);
        Iterator<Integer> clIterator = cl.iterator();
        while (clIterator.hasNext()) {
            Course c = em.find(Course.class, clIterator.next());
            StudentCourses sc = new StudentCourses();
            sc.setCourse(c);
            sc.setEnrolment(en);
            em.persist(sc);
        }
        em.flush();
        return en;
    }

    @Transactional
    public Enrolment getEnrolmentById(int id) {
        log.info("Getting by id: " + id);
        Enrolment en = em.find(Enrolment.class, id);
        if(en == null) throw new NoResultException("No enrolment by this id");
        return en;
    }

    @Transactional
    public Enrolment getLastEnrolmentByStudentId(int studentId) {
        log.info("Getting last enrolment for student id: " + studentId);
        return em.createNamedQuery("Enrolment.getLastByStudentId", Enrolment.class)
                .setParameter("id", studentId)
                .setMaxResults(1)
                .getSingleResult();
    }

    @Transactional
    public Enrolment getFirstEnrolmentByStudentIdAndProgram(int studentId, String studyProgramId) {
        log.info("Getting last enrolment for student id: " + studentId);
        return em.createNamedQuery("Enrolment.getFirstByStudentId", Enrolment.class)
                .setParameter("id", studentId)
                .setParameter("studyProgramId", studyProgramId)
                .setMaxResults(1)
                .getSingleResult();
    }
}
