package api.sources;

import api.exceptions.NoRequestBodyException;
import api.interceptors.annotations.LogApiCalls;
import pojo.ResponseError;
import beans.crud.StudyDegreeBean;
import com.kumuluz.ee.rest.beans.QueryParameters;
import entities.StudyDegree;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.logging.Logger;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("studydegrees")
@ApplicationScoped
@LogApiCalls
@Tags(value = @Tag(name = "study degrees"))
public class StudyDegreeSource {

    private final Logger log = Logger.getLogger(this.getClass().getName());

    @Context
    protected UriInfo uriInfo;

    @Inject
    private StudyDegreeBean cb;

    @Operation(description = "Returns a list of study degrees.", summary = "Get list of study degrees", responses = {
            @ApiResponse(responseCode = "200",
                    description = "List of study degrees",
                    content = @Content(
                            schema = @Schema(implementation = StudyDegree.class))
            )},
            parameters = {
                    @Parameter(name = "offset", description = "Starting point",in = ParameterIn.QUERY),
                    @Parameter(name = "limit", description = "Number of returned entities", in = ParameterIn.QUERY),
                    @Parameter(name = "order", description = "Order", in = ParameterIn.QUERY)
            })
    @GET
    public Response getStudyDegrees() {
        QueryParameters query = QueryParameters.query(uriInfo.getRequestUri().getQuery()).build();
        return Response
                .ok(cb.getStudyDegrees(query))
                .header("X-Total-Count", cb.getStudyDegrees(new QueryParameters()).size())
                .build();
    }

    @GET
    @Path("count")
    public Response getNumberOfStudyDegrees() {
        return Response.status(Response.Status.OK).entity(cb.getStudyDegrees(new QueryParameters()).size()).build();
    }

    @Operation(description = "Returns a study degree with specified id.", summary = "Get study degree by id", responses = {
            @ApiResponse(responseCode = "200",
                    description = "Study degree by id",
                    content = @Content(
                            schema = @Schema(implementation = StudyDegree.class))
            ),
            @ApiResponse(responseCode = "404",
                    description = "Study degree by id doesn't exist",
                    content = @Content(
                            schema = @Schema(implementation = ResponseError.class))
            )
    })
    @Path("{id}")
    @GET
    public Response getStudyDegree(@PathParam("id") String id) {
        return Response.ok().entity(cb.getStudyDegree(id)).build();
    }

    @Operation(description = "Inserts a new study degree.", summary = "Insert study degree", responses = {
            @ApiResponse(responseCode = "200",
                    description = "Insert successful",
                    content = @Content(
                            schema = @Schema(implementation = StudyDegree.class))),
            @ApiResponse(responseCode = "400",
                    description = "Insert failed",
                    content = @Content(
                            schema = @Schema(implementation = ResponseError.class)))
    })
    @PUT
    public Response createStudyDegree(@RequestBody StudyDegree c) {
        if(c == null) throw new NoRequestBodyException();
        if(cb.existsStudyDegree(c.getId())) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ResponseError(400, "ID already exists")).build();
        }
        c = cb.insertStudyDegree(c);
        return Response.ok().entity(c).build();
    }

    @Operation(description = "Deletes a study degree with specified id.", summary = "Delete study degree", responses = {
            @ApiResponse(responseCode = "200",
                    description = "Delete successful",
                    content = @Content(
                            schema = @Schema(implementation = StudyDegree.class))),
    })
    @Path("{id}")
    @DELETE
    public Response deleteStudyDegree(@PathParam("id") String id) {
        cb.deleteStudyDegree(id);
        return Response.ok().build();
    }

    @Operation(description = "Updates an existing study degree.", summary = "Update study degree", responses = {
            @ApiResponse(responseCode = "200",
                    description = "Update successful",
                    content = @Content(
                            schema = @Schema(implementation = StudyDegree.class))),
            @ApiResponse(responseCode = "400",
                    description = "Update failed",
                    content = @Content(
                            schema = @Schema(implementation = ResponseError.class)))
    })
    @POST
    public Response updateStudyDegree(@RequestBody StudyDegree c) {
        if(c == null) throw new NoRequestBodyException();
        c = cb.updateStudyDegree(c);
        return Response.ok().entity(c).build();
    }
}

