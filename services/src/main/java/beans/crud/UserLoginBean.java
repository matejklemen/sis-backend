package beans.crud;

import entities.UserLogin;
import entities.UserRole;
import org.apache.commons.codec.digest.DigestUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class UserLoginBean {
    private final Logger log = Logger.getLogger(this.getClass().getName());

    @PersistenceContext(unitName = "sis-jpa")
    private EntityManager em;

    private final SecureRandom rng = initSecureRandom();

    private SecureRandom initSecureRandom() {
        SecureRandom sr = null;

        try {
            sr = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            log.severe("Number generator could not be initialized due to a missing RNG implementation!");
            log.severe(e.getMessage());
        }

        return sr;
    }

    public List<UserLogin> getAllUserLoginInfo() {
        try {
            TypedQuery<UserLogin> q = em.createNamedQuery("UserLogin.getAll", UserLogin.class);
            return q.getResultList();
        }
        catch (Exception e) {
            log.severe("Something went wrong when trying to obtain all pojo.UserLogin info!");
            log.severe(e.getMessage());
            return new ArrayList<>();
        }
    }

    @Transactional
    public UserLogin insertUserLoginSingle(String username, String password, UserRole role) {
        UserLogin newUser = new UserLogin();

        int generatedSalt = rng.nextInt();

        newUser.setUsername(username);
        newUser.setPassword(DigestUtils.sha512Hex(password.concat(String.valueOf(generatedSalt))));
        newUser.setSalt(generatedSalt);
        newUser.setRole(role);

        try {
            em.persist(newUser);
            log.info(String.format("Successfully inserted new user (username = %s, role = %s)\n", newUser.getUsername(), newUser.getRole()));

            // returns the same entity with the addition of newly generated ID
            return newUser;
        }
        catch (Exception e) {
            log.severe("Something went wrong when trying to insert new pojo.UserLogin!");
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
    private boolean isPasswordCorrect(String inputPassword, int salt, String actualPassword) {
        String inputPasswordSaltedHashed = DigestUtils.sha512Hex(inputPassword.concat(String.valueOf(salt)));
        return actualPassword.equals(inputPasswordSaltedHashed);
    }


    /**
     * Authenticates user.
     * @param username
     * @param inputPassword
     * @return user login details (UserLogin object) if authenthication is successful and null otherwise.
     */
    public UserLogin authenticateUser(String username, String inputPassword) {
        TypedQuery<UserLogin> q = em.createNamedQuery("UserLogin.getSaltAndPasswordByUsername", UserLogin.class);
        q.setParameter("username", username);

        List<UserLogin> ul = q.getResultList();
        UserLogin retrievedUser = null;

        // usernames are unique, so we will always get 0 results (if user doesn't exist) or 1 result.
        if(ul.size() > 0 )
            retrievedUser = ul.get(0);
        else {
            log.info(String.format("User %s could not be found in database!", username));
            return null;
        }

        return (isPasswordCorrect(inputPassword, retrievedUser.getSalt(), retrievedUser.getPassword())) ? retrievedUser : null;
    }

}
