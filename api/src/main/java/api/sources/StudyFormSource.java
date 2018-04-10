package api.sources;

import api.exceptions.NoRequestBodyException;
import api.interceptors.annotations.LogApiCalls;
import api.mappers.ResponseError;
import beans.crud.StudyFormBean;
import entities.StudyForm;
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
@Path("studyforms")
@ApplicationScoped
@LogApiCalls
@Tags(value = @Tag(name = "study forms"))
public class StudyFormSource {

    @Inject
    private StudyFormBean cB;

    @Operation(description = "Returns a list of study forms.", summary = "Get list of study forms", responses = {
            @ApiResponse(responseCode = "200",
                    description = "List of study forms",
                    content = @Content(
                            schema = @Schema(implementation = StudyForm.class))
            )
    })
    @GET
    public Response getStudyForms() {
        return Response.ok(cB.getStudyForms()).build();
    }

    @Operation(description = "Returns a study form with specified id.", summary = "Get study form by id", responses = {
            @ApiResponse(responseCode = "200",
                    description = "Study form by id",
                    content = @Content(
                            schema = @Schema(implementation = StudyForm.class))
            ),
            @ApiResponse(responseCode = "404",
                    description = "Study form by id doesn't exist",
                    content = @Content(
                            schema = @Schema(implementation = ResponseError.class))
            )
    })
    @Path("{id}")
    @GET
    public Response getStudyFormById(@PathParam("id") int id) {
        return Response.ok(cB.getStudyForm(id)).build();
    }

    @Operation(description = "Inserts a new study form.", summary = "Insert study form", responses = {
            @ApiResponse(responseCode = "200",
                    description = "Insert successful",
                    content = @Content(
                            schema = @Schema(implementation = StudyForm.class))),
            @ApiResponse(responseCode = "400",
                    description = "Insert failed",
                    content = @Content(
                            schema = @Schema(implementation = ResponseError.class)))
    })
    @PUT
    public Response createStudyForm(@RequestBody StudyForm c) {
        if(c == null) throw new NoRequestBodyException();
        if(cB.existsStudyForm(c.getId())) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ResponseError(400, "ID already exists")).build();
        }
        c = cB.insertStudyForm(c);
        return Response.ok().entity(c).build();
    }

    @Operation(description = "Deletes a study form with specified id.", summary = "Delete study form", responses = {
            @ApiResponse(responseCode = "200",
                    description = "Delete successful",
                    content = @Content(
                            schema = @Schema(implementation = StudyForm.class))),
    })
    @Path("{id}")
    @DELETE
    public Response deleteStudyForm(@PathParam("id") int id) {
        cB.deleteStudyForm(id);
        return Response.ok().build();
    }

    @Operation(description = "Updates an existing study form.", summary = "Update study form", responses = {
            @ApiResponse(responseCode = "200",
                    description = "Update successful",
                    content = @Content(
                            schema = @Schema(implementation = StudyForm.class))),
            @ApiResponse(responseCode = "400",
                    description = "Update failed",
                    content = @Content(
                            schema = @Schema(implementation = ResponseError.class)))
    })
    @POST
    public Response updateStudyForm(@RequestBody StudyForm c) {
        if(c == null) throw new NoRequestBodyException();
        c = cB.updateStudyForm(c);
        return Response.ok().entity(c).build();
    }

}
