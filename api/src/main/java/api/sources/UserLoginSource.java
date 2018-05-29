package api.sources;

import api.interceptors.annotations.LogApiCalls;
import beans.crud.ProfessorBean;
import beans.crud.StudentBean;
import beans.crud.UserLoginBean;
import beans.crud.UserRoleBean;
import entities.UserLogin;
import exceptions.UserBlacklistedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureException;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import pojo.UserLoginRequest;
import utils.AuthUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("login")
@ApplicationScoped
@LogApiCalls
@Tags(value = @Tag(name = "authentication"))
public class UserLoginSource {

    private Logger log = Logger.getLogger(getClass().getSimpleName());
    private static final int TOKEN_VALIDITY_MINS_LOGIN = 60 * 2;
    private static final int TOKEN_VALIDITY_MINS_RESET_PASSWORD = 1;

    @Inject
    private UserLoginBean ulB;

    @Inject
    private UserRoleBean urB;

    @Inject private StudentBean sB;
    @Inject private ProfessorBean pB;

    /*
        Returns:
        - OK (200) if user entered correct username and password combination,
        - BAD_REQUEST (400) if username/password is missing,
        - FORBIDDEN (403) if username or password (or both) is wrong,
        - TOO_MANY_REQUESTS(429) if user attempted to login too many times in a short period.
     */
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public Response authenticateUser(@Context HttpServletRequest requestContext, UserLoginRequest ulreq){
        // add "-Djava.net.preferIPv4Stack=true" to VM options to get IPv4 addresses
        String ip = requestContext.getRemoteAddr();

        if(ulreq.getUsername() == null || ulreq.getPassword() == null)
            return Response.status(Response.Status.BAD_REQUEST).build();

        UserLogin user = null;

        try {
            user = ulB.authenticateUser(ip, ulreq.getUsername(), ulreq.getPassword());
        }
        catch (UserBlacklistedException ube) {
            return Response.status(429).build();
        }

        // wrong username / wrong password
        if(user == null)
            return Response.status(Response.Status.FORBIDDEN).build();

        HashMap<String, Object> tokenBodyItems = new HashMap<>();
        tokenBodyItems.put("username", user.getUsername());
        tokenBodyItems.put("loginId", user.getId());
        tokenBodyItems.put("role", user.getRole());
        // get user's "identity" - for a student, it's registerNumber
        // identity is used on FE for redirecting to /student/{registerNumber}
        // when student logs in.
        switch (user.getRole().getId()) {
            case 1:
                // TODO
                break;
            case 2:
                log.info(user.getId() + "");
                tokenBodyItems.put("identity", sB.getStudentByLoginId(user.getId()).getRegisterNumber());
                break;
            case 3:
                // TODO don't know if this is necessary
                tokenBodyItems.put("identity", pB.getProfessorByLoginId(user.getId()).getId());
                break;
            case 4:
                // TODO
                break;
            default:
                break;
        }

        String jwtToken = AuthUtils.issueJWTToken(tokenBodyItems, TOKEN_VALIDITY_MINS_LOGIN);

        return Response.ok(jwtToken).build();
    }

    @POST
    @Path("reset-password")
    /*
        Returns:
        - OK (200) in every situation because we do not want to give out any hints about existing/non-existing usernames
        (front end should say something like "If the entered mail is valid, you should have a reset token waiting in your inbox. Enter it here: ...")
     */
    public Response sendResetMail(UserLoginRequest ulreq) {

        if(ulreq.getUsername() == null)
            return Response.status(Response.Status.BAD_REQUEST).build();

        List<UserLogin> ul = ulB.getUserLoginInfoByUsername(ulreq.getUsername());

        if(ul.size() > 0)
            ulB.sendToken(ulreq.getUsername(), AuthUtils.issueJWTToken(null, TOKEN_VALIDITY_MINS_RESET_PASSWORD));
        else
            log.severe(String.format("User %s could not be found, therefore I am not sending a password reset mail.\n", ulreq.getUsername()));

        return Response.status(Response.Status.OK).build();
    }

    @POST
    @Path("change-password")
    /*
        Returns:
        - OK (200) if password change was successful,
        - BAD_REQUEST (400) otherwise
        NOTE: the JWT token is in request body because this endpoint will be used for:
        - changing password of authenticated users and
        - changing the password of users that want to reset their password (not authenticated).
    */
    public Response changePassword(UserLoginRequest ulreq) {

        if(ulreq.getJwtToken() == null || ulreq.getNewPassword() == null || ulreq.getNewPassword().length() == 0) {
            log.severe("Either the JWT token is missing OR new password is missing OR new password is an empty string.");
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        Claims userClaims = null;

        try {
            userClaims = AuthUtils.getJWTObject(ulreq.getJwtToken()).getBody();
        }
        catch (SignatureException se) {
            log.severe("Encountered an invalid JWT token in request to change password!");
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        String username = userClaims.getSubject();

        // check if reset token is still valid
        if(new Date().after(userClaims.getExpiration()))
            return Response.status(Response.Status.BAD_REQUEST).build();

        boolean success = ulB.changePassword(username, ulreq.getNewPassword());

        return success ? Response.status(Response.Status.OK).build():
                Response.status(Response.Status.BAD_REQUEST).build();
    }

}
