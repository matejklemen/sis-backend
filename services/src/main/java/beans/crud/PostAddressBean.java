package beans.crud;

import entities.PostAddress;

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
    public List<PostAddress> getPostAddresses() {
        TypedQuery<PostAddress> q = em.createNamedQuery("PostAddress.getAll", PostAddress.class);
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
        PostAddress e = em.find(PostAddress.class, id);
        if(e != null){
            em.remove(e);
        }
    }

    @Transactional
    public PostAddress updatePostAddress(PostAddress e) {
        em.merge(e);
        em.flush();
        return e;
    }

}
