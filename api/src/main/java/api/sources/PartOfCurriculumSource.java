package api.sources;

import api.interceptors.annotations.LogApiCalls;
import api.mappers.ResponseError;
import beans.crud.PartOfCurriculumBean;
import com.sun.org.apache.regexp.internal.RE;
import entities.StudyProgram;
import entities.curriculum.PartOfCurriculum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.links.Link;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
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
import java.util.List;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("poc")
@ApplicationScoped
@LogApiCalls
@Tags(value = @Tag(name = "part of curriculum"))
public class PartOfCurriculumSource {

    @Inject
    PartOfCurriculumBean pocb;

    @Operation(description = "Retrieves all tags for parts of curriculum.", summary = "Get parts of curriculum", responses = {
            @ApiResponse(responseCode = "200",
                    description = "Retrieved data",
                    content = @Content(
                            schema = @Schema(implementation = PartOfCurriculum.class))),
            @ApiResponse(responseCode = "404",
                    description = "No data found",
                    content = @Content(
                            schema = @Schema(implementation = ResponseError.class)))
    })
    @GET
    public Response getAllPOC() {
        List<PartOfCurriculum> allPOC = pocb.getAllPOC();

        return allPOC == null ? Response.status(Response.Status.NOT_FOUND).build() :
                Response.status(Response.Status.OK).entity(allPOC).build();
    }

    @Operation(description = "Add new module (name).", summary = "Add module", responses = {
            @ApiResponse(responseCode = "200",
                    description = "Created new module",
                    content = @Content(
                            schema = @Schema(implementation = PartOfCurriculum.class))),
            @ApiResponse(responseCode = "400",
                    description = "Creation of new module failed",
                    content = @Content(
                            schema = @Schema(implementation = ResponseError.class)))
    })
    @PUT
    public Response insertNewModule(@Parameter(description = "Name of new module", required = true) String moduleName) {
        try {
            PartOfCurriculum poc = pocb.insertNewModule(moduleName);

            return Response.status(Response.Status.OK).entity(poc).build();
        }
        catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }
}
