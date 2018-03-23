package api.mappers;

import org.eclipse.persistence.exceptions.DatabaseException;
import org.postgresql.util.PSQLException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class PSQLExceptionMapper implements ExceptionMapper<PSQLException> {

    @Override
    public Response toResponse(PSQLException e) {
        return Response
                .status(Response.Status.INTERNAL_SERVER_ERROR)
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .entity(new ResponseError(500, e.getMessage()))
                .build();
    }

}


