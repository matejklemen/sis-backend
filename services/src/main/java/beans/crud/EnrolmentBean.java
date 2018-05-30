package beans.crud;

import entities.Enrolment;
import entities.EnrolmentToken;
import entities.curriculum.Course;
import entities.curriculum.StudentCourses;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import javax.ws.rs.NotFoundException;
import java.util.Calendar;
import java.util.Date;
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
        en.setConfirmed(false);
        en.setForm(ent.getForm());
        en.setKind(ent.getKind());
        en.setStudent(ent.getStudent());
        en.setStudyProgram(ent.getStudyProgram());
        en.setStudyYear(ent.getStudyYear());
        en.setType(ent.getType());
        en.setYear(ent.getYear());
        en.setKlasiusSrv(ent.getKlasiusSrv());
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
        List<Enrolment> en = em.createNamedQuery("Enrolment.getLastByStudentId", Enrolment.class)
                .setParameter("id", studentId)
                .setMaxResults(1)
                .getResultList();

        if(en == null || en.size() == 0)
            throw new NotFoundException("No enrolment fot this student id");

        return en.get(0);
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

    @Transactional
    public Enrolment getOriginalYearOfRetryYearEnrolment(int studentId) {
        try {
            Enrolment e = em.createNamedQuery("Enrolment.getRetryYearEnrolment", Enrolment.class)
                    .setParameter("student_id", studentId)
                    .setMaxResults(1)
                    .getSingleResult();
            return em.createNamedQuery("Enrolment.getOriginalYearOfRetryYearEnrolment", Enrolment.class)
                    .setParameter("student_id", studentId)
                    .setParameter("year", e.getYear())
                    .setMaxResults(1)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    @Transactional
    public List<Enrolment> getEnrolmentsByStudentId(int studentId) {
        return em.createNamedQuery("Enrolment.getByStudentId", Enrolment.class).setParameter("id", studentId).getResultList();
    }

    @Transactional
    public Enrolment updateEnrolment(Enrolment e){
        log.info("Will update enrolment with id: "+e.getId());

        em.merge(e);
        em.flush();
        return e;

    }

    @Transactional
    public Enrolment getEnrolment(int studentId, int studyYearId) {
        return em.createNamedQuery("Enrolment.getByStudentIdAndStudyYearId", Enrolment.class)
                .setParameter("studentId", studentId)
                .setParameter("studyYearId", studyYearId)
                .setMaxResults(1)
                .getSingleResult();
    }

    @Transactional
    public List<Enrolment> getAllEnrolments() {
        TypedQuery<Enrolment> q = em.createQuery("SELECT enr FROM enrolment enr", Enrolment.class);

        return q.getResultList();
    }

    @Transactional
    public List<Enrolment> getEnrolmentsForCurrentYear() {
        // months start with 0
        final int MONTH_OCTOBER = 10 - 1;
        Date d = new Date();

        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);

        // if current date is less than 1st October, 'previous' study year is still in progress
        // example: if current date is 7th of June 2015, the current study year is 2014/2015, not 2015/2016
        String studyYear = month < MONTH_OCTOBER ? String.format("%s/%s", year - 1, year):
                String.format("%s/%s", year, year + 1);

        log.info(String.format("Getting enrolments for year %s...", studyYear));

        TypedQuery<Enrolment> q = em.createQuery("SELECT enr FROM enrolment enr WHERE " +
                "enr.studyYear.name = :study_year " +
                "ORDER BY enr.student.surname, enr.student.name", Enrolment.class);

        q.setParameter("study_year", studyYear);

        return q.getResultList();
    }
}
