package api.sources;

import api.interceptors.annotations.LogApiCalls;
import api.mappers.ResponseError;
import beans.crud.EnrolmentBean;
import beans.crud.EnrolmentTokenBean;
import beans.crud.StudentBean;
import com.kumuluz.ee.cors.annotations.CrossOrigin;
import entities.Enrolment;
import entities.EnrolmentToken;
import entities.Student;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
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
@LogApiCalls
public class EnrolmentTokenSource {

    @Inject
    private EnrolmentTokenBean etb;

    @Inject
    private EnrolmentBean eb;

    @Inject
    private StudentBean esb;


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
        Enrolment e = eb.getLastEnrolmentByStudentId(id);
        EnrolmentToken et = EnrolmentToken.createEnrolmentToken(e);

        et = etb.putEnrolmentToken(et);

        return Response.ok().entity(et).build();
    }

    @Operation(description = "Returns enrolment token for student id if existing", summary = "Get enrolment token", tags = {"enrolments", "tokens"},
            parameters = {
                    @Parameter(name = "studentId", description = "Student id", in = ParameterIn.QUERY, schema = @Schema(type = "int")),
            },
            responses = {
            @ApiResponse(responseCode = "200",
                    description = "Enrolment token for id",
                    content = @Content(
                            schema = @Schema(implementation
                                    = EnrolmentToken.class))
            ),
            @ApiResponse(responseCode = "404",
                    description = "No enrolment token with this student id",
                    content = @Content(
                            schema = @Schema(implementation
                                    = ResponseError.class))
            )
    })
    @GET
    public Response getToken(@QueryParam("studentId") int studentId){
        EnrolmentToken et = etb.getEnrolmentTokenByStudentId(studentId);
        return Response.ok().entity(et).build();
    }

    @Operation(description = "Deletes token with given id", summary = "Delete enrolment token", tags = {"enrolments", "tokens"},
            responses = {
                    @ApiResponse(responseCode = "204",
                            description = "Enrolment token was removed"
                    ),
                    @ApiResponse(responseCode = "404",
                            description = "No enrolment token with this id",
                            content = @Content(
                                    schema = @Schema(implementation
                                            = ResponseError.class))
                    )
            })
    @Path("{id}")
    @DELETE
    public Response removeToken(@PathParam("id") int id){
        etb.deleteEnrolmentToken(id);
        return Response.noContent().build();
    }


    @Operation(description = "Updates enrolment token", summary = "Post enrolment token", tags = {"enrolments", "tokens"},
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Updated enrolment token",
                            content = @Content(
                                    schema = @Schema(implementation
                                            = EnrolmentToken.class))
                    ),
                    @ApiResponse(responseCode = "404",
                            description = "No enrolment token with this id to update",
                            content = @Content(
                                    schema = @Schema(implementation
                                            = ResponseError.class))
                    )
            })
    @POST
    public Response postToken(@RequestBody EnrolmentToken token){
        token.setStudent(etb.getEnrolmentTokenById(token.getId()).getStudent());
        EnrolmentToken et = etb.updateEnrolmentToken(token);
        return Response.ok().entity(et).build();
    }
}

