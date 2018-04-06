package api.sources;

import api.exceptions.NoRequestBodyException;
import api.interceptors.annotations.LogApiCalls;
import api.mappers.ResponseError;
import beans.crud.CourseBean;
import entities.curriculum.Course;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("courses")
@ApplicationScoped
@LogApiCalls
@Tags(value = @Tag(name = "course"))
public class CourseSource {

    @Inject
    private CourseBean cB;

    @Operation(description = "Returns list of courses.", summary = "List of courses", tags = "course", responses = {
            @ApiResponse(responseCode = "200",
                    description = "List of courses",
                    content = @Content(
                            schema = @Schema(implementation
                                    = Course.class))
            )
    })
    @GET
    public Response getCourses() {
        return Response.ok(cB.getCourses()).build();
    }

    @Operation(description = "Returns a course with specified id.", summary = "Course by id", tags = {"students", "course"}, responses = {
            @ApiResponse(responseCode = "200",
                    description = "Course by id",
                    content = @Content(
                            schema = @Schema(implementation
                                    = Course.class))
            ),
            @ApiResponse(responseCode = "404",
                    description = "Course by id doesn't exist",
                    content = @Content(
                            schema = @Schema(implementation
                                    = ResponseError.class))
            )
    })
    @Path("{id}")
    @GET
    public Response getEnrolmentById(@PathParam("id") int id) {
        return Response.ok(cB.getCourse(id)).build();
    }
    
    @PUT
    public Response createCourse(@RequestBody Course c) {
        if(c == null) throw new NoRequestBodyException();
        if(cB.existsCourse(c.getId())) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ResponseError(400, "ID already exists")).build();
        }
        cB.insertCourse(c);
        return Response.ok().entity(c).build();
    }

    @Path("{id}")
    @DELETE
    public Response deleteCourse(@PathParam("id") int id) {
        cB.deleteCourse(id);
        return Response.ok().build();
    }

    @POST
    public Response updateCourse(@RequestBody Course c) {
        if(c == null) throw new NoRequestBodyException();
        cB.updateCourse(c);
        return Response.ok().entity(c).build();
    }

}
