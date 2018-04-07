package api.sources;

import api.exceptions.NoRequestBodyException;
import api.interceptors.annotations.LogApiCalls;
import api.mappers.ResponseError;
import beans.crud.StudyYearBean;
import entities.StudyYear;
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
@Path("studyyears")
@ApplicationScoped
@LogApiCalls
@Tags(value = @Tag(name = "study years"))
public class StudyYearSource {

    @Inject
    private StudyYearBean cb;

    @Operation(description = "Returns a list of study years.", summary = "Get list of study years", responses = {
            @ApiResponse(responseCode = "200",
                    description = "List of study years",
                    content = @Content(
                            schema = @Schema(implementation = StudyYear.class))
            )
    })
    @GET
    public Response getStudyYears() {
        return Response.ok().entity(cb.getStudyYears()).build();
    }

    @Operation(description = "Returns a study year with specified id.", summary = "Get study year by id", responses = {
            @ApiResponse(responseCode = "200",
                    description = "Study year by id",
                    content = @Content(
                            schema = @Schema(implementation = StudyYear.class))
            ),
            @ApiResponse(responseCode = "404",
                    description = "Study year by id doesn't exist",
                    content = @Content(
                            schema = @Schema(implementation = ResponseError.class))
            )
    })
    @Path("{id}")
    @GET
    public Response getStudyYear(@PathParam("id") int id) {
        return Response.ok().entity(cb.getStudyYear(id)).build();
    }

    @Operation(description = "Inserts a new study year.", summary = "Insert study year", responses = {
            @ApiResponse(responseCode = "200",
                    description = "Insert successful",
                    content = @Content(
                            schema = @Schema(implementation = StudyYear.class))),
            @ApiResponse(responseCode = "400",
                    description = "Insert failed",
                    content = @Content(
                            schema = @Schema(implementation = ResponseError.class)))
    })
    @PUT
    public Response createStudyYear(@RequestBody StudyYear c) {
        if(c == null) throw new NoRequestBodyException();
        if(cb.existsStudyYear(c.getId())) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ResponseError(400, "ID already exists")).build();
        }
        cb.insertStudyYear(c);
        return Response.ok().entity(c).build();
    }

    @Operation(description = "Deletes a study year with specified id.", summary = "Delete study year", responses = {
            @ApiResponse(responseCode = "200",
                    description = "Delete successful",
                    content = @Content(
                            schema = @Schema(implementation = StudyYear.class))),
    })
    @Path("{id}")
    @DELETE
    public Response deleteStudyYear(@PathParam("id") int id) {
        cb.deleteStudyYear(id);
        return Response.ok().build();
    }

    @Operation(description = "Updates an existing study year.", summary = "Update study year", responses = {
            @ApiResponse(responseCode = "200",
                    description = "Update successful",
                    content = @Content(
                            schema = @Schema(implementation = StudyYear.class))),
            @ApiResponse(responseCode = "400",
                    description = "Update failed",
                    content = @Content(
                            schema = @Schema(implementation = ResponseError.class)))
    })
    @POST
    public Response updateStudyYear(@RequestBody StudyYear c) {
        if(c == null) throw new NoRequestBodyException();
        cb.updateStudyYear(c);
        return Response.ok().entity(c).build();
    }
}
