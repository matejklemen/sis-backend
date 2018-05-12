package api.sources;

import api.interceptors.annotations.LogApiCalls;
import beans.logic.GradeDataBean;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import pojo.GradeData;
import pojo.ResponseError;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("gradeData")
@ApplicationScoped
@LogApiCalls
@Tags(value = @Tag(name = "gradeData"))
public class GradeDataSource {

    @Inject private GradeDataBean gdb;

    @Operation(description = "Returns a list of grade data.", summary = "Returns a list of grade data",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Grade data for student id",
                            content = @Content(
                                    schema = @Schema(implementation = GradeData.class))
                    ),
                    @ApiResponse(responseCode = "404",
                            description = "Course by id doesn't exist",
                            content = @Content(
                                    schema = @Schema(implementation = ResponseError.class))
                    )})
    @Path("{studentId}")
    @GET
    public Response getCourses(@PathParam("studentId") int studentId) {
        return Response.ok().entity(gdb.getStudentGradeData(studentId)).build();
    }
}
