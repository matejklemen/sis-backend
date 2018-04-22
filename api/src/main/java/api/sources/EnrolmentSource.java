package api.sources;

import api.exceptions.NoRequestBodyException;
import api.interceptors.annotations.LogApiCalls;
import beans.crud.*;
import beans.logic.EnrolmentPolicyBean;
import entities.Enrolment;
import entities.EnrolmentToken;
import entities.curriculum.Course;
import entities.curriculum.StudentCourses;
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

    @Inject private StudentBean sB;
    @Inject private EnrolmentBean enB;
    @Inject private EnrolmentTokenBean enrolmentTokenBean;
    @Inject private EnrolmentPolicyBean enrolmentPolicyBean;
    @Inject private StudentCoursesBean scB;
    @Inject private CourseBean cB;

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

    @Operation(description = "Update and confirm enrolment", summary = "Update and confirm enrolment for student",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Enrolment successful"
                    ),
                    @ApiResponse(responseCode = "404",
                            description = "Enrolment for student failed"
                    )
            })
    @POST
    @Path("confirm/{id}")
    public Response UpdateAndConfirmEnrolment(@RequestBody EnrolmentSheet es){
        if(es == null) throw new NoRequestBodyException();

        // update student
        sB.updateStudent(es.getStudent());

        // set enrolment as confirmed
        Enrolment e = enB.getLastEnrolmentByStudentId(es.getStudent().getId());
        e.setConfirmed(true);

        EnrolmentToken newET = es.getEnrolmentToken();
        e.setForm(newET.getForm());
        e.setKind(newET.getKind());
        e.setKlasiusSrv(newET.getKlasiusSrv());
        e.setStudyProgram(newET.getStudyProgram());
        e.setStudyYear(newET.getStudyYear());
        e.setType(newET.getType());
        e.setYear(newET.getYear());


        enB.updateEnrolment(e);

        // Update courses
        List<Integer> upcomingCourses = es.getCourses();
        List<StudentCourses> sc = scB.getStudentCoursesByEnrolmentId(e.getId());
        for ( StudentCourses course : sc) {
            Integer oldCourseId = course.getCourse().getId();
            // No change was made
            if(upcomingCourses.contains(oldCourseId)){
                // Remove from list
                upcomingCourses.remove((oldCourseId));
            }else{
                // Remove old
                scB.deleteStudentCourse(course);
            }
        }

        for(Integer newCourseId : upcomingCourses){
            Course c = cB.getCourse(newCourseId);
            StudentCourses studentCourse = new StudentCourses();

            studentCourse.setCourse(c);
            studentCourse.setEnrolment(e);

            scB.insertCourse(studentCourse);
        }


        return Response.ok(es).build();

    }
}

