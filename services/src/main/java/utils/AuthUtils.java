package utils;

import com.kumuluz.ee.configuration.utils.ConfigurationUtil;
import entities.UserLogin;
import io.jsonwebtoken.*;

import java.util.Date;
import java.util.HashSet;
import java.util.logging.Logger;

// authentication / authorization utilities
public class AuthUtils {
    private static Logger log = Logger.getLogger("AuthUtils");
    private static final String serverSecret = ConfigurationUtil.getInstance().get("auth.server-secret").orElse("");

    private static final String TOKEN_ISSUER = "sis";

    /**
     * Get JWT object from JWT token (String).
     * NOTE: get claims by using <Jws<Claims>-object>.getBody().get(<claim-name>).
     * @param jwtToken
     * @return JWT object
     * @throws SignatureException if an error occured during the parsing of JWT token (i.e. invalid token).
     */
    public static Jws<Claims> getJWTObject(String jwtToken) throws SignatureException {

        String[] tokenParts = jwtToken.split(" ");
        if(tokenParts.length != 2)
            throw new SignatureException("Invalid form of JWT token");

        return Jwts.parser().setSigningKey(serverSecret).parseClaimsJws(tokenParts[1]);
    }

    public static String issueJWTToken(UserLogin user, int duration) {
        Date issuedDate = new Date();

        Date expiredDate = new Date(issuedDate.getTime() + duration * 60 * 1000);

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

    public static boolean hasProperRole(String jwtToken, HashSet<String> allowedRoles) {
        // if allowedRoles is null, there must have been some major fuck up -> don't allow anybody in
        // TODO: should only allow admin in this case
        if(allowedRoles == null)
            return false;

        Object role = getJWTObject(jwtToken).getBody().get("role");

        if(role != null)
            return allowedRoles.contains(role.toString());
        else {
            log.severe("No role found");
            return false;
        }
    }

}
