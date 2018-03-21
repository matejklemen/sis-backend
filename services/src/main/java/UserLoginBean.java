import org.apache.commons.codec.digest.DigestUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class UserLoginBean {
    private final Logger log = Logger.getLogger(this.getClass().getName());

    @PersistenceContext(unitName = "sis-jpa")
    private EntityManager em;

    public List<UserLogin> getAllUserLoginInfo() {
        try {
            Query q = em.createNamedQuery("UserLogin.getAllUserLogins");
            return (List<UserLogin>) (q.getResultList());
        }
        catch (Exception e) {
            log.severe("Something went wrong when trying to obtain all UserLogin info!");
            log.severe(e.getMessage());
            return new ArrayList<>();
        }
    }

    @Transactional
    public UserLogin insertUserLoginSingle(String username, String password, String role) {
        UserLogin newUser = new UserLogin();

        newUser.setUsername(username);
        newUser.setPassword(password); // TODO: SHA-512 hash
        newUser.setSalt(123); // TODO: salt generation
        newUser.setRole(role);

        try {
            em.persist(newUser);
            log.info(String.format("Successfully inserted new user (username = %s, role = %s)\n", newUser.getUsername(), newUser.getRole()));

            // returns the same entity with the addition of newly generated ID
            return newUser;
        }
        catch (Exception e) {
            log.severe("Something went wrong when trying to insert new UserLogin!");
            log.severe(e.getMessage());
            return null;
        }
    }

    /**
     * Checks if hashed + salted password that the user entered matches user's actual password.
     * @param inputPassword password that the user entered
     * @param salt user's salt, extracted from database
     * @param actualPassword user's actual (hashed and salted) password
     * @return true if entered password matches actual password and false otherwise.
     */
    public boolean isPasswordCorrect(String inputPassword, int salt, String actualPassword) {
        String inputPasswordSaltedHashed = DigestUtils.sha512Hex(inputPassword.concat(String.valueOf(salt)));
        return actualPassword.equals(inputPasswordSaltedHashed);
    }

    /*
        NOTE: will probably be changed so that the method returns a JWT.
     */
    public boolean authenticateUser(String username, String inputPassword) {
        Query q = em.createNamedQuery("UserLogin.getSaltAndPasswordByUsername");
        q.setParameter("username", username);

        List<UserLogin> ul = q.getResultList();
        UserLogin retrievedUser = null;

        // usernames are unique, so we will always get 0 results (if user doesn't exist) or 1 result.
        if(ul.size() > 0 )
            retrievedUser = ul.get(0);
        else {
            log.info(String.format("User %s could not be found in database!", username));
            return false;
        }

        boolean isPasswordCorrect = isPasswordCorrect(inputPassword, retrievedUser.getSalt(), retrievedUser.getPassword());

        // TODO: generate JWT

        return isPasswordCorrect;
    }
}
