package api.sources;

import api.interceptors.annotations.LogApiCalls;
import api.mappers.ResponseError;
import beans.crud.StudentCoursesBean;
import entities.curriculum.StudentCourses;
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
@Path("studentcourses")
@ApplicationScoped
@LogApiCalls
@Tags(value = @Tag(name = "studentcourses"))
public class StudentCoursesSource {

    @Inject
    private StudentCoursesBean scB;

    @Operation(description = "Returns a list of student's courses.", summary = "Get list of student's courses", responses = {
            @ApiResponse(responseCode = "200",
                    description = "List of student's courses",
                    content = @Content(
                            schema = @Schema(implementation = StudentCourses.class))
            )
    })
    @GET
    public Response getAllStudentCourses() {
        return Response.ok(scB.getAllStudentCourses()).build();
    }

    @Operation(description = "Returns a student's courses by enrolment id.", summary = "Get student's courses by enrolment id", responses = {
            @ApiResponse(responseCode = "200",
                    description = "Student's courses by enrolment id",
                    content = @Content(
                            schema = @Schema(implementation = StudentCourses.class))
            ),
            @ApiResponse(responseCode = "404",
                    description = "Student's courses by this enrolment id don't exist",
                    content = @Content(
                            schema = @Schema(implementation = ResponseError.class))
            )
    })
    @Path("{enrolment_id}")
    @GET
    public Response getStudentCoursesByEnrolmentId(@PathParam("enrolment_id") int enrolment_id) {
        return Response.ok(scB.getStudentCoursesByEnrolmentId(enrolment_id)).build();
    }

}
