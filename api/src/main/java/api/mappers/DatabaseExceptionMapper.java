package api.mappers;

import org.eclipse.persistence.exceptions.DatabaseException;
import pojo.ResponseError;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class DatabaseExceptionMapper implements ExceptionMapper<DatabaseException> {

    @Override
    public Response toResponse(DatabaseException e) {

        e.printStackTrace();

        // TODO: what the fuck
        int ioe = e.getMessage().indexOf("ERROR");
        String errorMessage = e.getMessage();
        if(ioe > 0) {
            errorMessage = e.getMessage().substring(ioe);
            if(errorMessage.length() > 100)
                errorMessage = errorMessage.substring(0, 100) + "...";
        }


        return Response
                .status(Response.Status.INTERNAL_SERVER_ERROR)
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .entity(new ResponseError(500, errorMessage))
                .build();
    }

}


