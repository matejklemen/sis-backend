package api.mappers;

import pojo.ResponseError;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class NullPointerExceptionMapper implements ExceptionMapper<NullPointerException> {

    @Override
    public Response toResponse(NullPointerException e) {

        e.printStackTrace();

        return Response
                .status(Response.Status.INTERNAL_SERVER_ERROR)
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .entity(ResponseError.error500())
                .build();
    }

}


