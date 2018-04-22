package api.sources;

import api.exceptions.NoRequestBodyException;
import api.interceptors.annotations.LogApiCalls;
import beans.crud.EnrolmentBean;
import beans.crud.EnrolmentTokenBean;
import beans.crud.StudentBean;
import beans.logic.EnrolmentPolicyBean;
import entities.Enrolment;
import entities.EnrolmentToken;
import entities.logic.EnrolmentSheet;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import pojo.ResponseError;

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
@LogApiCalls
@Tags(value = @Tag(name = "enrolments"))
public class EnrolmentSource {

    private final Logger log = Logger.getLogger(this.getClass().getName());

    @Inject
    private StudentBean sB;

    @Inject
    private EnrolmentBean enB;

    @Inject
    private EnrolmentTokenBean enrolmentTokenBean;

    @Inject
    private EnrolmentPolicyBean enrolmentPolicyBean;

    @Operation(description = "Returns enrolments for student. If order and studyProgramId are given, returns one Enrolment. Order can be one of [\"first\",\"last\"]", summary = "Get enrolment(-s) by studentId and/or query parameters",
            parameters = {
                    @Parameter(name = "studyProgramId", description = "studyProgram Id", in = ParameterIn.QUERY, schema = @Schema(type = "string")),
                    @Parameter(name = "order", description = "can be last or first. Last is default", in = ParameterIn.QUERY, schema = @Schema(type = "string")),
            },
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "List of enrolments",
                            content = @Content(
                                    schema = @Schema(implementation = Enrolment.class))
                    ),
                    @ApiResponse(responseCode = "404",
                            description = "Enrolment for student id and studyProgramId doesn't exist",
                            content = @Content(
                                    schema = @Schema(implementation = ResponseError.class))
                    )
            })
    @Path("{studentId}")
    @GET
    public Response getEnrolment(@PathParam("studentId") int studentId, @QueryParam("order") String order, @QueryParam("studyProgramId") String studyProgramId) {
        Enrolment en;

        if(order != null) {
            if (order.equals("first")) {
                if (studyProgramId == null)
                    return Response.status(400).entity(new ResponseError(400, "No query parameter studyProgramId")).build();
                en = enB.getFirstEnrolmentByStudentIdAndProgram(studentId, studyProgramId);
            } else { // order == "last"
                if (studyProgramId != null)
                    return Response.status(400).entity(new ResponseError(400, "Query parameter studyProgramId is not impelmented for last")).build();
                en = enB.getLastEnrolmentByStudentId(studentId);
            }
            return Response.ok(en).build();
        } else {
            return Response.ok(enB.getEnrolmentsForStudent(studentId)).build();
        }
    }

    @Operation(description = "Creates new enrolment for a student and adds his courses", summary = "Create new enrolment for a student and add his courses",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Enrolment successful",
                            content = @Content(
                                    schema = @Schema(implementation = EnrolmentSheet.class))
                    ),
                    @ApiResponse(responseCode = "404",
                            description = "Enrolment for student failed",
                            content = @Content(
                                    schema = @Schema(implementation = ResponseError.class))
                    )
            })
    @POST
    public Response CreateEnrolmentAndAddCourses(@RequestBody EnrolmentSheet es) {
        if(es == null) throw new NoRequestBodyException();
        EnrolmentToken enToken = enrolmentTokenBean.getEnrolmentTokenByStudentId(es.getStudent().getId());
        List<String> list = enrolmentPolicyBean.checkEnrolment(es, enToken);
        if(list.isEmpty()) {
            // set token as used
            enToken.setUsed(true);
            enrolmentTokenBean.updateEnrolmentToken(enToken);
            // add an enrolment and courses for the used token
            enB.putEnrolment(es.getEnrolmentToken(), es.getCourses());
            // update student from sheet
            sB.updateStudent(es.getStudent());
            
            return Response.ok(es).build();
        } else {
            return Response.status(400).entity(new ResponseError(400, list.toArray(new String[0]))).build();
        }
    }
}
