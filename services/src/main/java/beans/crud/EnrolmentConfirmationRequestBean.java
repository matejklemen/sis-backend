package beans.crud;

import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;
import entities.Request;
import entities.Student;
import utils.SearchAllCriteriaFilter;

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
    public void insertNewRequest(int studentId, int copies, String type){
        Student s = sB.getStudent(studentId);

        Request ecr = new Request();
        ecr.setNumberOfCopies(copies);
        ecr.setStudent(s);
        ecr.setType(type);

        em.persist(ecr);
        em.flush();
    }

    public List<Request> getAllRequests(QueryParameters query){
        return JPAUtils.queryEntities(em, Request.class, query);
    }

    public List<Request> getAllRequestsByType(QueryParameters query, String type){
        return JPAUtils.queryEntities(em, Request.class, query, new SearchAllCriteriaFilter<>(type));
    }

    public List<Request> getAllRequestsByType(String type){
        return em.createNamedQuery("Request.getByType", Request.class)
                .setParameter("requesttype",type)
                .getResultList();
    }

    @Transactional
    public void deleteRequest(int requestId){
        em.remove(em.find(Request.class, requestId));
        em.flush();
    }
}
