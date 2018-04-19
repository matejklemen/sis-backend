package beans.crud;

import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;
import entities.KlasiusSrv;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class KlasiusSrvBean {

    @PersistenceContext(unitName = "sis-jpa")
    private EntityManager em;

    @Transactional
    public List<KlasiusSrv> getKlasiusSrvs(QueryParameters query) {
        List<KlasiusSrv> curriculums = JPAUtils.queryEntities(em, KlasiusSrv.class, query);
        return curriculums;
    }

    @Transactional
    public KlasiusSrv getKlasiusSrv(int id) {
        KlasiusSrv c = em.find(KlasiusSrv.class, id);
        if(c == null) throw new NoResultException("No course by this id");
        return c;
    }

    @Transactional
    public boolean existsKlasiusSrv(int id) {
        return em.find(KlasiusSrv.class, id) != null;
    }

    @Transactional
    public KlasiusSrv insertKlasiusSrv(KlasiusSrv c) {
        em.persist(c);
        em.flush();
        return c;
    }

    @Transactional
    public void deleteKlasiusSrv(int id) {
        KlasiusSrv c = em.find(KlasiusSrv.class, id);
        if(c != null) {
            c.setDeleted(!c.getDeleted());
            em.merge(c);
        } else {
            throw new NoResultException("Course by ID doesn't exist");
        }
    }

    @Transactional
    public KlasiusSrv updateKlasiusSrv(KlasiusSrv c) {
        em.merge(c);
        em.flush();
        return c;
    }

}
