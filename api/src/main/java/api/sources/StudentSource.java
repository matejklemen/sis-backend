package api.sources;

import api.interceptors.annotations.LogApiCalls;
import api.mappers.ResponseError;
import beans.crud.StudentBean;
import entities.Student;
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
import java.util.List;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("students")
@ApplicationScoped
@LogApiCalls
@Tags(value = @Tag(name = "students"))
public class StudentSource {

    @Inject
    private StudentBean sdB;

    @Operation(description = "Returns a list of students.", summary = "Get list of students", responses = {
            @ApiResponse(responseCode = "200",
                    description = "List of students",
                    content = @Content(
                            schema = @Schema(implementation = Student.class))
            )
    })
    @GET
    public Response getStudents() {
        return Response.ok(sdB.getStudents()).build();
    }

    @Operation(description = "Returns a student with specified id.", summary = "Get student by id", responses = {
            @ApiResponse(responseCode = "200",
                    description = "Student by id",
                    content = @Content(
                            schema = @Schema(implementation = Student.class))
            ),
            @ApiResponse(responseCode = "404",
                    description = "Student by id doesn't exist",
                    content = @Content(
                            schema = @Schema(implementation = ResponseError.class))
            )
    })
    @Path("{id}")
    @GET
    public Response getStudentById(@PathParam("id") int id) {
        Student sd = sdB.getStudentById(id);
        return Response.ok(sd).build();
    }

    @Operation(description = "Returns a student with specified register number.", summary = "Get student by register number", responses = {
            @ApiResponse(responseCode = "200",
                    description = "Student by register number",
                    content = @Content(
                            schema = @Schema(implementation = Student.class))
            ),
            @ApiResponse(responseCode = "404",
                    description = "Student by register number doesn't exist",
                    content = @Content(
                            schema = @Schema(implementation = ResponseError.class))
            )
    })
    @Path("s/{regno}")
    @GET
    public Response getStudentByRegisterNumber(@PathParam("regno") String regno) {
        Student sd = sdB.getStudentByRegisterNumber(regno);
        return Response.ok(sd).build();
    }

    @Operation(description = "Returns a list of students. Same as '/students'.", summary = "Get list of students", responses = {
            @ApiResponse(responseCode = "200",
                    description = "List of students",
                    content = @Content(
                            schema = @Schema(implementation = Student.class))
            )
    })
    @Path("search")
    @GET
    public Response getStudentsWithEmptySearch() {
        return getStudents();
    }

    @Operation(description = "Returns a list of students that match the search query. Query can partially match either the register number, name or surname. It's possible that the returned list is empty.", summary = "Get list of students by search query", responses = {
            @ApiResponse(responseCode = "200",
                    description = "List of students by search query",
                    content = @Content(
                            schema = @Schema(implementation
                                    = Student.class))
            )
    })
    @Path("search/{query}")
    @GET
    public Response getStudents(@PathParam("query") String query) {
        List sdl = sdB.searchStudents(query);
        return Response.ok(sdl).build();
    }

}
