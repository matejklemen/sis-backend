package api.sources;

import api.interceptors.annotations.LogApiCalls;
import beans.crud.ExamSignUpBean;
import beans.logic.ExamSignUpLogicBean;
import entities.curriculum.ExamSignUp;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
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

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("exam-sign-up")
@ApplicationScoped
@LogApiCalls
@Tags(value = @Tag(name = "exam signup"))
public class ExamSignUpSource {

    @Inject
    private ExamSignUpBean esb;
    @Inject
    private ExamSignUpLogicBean esulb;

    @Operation(description = "Returns exam sign-ups for a student (and course).", summary = "Get exam sign-ups by studentId and/or query parameters",
            parameters = {
                    @Parameter(name = "studentId", required = true, description = "Student id", in = ParameterIn.QUERY, schema = @Schema(type = "int")),
                    @Parameter(name = "courseId", description = "Course id", in = ParameterIn.QUERY, schema = @Schema(type = "int")),
            },
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "List of exam sign-ups",
                            content = @Content(
                                    schema = @Schema(implementation = ExamSignUp.class))
                    ),
            })
    @GET
    public Response getExamSignUpsForExamTerm(@QueryParam("studentId") Integer studentId, @QueryParam("courseId") Integer courseId) {

        // TODO: popravil da se uporablja studentId namesto registerNumber, vendar nisem se testiral!
        List<ExamSignUp> signups = esb.getExamSignUpsOnCourseForStudent(courseId, studentId);

        return signups == null ? Response.status(Response.Status.NOT_FOUND).build():
                Response.status(Response.Status.OK).entity(signups).build();
    }

    @Operation(description = "Signs up a student for an exam.", summary = "Sign-up student for exam",
            parameters = {
                    @Parameter(name = "studentId", required = true, description = "Student id", in = ParameterIn.QUERY, schema = @Schema(type = "int")),
                    @Parameter(name = "studentCoursesId", required = true, description = "StudentCourses id", in = ParameterIn.QUERY, schema = @Schema(type = "int")),
                    @Parameter(name = "courseExamTermId", required = true, description = "CourseExamTerm id", in = ParameterIn.QUERY, schema = @Schema(type = "int")),
            },
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Sign-up successful",
                            content = @Content(
                                    schema = @Schema(implementation = ExamSignUp.class))),
                    @ApiResponse(responseCode = "400",
                            description = "Sign-up failed",
                            content = @Content(
                                    schema = @Schema(implementation = ResponseError.class)))
    })
    @PUT
    public Response addExamSignUp(@QueryParam("studentId") Integer studentId, @QueryParam("studentCoursesId") Integer studentCoursesId, @QueryParam("courseExamTermId") Integer courseExamTermId) {
        List<String> err = esulb.addExamSignUp(studentId, studentCoursesId, courseExamTermId);

        return !err.isEmpty() ? Response.status(Response.Status.BAD_REQUEST).entity(new ResponseError(400, err.toArray(new String[0]))).build() :
                Response.status(Response.Status.OK).build();
    }

    @Operation(description = "Returns exam sign up.", summary = "Returns exam sign up.",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Return successful",
                            content = @Content(
                                    schema = @Schema(implementation = ExamSignUp.class))),
                    @ApiResponse(responseCode = "400",
                            description = "Return failed",
                            content = @Content(
                                    schema = @Schema(implementation = ResponseError.class))),
                    @ApiResponse(responseCode = "404",
                            description = "No exam sign up with given ids",
                            content = @Content(
                                    schema = @Schema(implementation = ResponseError.class)))
            })
    @POST
    @Path("/return")
    public Response returnExamSignUp(@QueryParam("courseExamTermId") int courseExamTermId, @QueryParam("studentCourseId") int studentCourseId){

        ExamSignUp esu = esb.getExamSignedUp(courseExamTermId, studentCourseId);
        esu.setReturned(true);
        esu = esb.updateExamSignUp(esu);

        return Response.ok().entity(esu).build();
    }

}
