package beans.crud;

import entities.curriculum.Curriculum;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class CurriculumBean {

    private final Logger log = Logger.getLogger(this.getClass().getName());

    @PersistenceContext(unitName = "sis-jpa")
    private EntityManager em;

    public List<Curriculum> getEntireCurriculum() {
        TypedQuery<Curriculum> q = em.createNamedQuery("Curriculum.getAll", Curriculum.class);

        return q.getResultList();
    }

    public List<Curriculum> getCurriculumByStudyProgramId(String studyProgramId) {
        TypedQuery<Curriculum> q = em.createNamedQuery("Curriculum.getByStudyProgramId", Curriculum.class);
        q.setParameter("id", studyProgramId);

        return q.getResultList();
    }

    public List<Curriculum> getCurriculumByStudyProgramName(String studyProgramName) {
        TypedQuery<Curriculum> q = em.createNamedQuery("Curriculum.getByStudyProgramName", Curriculum.class);
        q.setParameter("name", studyProgramName);

        return q.getResultList();
    }

    public List<Curriculum> getCurriculumByStudyProgramDegreeName(String studyProgramDegreeName) {
        TypedQuery<Curriculum> q = em.createNamedQuery("Curriculum.getByStudyProgramDegreeName", Curriculum.class);
        q.setParameter("name", studyProgramDegreeName);

        return q.getResultList();
    }

    public List<Curriculum> getFirstYearCourses(String studyYear, String idStudyProgram) {
        TypedQuery<Curriculum> q = em.createNamedQuery("Curriculum.getFirstYear", Curriculum.class);
        q.setParameter("nameStudyYear", studyYear);
        q.setParameter("idStudyProgram", idStudyProgram);

        return q.getResultList();
    }

    /*
        NOTE: 4 almost same methods created for getting courses, could be merged into 1 method, but they're not because
        encoding for type of course is determined ad-hoc and would probably need to be looked up everytime.
    */
    public List<Curriculum> getModuleCourses(String studyYear, String idStudyProgram, int yearOfProgram) {
        TypedQuery<Curriculum> q = em.createNamedQuery("Curriculum.getModuleCourses", Curriculum.class);
        q.setParameter("nameStudyYear", studyYear);
        q.setParameter("idStudyProgram", idStudyProgram);
        q.setParameter("yearOfProgram", yearOfProgram);

        return q.getResultList();
    }

    public List<Curriculum> getMandatoryCourses(String studyYear, String idStudyProgram, int yearOfProgram) {
        TypedQuery<Curriculum> q = em.createNamedQuery("Curriculum.getMandatoryCourses", Curriculum.class);
        q.setParameter("nameStudyYear", studyYear);
        q.setParameter("idStudyProgram", idStudyProgram);
        q.setParameter("yearOfProgram", yearOfProgram);

        return q.getResultList();
    }

    public List<Curriculum> getSpecialistElectiveCourses(String studyYear, String idStudyProgram, int yearOfProgram) {
        TypedQuery<Curriculum> q = em.createNamedQuery("Curriculum.getSpecialistElectiveCourses", Curriculum.class);
        q.setParameter("nameStudyYear", studyYear);
        q.setParameter("idStudyProgram", idStudyProgram);
        q.setParameter("yearOfProgram", yearOfProgram);

        return q.getResultList();
    }

    public List<Curriculum> getGeneralElectiveCourses(String studyYear, String idStudyProgram, int yearOfProgram) {
        TypedQuery<Curriculum> q = em.createNamedQuery("Curriculum.getGeneralElectiveCourses", Curriculum.class);
        q.setParameter("nameStudyYear", studyYear);
        q.setParameter("idStudyProgram", idStudyProgram);
        q.setParameter("yearOfProgram", yearOfProgram);

        return q.getResultList();
    }
}
