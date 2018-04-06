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

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("courses")
@ApplicationScoped
@LogApiCalls
@Tags(value = @Tag(name = "courses"))
public class CourseSource {

    @Inject
    private CourseBean cB;

    @Operation(description = "Returns a list of courses.", summary = "Get list of courses", responses = {
            @ApiResponse(responseCode = "200",
                    description = "List of courses",
                    content = @Content(
                            schema = @Schema(implementation = Course.class))
            )
    })
    @GET
    public Response getCourses() {
        return Response.ok(cB.getCourses()).build();
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
    public Response getEnrolmentById(@PathParam("id") int id) {
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
            return Response.status(Response.Status.BAD_REQUEST).entity(new ResponseError(400, "ID already exists")).build();
        }
        cB.insertCourse(c);
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
        cB.updateCourse(c);
        return Response.ok().entity(c).build();
    }

}
