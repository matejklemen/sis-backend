package api.mappers;

import javax.persistence.NoResultException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class NoResultExceptionMapper implements ExceptionMapper<NoResultException> {

    @Override
    public Response toResponse(NoResultException e) {
        return Response
                .status(Response.Status.NOT_FOUND)
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .entity(new ResponseError(404, e.getMessage()))
                .build();
    }

}


