package beans.crud;

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
    public List<Municipality> getMunicipalities() {
        TypedQuery<Municipality> q = em.createNamedQuery("Municipality.getAll", Municipality.class);
        return q.getResultList();
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
        Municipality e = em.find(Municipality.class, id);
        if(e != null){
            em.remove(e);
        }
    }

    @Transactional
    public Municipality updateMunicipality(Municipality e) {
        em.merge(e);
        em.flush();
        return e;
    }

}
