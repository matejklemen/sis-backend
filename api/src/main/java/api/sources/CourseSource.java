package api.sources;

import api.exceptions.NoRequestBodyException;
import api.interceptors.annotations.LogApiCalls;
import beans.crud.StudentCoursesBean;
import pojo.ResponseError;
import beans.crud.CourseBean;
import com.kumuluz.ee.rest.beans.QueryParameters;
import entities.curriculum.Course;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
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
import java.util.logging.Logger;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("courses")
@ApplicationScoped
@LogApiCalls
@Tags(value = @Tag(name = "courses"))
public class CourseSource {

    private final Logger log = Logger.getLogger(this.getClass().getName());

    @Context
    protected UriInfo uriInfo;

    @Inject
    private CourseBean cB;

    @Inject
    StudentCoursesBean scB;

    @Operation(description = "Returns a list of courses.", summary = "Get list of courses", responses = {
            @ApiResponse(responseCode = "200",
                    description = "List of courses",
                    content = @Content(
                            schema = @Schema(implementation = Course.class))
            )},
            parameters = {
                    @Parameter(name = "offset", description = "Starting point",in = ParameterIn.QUERY),
                    @Parameter(name = "limit", description = "Number of returned entities", in = ParameterIn.QUERY),
                    @Parameter(name = "order", description = "Order", in = ParameterIn.QUERY)
            })
    @GET
    public Response getCourses() {
        QueryParameters query = QueryParameters.query(uriInfo.getRequestUri().getQuery()).build();
        return Response
                .ok(cB.getCourses(query))
                .header("X-Total-Count", cB.getCourses(new QueryParameters()).size())
                .build();
    }

    @GET
    @Path("count")
    public Response getNumberOfCourses() {
        return Response.status(Response.Status.OK).entity(cB.getCourses(new QueryParameters()).size()).build();
    }

    @Operation(description = "Returns a course with specified id.", summary = "Get course by id", responses = {
            @ApiResponse(responseCode = "200",
                    description = "Course by id",
                    content = @Content(
                            schema = @Schema(implementation = Course.class))
            ),
            @ApiResponse(responseCode = "404",
                    description = "Course by id doesn't exist",
                    content = @Content(
                            schema = @Schema(implementation = ResponseError.class))
            )
    })
    @Path("{id}")
    @GET
    public Response getCourseById(@PathParam("id") int id) {
        return Response.ok(cB.getCourse(id)).build();
    }

    @Operation(description = "Inserts a new course.", summary = "Insert course", responses = {
            @ApiResponse(responseCode = "200",
                    description = "Insert successful",
                    content = @Content(
                            schema = @Schema(implementation = Course.class))),
            @ApiResponse(responseCode = "400",
                    description = "Insert failed",
                    content = @Content(
                            schema = @Schema(implementation = ResponseError.class)))
    })
    @PUT
    public Response createCourse(@RequestBody Course c) {
        if(c == null) throw new NoRequestBodyException();
        if(cB.existsCourse(c.getId())) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ResponseError.errorIdAlreadyExists()).build();
        }
        c = cB.insertCourse(c);
        return Response.ok().entity(c).build();
    }

    @Operation(description = "Deletes a course with specified id.", summary = "Delete course", responses = {
            @ApiResponse(responseCode = "200",
                    description = "Delete successful",
                    content = @Content(
                            schema = @Schema(implementation = Course.class))),
    })
    @Path("{id}")
    @DELETE
    public Response deleteCourse(@PathParam("id") int id) {
        cB.deleteCourse(id);
        return Response.ok().build();
    }

    @Operation(description = "Updates an existing course.", summary = "Update course", responses = {
            @ApiResponse(responseCode = "200",
                    description = "Update successful",
                    content = @Content(
                            schema = @Schema(implementation = Course.class))),
            @ApiResponse(responseCode = "400",
                    description = "Update failed",
                    content = @Content(
                            schema = @Schema(implementation = ResponseError.class)))
    })
    @POST
    public Response updateCourse(@RequestBody Course c) {
        if(c == null) throw new NoRequestBodyException();
        c = cB.updateCourse(c);
        return Response.ok().entity(c).build();
    }

    @Operation(description = "Returns a list of courses for enrolment id.", summary = "Get list of courses for enrolment id.", responses = {
            @ApiResponse(responseCode = "200",
                    description = "List of courses",
                    content = @Content(
                            schema = @Schema(implementation = Course.class))
            )})
    @GET
    @Path("enrolment/{id}")
    public Response getCoursesByEnrolmentId(@PathParam("id") int id) {
        return Response.ok().entity(scB.getStudentCoursesByEnrolmentId(id)).build();
    }
}
