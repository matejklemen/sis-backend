package beans.crud;

import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;
import entities.EnrolmentConfirmationRequest;
import entities.Student;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class EnrolmentConfirmationRequestBean {

    private final Logger log = Logger.getLogger(this.getClass().getName());

    @PersistenceContext(unitName = "sis-jpa")
    private EntityManager em;

    @Inject private StudentBean sB;

    @Transactional
    public void insertNewEnrolmentConfirmationRequest(int studentId, int copies){
        Student s = sB.getStudent(studentId);

        EnrolmentConfirmationRequest ecr = new EnrolmentConfirmationRequest();
        ecr.setNumberOfCopies(copies);
        ecr.setStudent(s);

        em.persist(ecr);
        em.flush();
    }

    public List<EnrolmentConfirmationRequest> getAllRequests(QueryParameters query){
        return JPAUtils.queryEntities(em, EnrolmentConfirmationRequest.class, query);
    }

    public List<EnrolmentConfirmationRequest> getAllRequests(){
        return em.createNamedQuery("Request.getAllRequests", EnrolmentConfirmationRequest.class).getResultList();
    }

    @Transactional
    public void deleteRequest(int requestId){
        em.remove(em.find(EnrolmentConfirmationRequest.class, requestId));
        em.flush();
    }
}
