package api.sources;

import api.exceptions.NoRequestBodyException;
import api.interceptors.annotations.LogApiCalls;
import api.mappers.ResponseError;
import beans.crud.StudyKindBean;
import entities.StudyKind;
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
@Path("studykinds")
@ApplicationScoped
@LogApiCalls
@Tags(value = @Tag(name = "study kinds"))
public class StudyKindSource {

    @Inject
    private StudyKindBean cB;

    @Operation(description = "Returns a list of study kinds.", summary = "Get list of study kinds", responses = {
            @ApiResponse(responseCode = "200",
                    description = "List of study kinds",
                    content = @Content(
                            schema = @Schema(implementation = StudyKind.class))
            )
    })
    @GET
    public Response getStudyKinds() {
        return Response.ok(cB.getStudyKinds()).build();
    }

    @Operation(description = "Returns a study kind with specified id.", summary = "Get study kind by id", responses = {
            @ApiResponse(responseCode = "200",
                    description = "Study kind by id",
                    content = @Content(
                            schema = @Schema(implementation = StudyKind.class))
            ),
            @ApiResponse(responseCode = "404",
                    description = "Study kind by id doesn't exist",
                    content = @Content(
                            schema = @Schema(implementation = ResponseError.class))
            )
    })
    @Path("{id}")
    @GET
    public Response getStudyKindById(@PathParam("id") int id) {
        return Response.ok(cB.getStudyKind(id)).build();
    }

    @Operation(description = "Inserts a new study kind.", summary = "Insert study kind", responses = {
            @ApiResponse(responseCode = "200",
                    description = "Insert successful",
                    content = @Content(
                            schema = @Schema(implementation = StudyKind.class))),
            @ApiResponse(responseCode = "400",
                    description = "Insert failed",
                    content = @Content(
                            schema = @Schema(implementation = ResponseError.class)))
    })
    @PUT
    public Response createStudyKind(@RequestBody StudyKind c) {
        if(c == null) throw new NoRequestBodyException();
        if(cB.existsStudyKind(c.getId())) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ResponseError(400, "ID already exists")).build();
        }
        c = cB.insertStudyKind(c);
        return Response.ok().entity(c).build();
    }

    @Operation(description = "Deletes a study kind with specified id.", summary = "Delete study kind", responses = {
            @ApiResponse(responseCode = "200",
                    description = "Delete successful",
                    content = @Content(
                            schema = @Schema(implementation = StudyKind.class))),
    })
    @Path("{id}")
    @DELETE
    public Response deleteStudyKind(@PathParam("id") int id) {
        cB.deleteStudyKind(id);
        return Response.ok().build();
    }

    @Operation(description = "Updates an existing study kind.", summary = "Update study kind", responses = {
            @ApiResponse(responseCode = "200",
                    description = "Update successful",
                    content = @Content(
                            schema = @Schema(implementation = StudyKind.class))),
            @ApiResponse(responseCode = "400",
                    description = "Update failed",
                    content = @Content(
                            schema = @Schema(implementation = ResponseError.class)))
    })
    @POST
    public Response updateStudyKind(@RequestBody StudyKind c) {
        if(c == null) throw new NoRequestBodyException();
        c = cB.updateStudyKind(c);
        return Response.ok().entity(c).build();
    }

}
