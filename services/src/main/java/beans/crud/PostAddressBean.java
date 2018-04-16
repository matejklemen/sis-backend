package beans.crud;

import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;
import entities.address.PostAddress;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class PostAddressBean {

    @PersistenceContext(unitName = "sis-jpa")
    private EntityManager em;

    @Transactional
    public List<PostAddress> getPostAddresses(QueryParameters query) {
        List<PostAddress> postAdresses = JPAUtils.queryEntities(em, PostAddress.class, query);

        return postAdresses;
    }

    @Transactional
    public List<PostAddress> getDeletedPostAddresses() {
        TypedQuery<PostAddress> q = em.createNamedQuery("PostAddress.getDeleted", PostAddress.class);
        return q.getResultList();
    }

    @Transactional
    public PostAddress getPostAddress(int postNumber) {
        PostAddress pa = em.find(PostAddress.class, postNumber);
        if(pa == null) throw new NoResultException("No post address by this post number");
        return pa;
    }

    @Transactional
    public boolean existsPostAddress(int id) {
        return em.find(PostAddress.class, id) != null;
    }

    @Transactional
    public PostAddress insertPostAddress(PostAddress e) {
        em.persist(e);
        em.flush();
        return e;
    }

    @Transactional
    public void deletePostAddress(int id) {
        PostAddress c = em.find(PostAddress.class, id);
        if(c != null) {
            c.setDeleted(!c.getDeleted());
            em.merge(c);
        } else {
            throw new NoResultException("Course by ID doesn't exist");
        }
    }

    @Transactional
    public PostAddress updatePostAddress(PostAddress e) {
        em.merge(e);
        em.flush();
        return e;
    }

}
