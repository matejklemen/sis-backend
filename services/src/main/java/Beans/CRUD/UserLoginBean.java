package Beans.CRUD;

import entities.UserLogin;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

@ApplicationScoped
public class UserLoginBean {
    private final  Logger log = Logger.getLogger(this.getClass().getName());

    @PersistenceContext(unitName = "sis-jpa")
    private EntityManager em;

    public List<UserLogin> getAllUserLoginInfo() {
        try {
            Query q = em.createNamedQuery("UserLogin.getAll");
            return (List<UserLogin>) (q.getResultList());
        }
        catch (Exception e) {
            log.severe("Something went wrong when trying to obtain all UserLogin info!");
            e.printStackTrace();
            log.severe(String.valueOf(em == null));
            return new ArrayList<>();
        }
    }

    public UserLogin putUserLogin(UserLogin ul){
        try{
            em.persist(ul);
            em.flush();
            return ul;
        }
        catch(Exception ex){
            log.info("Something went wrong");
            ex.printStackTrace();
            return ul;
        }
    }
}
