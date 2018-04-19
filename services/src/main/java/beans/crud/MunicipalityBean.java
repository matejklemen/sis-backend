package beans.crud;

import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;
import entities.address.Municipality;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class MunicipalityBean {

    @PersistenceContext(unitName = "sis-jpa")
    private EntityManager em;

    @Transactional
    public List<Municipality> getMunicipalities(QueryParameters query) {
        List<Municipality> countries = JPAUtils.queryEntities(em, Municipality.class, query);
        return countries;
    }

    @Transactional
    public Municipality getMunicipality(int postNumber) {
        Municipality pa = em.find(Municipality.class, postNumber);
        if(pa == null) throw new NoResultException("No municipality by this post number");
        return pa;
    }

    @Transactional
    public boolean existsMunicipality(int id) {
        return em.find(Municipality.class, id) != null;
    }

    @Transactional
    public Municipality insertMunicipality(Municipality e) {
        em.persist(e);
        em.flush();
        return e;
    }

    @Transactional
    public void deleteMunicipality(int id) {
        Municipality c = em.find(Municipality.class, id);
        if(c != null) {
            c.setDeleted(!c.getDeleted());
            em.merge(c);
        } else {
            throw new NoResultException("Course by ID doesn't exist");
        }
    }

    @Transactional
    public Municipality updateMunicipality(Municipality e) {
        em.merge(e);
        em.flush();
        return e;
    }

}
