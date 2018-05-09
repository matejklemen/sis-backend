package beans.crud;

import com.kumuluz.ee.configuration.utils.ConfigurationUtil;
import entities.UserLogin;
import entities.UserRole;
import exceptions.UserBlacklistedException;
import org.apache.commons.codec.digest.DigestUtils;
import org.simplejavamail.email.Email;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.Mailer;
import org.simplejavamail.mailer.MailerBuilder;
import org.simplejavamail.mailer.config.TransportStrategy;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;
import java.util.logging.Logger;

@ApplicationScoped
public class UserLoginBean {
    private final Logger log = Logger.getLogger(this.getClass().getName());
    private Map<String, Integer> ipLoginMapping = new HashMap<String, Integer>(); /* {IP: number of login attempts}*/
    private Map<String, Date> ipBlacklist = new HashMap<String, Date>(); /* {IP: date when the lock expires} */
    private final int MAX_UNSUCCESSFUL_ATTEMPTS = 5;
    private final int BLOCK_IP_MINS = 1;
    private final String SENDER_NAME = ConfigurationUtil.getInstance().get("contact-mail.name").orElse("");
    private final String SENDER_MAIL = ConfigurationUtil.getInstance().get("contact-mail.address").orElse("");
    private final String SENDER_PASSWORD = ConfigurationUtil.getInstance().get("contact-mail.password").orElse("");

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
            log.severe("Something went wrong when trying to obtain all UserLogin info!");
            log.severe(e.getMessage());
            return new ArrayList<>();
        }
    }

    @Transactional
    public UserLogin getUserLoginById(int id){
        return em.find(UserLogin.class, id);
    }

    public List<UserLogin> getUserLoginInfoByUsername(String username) {
        TypedQuery<UserLogin> q = em.createNamedQuery("UserLogin.getUserLoginInfoByUsername", UserLogin.class);
        q.setParameter("username", username);

        return q.getResultList();
    }

    public UserLogin getUserLoginInfoByUserLoginId(int userLoginId) {
        UserLogin ul = em.find(UserLogin.class, userLoginId);
        return ul;
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
    private boolean isPasswordCorrect(String inputPassword, int salt, String actualPassword) {
        String inputPasswordSaltedHashed = DigestUtils.sha512Hex(inputPassword.concat(String.valueOf(salt)));
        return actualPassword.equals(inputPasswordSaltedHashed);
    }

    /**
     * Authenticates user.
     * @param ip IP address of caller
     * @param username
     * @param inputPassword
     * @return user login details (UserLogin object) if authenthication is successful and null otherwise.
     * @throws UserBlacklistedException if user with IP 'ip' is on the blacklist.
     */
    public UserLogin authenticateUser(String ip, String username, String inputPassword) throws UserBlacklistedException {
        List<UserLogin> ul = getUserLoginInfoByUsername(username);
        UserLogin retrievedUser = null;

        // usernames are unique, so we will always get 0 results (if user doesn't exist) or 1 result.
        if(ul.size() > 0 )
            retrievedUser = ul.get(0);
        else {
            log.info(String.format("User %s could not be found in database!", username));

            // user is not allowed to make any more login attempts for the next BLOCK_IP_MINS minutes
            if(!trackUserLoginRequest(ip))
                throw new UserBlacklistedException(BLOCK_IP_MINS);

            return null;
        }

        if(isPasswordCorrect(inputPassword, retrievedUser.getSalt(), retrievedUser.getPassword()))
            return retrievedUser;
        else {
            // user is not allowed to make any more login attempts for the next BLOCK_IP_MINS minutes
            if(!trackUserLoginRequest(ip))
                throw new UserBlacklistedException(BLOCK_IP_MINS);

            return null;
        }
    }

    /**
     * Keeps track of unsuccessful login attempts and (if needed) blacklists user.
     * @param ip IP of caller
     * @return true if user is allowed to continue and false otherwise (user blacklisted).
     */
    public boolean trackUserLoginRequest(String ip) {
        Integer numLoginsCurrentUser = ipLoginMapping.get(ip);

        // first login attempt
        if(numLoginsCurrentUser == null)
            numLoginsCurrentUser = 0;

        numLoginsCurrentUser++;

        // check if user is on blacklist
        Date blacklistExpirationDate = ipBlacklist.get(ip);
        if(blacklistExpirationDate != null) {
            Date currDate = new Date();

            // if the penalty is over, remove user from blacklist
            if(currDate.after(blacklistExpirationDate)) {
                ipBlacklist.remove(ip);
                ipLoginMapping.remove(ip);
                log.info(String.format("Removed %s from blacklist!\n", ip));
            }
            else
                return false;
        }

        ipLoginMapping.put(ip, numLoginsCurrentUser);

        // if user unsuccessfully tries to login many times, add the user to blacklist
        if(numLoginsCurrentUser > MAX_UNSUCCESSFUL_ATTEMPTS) {
            Date currDate = new Date();
            Date expirationDate = new Date(currDate.getTime() + BLOCK_IP_MINS * 60 * 1000);

            ipBlacklist.put(ip, expirationDate);
            ipLoginMapping.remove(ip);

            log.info(String.format("Added %s to blacklist with expiration at %s!\n", ip, expirationDate.toString()));

            return false;
        }

        return true;
    }

    public void sendToken(String receiverAddress, String message) {
        final Email email = EmailBuilder.startingBlank()
                .from(SENDER_NAME, SENDER_MAIL)
                .to(receiverAddress, receiverAddress)
                .withSubject("Password change request from sis")
                .withHTMLText(String.format("<b>[RESET TOKEN]</b> %s", message))
                .buildEmail();

        Mailer mailer = MailerBuilder
                .withSMTPServer("smtp.gmail.com", 587, SENDER_MAIL, SENDER_PASSWORD)
                .withTransportStrategy(TransportStrategy.SMTP_TLS)
                .buildMailer();

        mailer.sendMail(email);
        log.info(String.format("Sent mail to %s!\n", receiverAddress));
    }

    /**
     * Changes password for a specified user to 'newPassword'.
     * @param username
     * @param newPassword
     * @return true if password was changed successfully and false otherwise
     */
    @Transactional
    public boolean changePassword(String username, String newPassword) {

        List<UserLogin> ul = getUserLoginInfoByUsername(username);

        // user does not exist
        if(ul.size() == 0)
            return false;

        UserLogin user = ul.get(0);

        // generate new salt for changed password
        int generatedSalt = rng.nextInt();
        user.setPassword(DigestUtils.sha512Hex(newPassword.concat(String.valueOf(generatedSalt))));
        user.setSalt(generatedSalt);

        try {
            em.merge(user);
        }
        catch (Exception e) {
            log.severe("An error occurred when trying to update user password!");
            log.severe(e.getMessage());
        }
        return true;
    }
}
