package api.sources;

import api.mappers.ResponseError;
import beans.crud.EnrolmentBean;
import beans.crud.EnrolmentTokenBean;
import beans.crud.StudentBean;
import com.kumuluz.ee.cors.annotations.CrossOrigin;
import entities.Enrolment;
import entities.EnrolmentToken;
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

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("tokens")
@ApplicationScoped
public class EnrolmentTokenSource {

    @Inject
    private EnrolmentTokenBean etb;

    @Inject
    private EnrolmentBean eb;


    @Operation(description = "Returns newly created enrolment token for given id", summary = "Put enrolment token", tags = {"enrolments", "tokens"}, responses = {
            @ApiResponse(responseCode = "200",
                    description = "Created token for id",
                    content = @Content(
                            schema = @Schema(implementation
                                    = EnrolmentToken.class))
            ),
            @ApiResponse(responseCode = "404",
                    description = "No student with this id",
                    content = @Content(
                            schema = @Schema(implementation
                                    = ResponseError.class))
            )
    })
    @Path("{id}")
    @PUT
    public Response putToken(@PathParam("id") int id) {
        Enrolment e = eb.getEnrolmentById(id);
        EnrolmentToken et = EnrolmentToken.createEnrolmentToken(e);

        et = etb.putEnrolmentToken(et);

        return Response.ok().entity(et).build();
    }
}

