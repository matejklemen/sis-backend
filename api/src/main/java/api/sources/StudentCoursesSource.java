package api.sources;

import api.interceptors.annotations.LogApiCalls;
import beans.logic.ExamSignUpLogicBean;
import pojo.ResponseError;
import beans.crud.StudentCoursesBean;
import com.kumuluz.ee.rest.beans.QueryParameters;
import entities.curriculum.StudentCourses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.logging.Logger;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("studentcourses")
@ApplicationScoped
@LogApiCalls
@Tags(value = @Tag(name = "studentcourses"))
public class StudentCoursesSource {

    private final Logger log = Logger.getLogger(this.getClass().getName());

    @Context
    protected UriInfo uriInfo;

    @Inject
    private StudentCoursesBean scB;
    @Inject
    private ExamSignUpLogicBean esulB;

    @Operation(description = "Returns a list of student's courses.", summary = "Get list of student's courses", responses = {
            @ApiResponse(responseCode = "200",
                    description = "List of student's courses",
                    content = @Content(
                            schema = @Schema(implementation = StudentCourses.class))
            )},
            parameters = {
                    @Parameter(name = "offset", description = "Starting point",in = ParameterIn.QUERY),
                    @Parameter(name = "limit", description = "Number of returned entities", in = ParameterIn.QUERY),
                    @Parameter(name = "order", description = "Order", in = ParameterIn.QUERY)
            })
    @GET
    public Response getAllStudentCourses() {
        QueryParameters query = QueryParameters.query(uriInfo.getRequestUri().getQuery()).build();
        return Response.ok(scB.getAllStudentCourses(query)).build();
    }

    @GET
    @Path("count")
    public Response geNumberOfAllStudentCourses() {
        return Response.status(Response.Status.OK).entity(scB.getAllStudentCourses(new QueryParameters()).size()).build();
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

    @GET
    @Path("student/{regNumber}")
    public Response getCurrentStudentCoursesByRegisterNumber(@PathParam("regNumber") String registerNumber) {
        List<StudentCourses> res = esulB.getCoursesByRegisterNumber(registerNumber);

        return res == null? Response.status(Response.Status.NOT_FOUND).entity(ResponseError.error404()).build():
                Response.status(Response.Status.OK).entity(res).build();
    }

}
