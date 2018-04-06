package api.mappers;

import api.exceptions.NoRequestBodyException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class NoRequestBodyExceptionMapper implements ExceptionMapper<NoRequestBodyException> {

    @Override
    public Response toResponse(NoRequestBodyException e) {
        return Response
                .status(Response.Status.BAD_REQUEST)
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .entity(new ResponseError(400, e.getMessage()))
                .build();
    }

}


