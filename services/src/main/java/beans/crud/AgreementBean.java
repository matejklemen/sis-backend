package beans.crud;

import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;
import entities.Agreement;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class AgreementBean {

    private final Logger log = Logger.getLogger(this.getClass().getName());

    @PersistenceContext(unitName = "sis-jpa")
    private EntityManager em;

    public List<Agreement> getAgreements(QueryParameters query) {
        return JPAUtils.queryEntities(em, Agreement.class, query);
    }

    public List<Agreement> getAgreementsForStudent(int idStudent) {
        TypedQuery<Agreement> q = em.createQuery("SELECT a FROM agreement a WHERE a.student.id = :id_student", Agreement.class);

        q.setParameter("id_student", idStudent);
        return q.getResultList();
    }

    @Transactional
    public Agreement insertAgreement(Agreement agreement) {
        em.persist(agreement);
        em.flush();

        return agreement;
    }

    @Transactional
    public Agreement updateAgreement(Agreement newAgreement) {
        em.merge(newAgreement);
        em.flush();

        return newAgreement;
    }

    @Transactional
    public void deleteAgreement(int idAgreement) {
        Agreement agreement = em.find(Agreement.class, idAgreement);
        if(agreement != null) {
            agreement.setDeleted(!agreement.isDeleted());
            em.merge(agreement);
        } else {
            throw new NoResultException(String.format("Agreement with ID %d doesn't exist", idAgreement));
        }
    }
}
