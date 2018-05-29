package api.mappers;

import pojo.ResponseError;

import javax.persistence.PersistenceException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Provider
public class PersistenceExceptionMapper implements ExceptionMapper<PersistenceException> {

    private static Pattern[] patterns = {
            Pattern.compile("null value in column \"(.+)\" violates not-null constraint"),
            Pattern.compile("duplicate key value violates unique constraint \"(.+)\"")
    };

    private static String[] responses = {
            "Polje '%s' ne sme biti prazno.",
            "Podvojen ključ: '%s'",
    };

    @Override
    public Response toResponse(PersistenceException e) {

        String exMessage = e.getMessage();
        String reMessage = null;

        for(int i = 0; i < patterns.length; i++) {
            Matcher m = patterns[i].matcher(exMessage);
            if(m.find()) {
                reMessage = String.format(responses[i], m.group(1));
            }
        }

        return Response
                .status(reMessage != null ? Response.Status.BAD_REQUEST : Response.Status.INTERNAL_SERVER_ERROR)
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .entity(reMessage != null ? new ResponseError(400, reMessage) : ResponseError.error500())
                .build();
    }

}


