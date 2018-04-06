package api.sources;

import api.interceptors.annotations.LogApiCalls;
import api.mappers.ResponseError;
import beans.crud.EnrolmentBean;
import entities.Enrolment;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("enrolments")
@ApplicationScoped
@LogApiCalls
@Tags(value = @Tag(name = "enrolments"))
public class EnrolmentSource {

    @Inject
    private EnrolmentBean enB;

    @Operation(description = "Returns a enrolment with specified id.", summary = "Get enrolment by id", responses = {
            @ApiResponse(responseCode = "200",
                    description = "Enrolment by id",
                    content = @Content(
                            schema = @Schema(implementation = Enrolment.class))
            ),
            @ApiResponse(responseCode = "404",
                    description = "Enrolment by id doesn't exist",
                    content = @Content(
                            schema = @Schema(implementation = ResponseError.class))
            )
    })
    @Path("{id}")
    @GET
    public Response getEnrolmentById(@PathParam("id") int id) {
        Enrolment en = enB.getEnrolmentById(id);
        return Response.ok(en).build();
    }

}
