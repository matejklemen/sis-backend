package api.sources;

import api.mappers.ResponseError;
import beans.crud.CourseBean;
import entities.curriculum.Course;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

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
public class CourseSource {
    private Logger log = Logger.getLogger(getClass().getSimpleName());

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
        Course c = cB.getCourse(id);
        return Response.ok(c).build();
    }

}
