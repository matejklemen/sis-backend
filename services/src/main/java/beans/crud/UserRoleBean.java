package beans.crud;

import entities.UserLogin;
import entities.UserRole;
import org.apache.commons.codec.digest.DigestUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class UserRoleBean {
    private final Logger log = Logger.getLogger(this.getClass().getName());

    @PersistenceContext(unitName = "sis-jpa")
    private EntityManager em;

    public UserRole getRoleById(int id) {
        return em.find(UserRole.class, id);
    }

    public UserRole getRoleByName(String name) {
        return em.createNamedQuery("UserRole.getByName", UserRole.class).setParameter("name", name).getSingleResult();
    }

}
