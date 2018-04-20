package beans.crud;

import entities.curriculum.ECTSDistribution;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class ECTSDistributionBean {

    private final Logger log = Logger.getLogger(this.getClass().getName());

    @PersistenceContext(unitName = "sis-jpa")
    private EntityManager em;

    public ECTSDistribution getECTSDistribution(int idStudyYear, int yearOfProgram, String idStudyProgram) {
        TypedQuery<ECTSDistribution> q = em.createQuery("SELECT ed FROM ects_distribution ed WHERE " +
                "ed.studyYear.id = :id_study_year AND ed.yearOfProgram = :year_of_program AND " +
                "ed.studyProgram.id = :id_study_program", ECTSDistribution.class);

        q.setParameter("id_study_year", idStudyYear);
        q.setParameter("year_of_program", yearOfProgram);
        q.setParameter("id_study_program", idStudyProgram);

        List<ECTSDistribution> res = q.getResultList();

        if(res != null && !res.isEmpty())
            return res.get(0);

        return null;
    }
}
