package beans.crud;

import entities.PostAddress;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class PostAddressBean {

    private final Logger log = Logger.getLogger(this.getClass().getName());

    @PersistenceContext(unitName = "sis-jpa")
    private EntityManager em;

    @Transactional
    public List<PostAddress> getPostAdresses() {
        TypedQuery<PostAddress> q = em.createNamedQuery("PostAddress.getAll", PostAddress.class);
        //q.setMaxResults(100);
        return q.getResultList();
    }

    @Transactional
    public PostAddress getPostAddress(int postNumber) {
        log.info("Getting by id: " + postNumber);
        PostAddress pa = em.find(PostAddress.class, postNumber);
        if(pa == null) throw new NoResultException("No post address by this post number");
        return pa;
    }

}
