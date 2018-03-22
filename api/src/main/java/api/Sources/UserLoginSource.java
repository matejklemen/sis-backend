package api.Sources;

import beans.UserLoginBean;
import com.kumuluz.ee.configuration.utils.ConfigurationUtil;
import com.kumuluz.ee.cors.annotations.CrossOrigin;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import pojo.UserLogin;
import pojo.UserLoginRequest;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.logging.Logger;

import static javax.ws.rs.core.HttpHeaders.AUTHORIZATION;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("login")
@CrossOrigin(allowOrigin = "http://localhost:3000", supportedMethods = "GET, POST, PUT, DELETE, HEAD, OPTIONS")
@ApplicationScoped
public class UserLoginSource {
    private Logger log = Logger.getLogger(getClass().getSimpleName());
    private final String serverSecret = ConfigurationUtil.getInstance().get("auth.server-secret").orElse("");

    private final String TOKEN_ISSUER = "sis";
    private final int TOKEN_VALIDITY_MINS = 15;

    @Inject
    private UserLoginBean ulB;

    /*
        Returns:
        - OK (200) if user entered correct username and password combination,
        - BAD_REQUEST (400) if username or password is missing,
        - FORBIDDEN (403) if username or password (or both) is wrong.
     */
    @POST
    public Response authenticateUser(UserLoginRequest ulreq){

        if(ulreq.getUsername() == null || ulreq.getPassword() == null)
            return Response.status(Response.Status.BAD_REQUEST).build();

        UserLogin user = ulB.authenticateUser(ulreq.getUsername(), ulreq.getPassword());

        // wrong username / wrong password
        if(user == null)
            return Response.status(Response.Status.FORBIDDEN).build();

        String jwtToken = issueJWTToken(user);

        return Response.ok().header(AUTHORIZATION, "Bearer " + jwtToken).build();
    }

    private String issueJWTToken(UserLogin user) {
        Date issuedDate = new Date();

        Date expiredDate = new Date(issuedDate.getTime() + TOKEN_VALIDITY_MINS * 60 * 1000);

        String jwtToken = Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuer(TOKEN_ISSUER)
                .setIssuedAt(issuedDate)
                .setExpiration(expiredDate)
                .claim("role", user.getRole())
                .signWith(SignatureAlgorithm.HS512, serverSecret) /* server secret is set in the config file */
                .compact();

        return jwtToken;
    }
}
