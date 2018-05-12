package beans.crud;

import entities.Staff;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Transient;
import javax.persistence.TypedQuery;
import java.util.logging.Logger;

@ApplicationScoped
public class StaffBean {

    private final Logger log = Logger.getLogger(this.getClass().getName());

    @PersistenceContext(unitName = "sis-jpa")
    private EntityManager em;

    @Transient
    public Staff getStaffByLoginId(int id){
        TypedQuery<Staff> q = em.createNamedQuery("Staff.getByLoginid", Staff.class);

        q.setParameter("loginId", id);

        return q.getSingleResult();
    }
}
