package api.sources;

import api.exceptions.NoRequestBodyException;
import api.interceptors.annotations.LogApiCalls;
import api.mappers.ResponseError;
import beans.crud.StudyProgramBean;
import entities.StudyProgram;
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
@Path("studyprograms")
@ApplicationScoped
@LogApiCalls
@Tags(value = @Tag(name = "study programs"))
public class StudyProgramSource {

    @Inject
    private StudyProgramBean cb;

    @Operation(description = "Returns a list of study programs.", summary = "Get list of study programs", responses = {
            @ApiResponse(responseCode = "200",
                    description = "List of study programs",
                    content = @Content(
                            schema = @Schema(implementation = StudyProgram.class))
            )
    })
    @GET
    public Response getStudyPrograms() {
        return Response.ok().entity(cb.getStudyPrograms()).build();
    }

    @Operation(description = "Returns a study program with specified id.", summary = "Get study program by id", responses = {
            @ApiResponse(responseCode = "200",
                    description = "Study program by id",
                    content = @Content(
                            schema = @Schema(implementation = StudyProgram.class))
            ),
            @ApiResponse(responseCode = "404",
                    description = "Study program by id doesn't exist",
                    content = @Content(
                            schema = @Schema(implementation = ResponseError.class))
            )
    })
    @Path("{id}")
    @GET
    public Response getStudyProgram(@PathParam("id") String id) {
        return Response.ok().entity(cb.getStudyProgram(id)).build();
    }

    @Operation(description = "Inserts a new study program.", summary = "Insert study program", responses = {
            @ApiResponse(responseCode = "200",
                    description = "Insert successful",
                    content = @Content(
                            schema = @Schema(implementation = StudyProgram.class))),
            @ApiResponse(responseCode = "400",
                    description = "Insert failed",
                    content = @Content(
                            schema = @Schema(implementation = ResponseError.class)))
    })
    @PUT
    public Response createStudyProgram(@RequestBody StudyProgram c) {
        if(c == null) throw new NoRequestBodyException();
        if(cb.existsStudyProgram(c.getId())) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ResponseError(400, "ID already exists")).build();
        }
        cb.insertStudyProgram(c);
        return Response.ok().entity(c).build();
    }

    @Operation(description = "Deletes a study program with specified id.", summary = "Delete study program", responses = {
            @ApiResponse(responseCode = "200",
                    description = "Delete successful",
                    content = @Content(
                            schema = @Schema(implementation = StudyProgram.class))),
    })
    @Path("{id}")
    @DELETE
    public Response deleteStudyProgram(@PathParam("id") String id) {
        cb.deleteStudyProgram(id);
        return Response.ok().build();
    }

    @Operation(description = "Updates an existing study program.", summary = "Update study program", responses = {
            @ApiResponse(responseCode = "200",
                    description = "Update successful",
                    content = @Content(
                            schema = @Schema(implementation = StudyProgram.class))),
            @ApiResponse(responseCode = "400",
                    description = "Update failed",
                    content = @Content(
                            schema = @Schema(implementation = ResponseError.class)))
    })
    @POST
    public Response updateStudyProgram(@RequestBody StudyProgram c) {
        if(c == null) throw new NoRequestBodyException();
        cb.updateStudyProgram(c);
        return Response.ok().entity(c).build();
    }
}

