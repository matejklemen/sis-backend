package api.sources;

import api.mappers.ResponseError;
import beans.crud.EnrolmentBean;
import beans.crud.StudentBean;
import entities.Enrolment;
import entities.Student;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.logging.Logger;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("enrolments")
@ApplicationScoped
public class EnrolmentSource {
    private Logger log = Logger.getLogger(getClass().getSimpleName());

    @Inject
    private EnrolmentBean enB;

    @Operation(description = "Returns an enrolment with specified id.", summary = "Enrolment by id", tags = {"students", "enrolments"}, responses = {
            @ApiResponse(responseCode = "200",
                    description = "Enrolment by id",
                    content = @Content(
                            schema = @Schema(implementation
                                    = Enrolment.class))
            ),
            @ApiResponse(responseCode = "404",
                    description = "Enrolment by id doesn't exist",
                    content = @Content(
                            schema = @Schema(implementation
                                    = ResponseError.class))
            )
    })
    @Path("{id}")
    @GET
    public Response getEnrolmentById(@PathParam("id") int id) {
        Enrolment en = enB.getEnrolmentById(id);
        return Response.ok(en).build();
    }

}
