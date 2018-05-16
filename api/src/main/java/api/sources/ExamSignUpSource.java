package api.sources;

import api.interceptors.annotations.LogApiCalls;
import beans.crud.*;
import beans.logic.ExamSignUpLogicBean;
import entities.ExamSignUpHistory;
import entities.curriculum.ExamSignUp;
import entities.curriculum.StudentCourses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import pojo.ResponseError;
import pojo.SignUpInfoResponse;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("exam-sign-up")
@ApplicationScoped
@LogApiCalls
@Tags(value = @Tag(name = "exam signup"))
public class ExamSignUpSource {

    @Inject private ExamSignUpBean esb;
    @Inject private ExamSignUpLogicBean esulb;
    @Inject private CourseExamTermBean cetb;
    @Inject private ExamSignUpHistoryBean esuhb;
    @Inject private UserLoginBean ulb;
    @Inject private StudentCoursesBean scb;

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

        List<ExamSignUp> signups = esb.getExamSignUpsOnCourseForStudent(courseId, studentId);

        return signups == null ? Response.status(Response.Status.NOT_FOUND).build():
                Response.status(Response.Status.OK).entity(signups).build();
    }

    @Path("augmented")
    @GET
    // get exam sign ups and their final grade for an exam term
    public Response getAugmentedSignUpsForExamTerm(@QueryParam("examtermid") int idExamTerm) {
        List<ExamSignUp> signups = esb.getExamSignUpsByExamTerm(idExamTerm);

        if(signups == null || signups.isEmpty())
            return Response.status(Response.Status.NOT_FOUND).build();

        List<SignUpInfoResponse> signUpInfo = new ArrayList<>(signups.size());

        // copying only the necessary data into new object so that we do not waste bandwidth
        for(int idx = 0; idx < signups.size(); idx++) {
            StudentCourses currStudentCourseInfo = scb.getStudentCourses(signups.get(idx).
                    getStudentCourses().getIdStudentCourses());

            Integer numPriorTakings = esb.getNumberOfExamTakingsBeforeDatetime(currStudentCourseInfo.getIdStudentCourses(),
                    signups.get(idx).getCourseExamTerm().getDatetimeObject());

            SignUpInfoResponse currInfo = new SignUpInfoResponse();
            currInfo.setStudentInfo(currStudentCourseInfo.getEnrolment().getStudent());
            currInfo.setYearOfTakingCourse(currStudentCourseInfo.getEnrolment().getStudyYear());
            currInfo.setFinalGrade(signups.get(idx).getCurrFinalGrade());
            currInfo.setCurrentGrade(signups.get(idx).getGrade());
            currInfo.setCurrentNumberOfTakings(numPriorTakings);

            signUpInfo.add(idx, currInfo);
        }

        return Response.status(Response.Status.OK).entity(signUpInfo).build();
    }

    @Operation(description = "Signs up a student for an exam.", summary = "Sign-up student for exam",
            parameters = {
                    @Parameter(name = "studentId", required = true, description = "Student id", in = ParameterIn.QUERY, schema = @Schema(type = "int")),
                    @Parameter(name = "studentCoursesId", required = true, description = "StudentCourses id", in = ParameterIn.QUERY, schema = @Schema(type = "int")),
                    @Parameter(name = "courseExamTermId", required = true, description = "CourseExamTerm id", in = ParameterIn.QUERY, schema = @Schema(type = "int")),
                    @Parameter(name = "userLoginId", description = "UserLogin id (optional for students) - if given user login refers to a 'referent' role, all tests are ignored (basically force sign-up).")
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
    public Response addExamSignUp(@QueryParam("studentId") Integer studentId,
                                  @QueryParam("studentCoursesId") Integer studentCoursesId,
                                  @QueryParam("courseExamTermId") Integer courseExamTermId,
                                  @QueryParam("userLoginId") Integer userLoginId,
                                  @QueryParam("force") Boolean force) {
        List<String> err = esulb.addExamSignUp(studentId, studentCoursesId, courseExamTermId, userLoginId, force);

        if(err.isEmpty()) {
            ExamSignUp esu = esb.getExamSignUp(courseExamTermId, studentCoursesId);
            return Response.ok(esu).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ResponseError(400, err.toArray(new String[0]))).build();
        }
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
    public Response returnExamSignUp(@QueryParam("courseExamTermId") int courseExamTermId,
                                     @QueryParam("studentCourseId") int studentCourseId,
                                     @QueryParam("loginId") int loginId,
                                     @QueryParam("force") Boolean force) {

        List<String> err = esulb.returnExamSignUp(courseExamTermId, studentCourseId, loginId, force);

        if(err.isEmpty()) {
            ExamSignUp esu = esb.getExamSignUp(courseExamTermId, studentCourseId);
            return Response.ok().entity(esu).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ResponseError(400, err.toArray(new String[0]))).build();
        }
    }


    @Operation(description = "Get exam sign up history.", summary = "Get exam sign up history.",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Exam sign up istory",
                            content = @Content(
                                    schema = @Schema(implementation = ExamSignUpHistory.class))),
                    @ApiResponse(responseCode = "404",
                            description = "No exam sign up with given ids",
                            content = @Content(
                                    schema = @Schema(implementation = ResponseError.class)))
            })
    @GET
    @Path("/history")
    public Response getExamSignUpHistory(@QueryParam("courseExamTermId") int courseExamTermId,
                                         @QueryParam("studentCourseId") int studentCourseId){
        ExamSignUp esu = esb.getExamSignUp(courseExamTermId, studentCourseId);

        if(esu == null)
            return Response.status(Response.Status.NOT_FOUND).build();

        return Response.ok().entity(esulb.getExamSignUpHistry(esu)).build();
    }

}
