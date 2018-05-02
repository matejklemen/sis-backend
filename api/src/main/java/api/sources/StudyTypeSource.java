package api.sources;

import api.exceptions.NoRequestBodyException;
import api.interceptors.annotations.LogApiCalls;
import beans.crud.StudyTypeBean;
import com.kumuluz.ee.rest.beans.QueryParameters;
import entities.StudyType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import pojo.ResponseError;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.logging.Logger;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("studytypes")
@ApplicationScoped
@LogApiCalls
@Tags(value = @Tag(name = "study types"))
public class StudyTypeSource {

    private final Logger log = Logger.getLogger(this.getClass().getName());

    @Context
    protected UriInfo uriInfo;

    @Inject
    private StudyTypeBean cB;

    @Operation(description = "Returns a list of study types.", summary = "Get list of study types", responses = {
            @ApiResponse(responseCode = "200",
                    description = "List of study types",
                    content = @Content(
                            schema = @Schema(implementation = StudyType.class))
            )},
            parameters = {
                    @Parameter(name = "offset", description = "Starting point",in = ParameterIn.QUERY),
                    @Parameter(name = "limit", description = "Number of returned entities", in = ParameterIn.QUERY),
                    @Parameter(name = "order", description = "Order", in = ParameterIn.QUERY),
                    @Parameter(name = "search", description = "Search", in = ParameterIn.QUERY)
            })
    @GET
    public Response getStudyTypes(@QueryParam("search") String searchQuery) {
        QueryParameters query = QueryParameters.query(uriInfo.getRequestUri().getQuery()).build();
        List enl = cB.getStudyTypes(query, searchQuery);
        return Response
                .ok(enl)
                .header("X-Total-Count", cB.getStudyTypes(new QueryParameters(), searchQuery).size())
                .build();
    }

    @GET
    @Path("count")
    public Response getNumberOfStudyTypes() {
        return Response.status(Response.Status.OK).entity(cB.getStudyTypes(new QueryParameters()).size()).build();
    }

    @Operation(description = "Returns a study type with specified id.", summary = "Get study type by id", responses = {
            @ApiResponse(responseCode = "200",
                    description = "Study type by id",
                    content = @Content(
                            schema = @Schema(implementation = StudyType.class))
            ),
            @ApiResponse(responseCode = "404",
                    description = "Study type by id doesn't exist",
                    content = @Content(
                            schema = @Schema(implementation = ResponseError.class))
            )
    })
    @Path("{id}")
    @GET
    public Response getStudyTypeById(@PathParam("id") int id) {
        return Response.ok(cB.getStudyType(id)).build();
    }

    @Operation(description = "Inserts a new study type.", summary = "Insert study type", responses = {
            @ApiResponse(responseCode = "200",
                    description = "Insert successful",
                    content = @Content(
                            schema = @Schema(implementation = StudyType.class))),
            @ApiResponse(responseCode = "400",
                    description = "Insert failed",
                    content = @Content(
                            schema = @Schema(implementation = ResponseError.class)))
    })
    @PUT
    public Response createStudyType(@RequestBody StudyType c) {
        if(c == null) throw new NoRequestBodyException();
        if(cB.existsStudyType(c.getId())) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ResponseError.errorIdAlreadyExists()).build();
        }
        c = cB.insertStudyType(c);
        return Response.ok().entity(c).build();
    }

    @Operation(description = "Deletes a study type with specified id.", summary = "Delete study type", responses = {
            @ApiResponse(responseCode = "200",
                    description = "Delete successful",
                    content = @Content(
                            schema = @Schema(implementation = StudyType.class))),
    })
    @Path("{id}")
    @DELETE
    public Response deleteStudyType(@PathParam("id") int id) {
        cB.deleteStudyType(id);
        return Response.ok().build();
    }

    @Operation(description = "Updates an existing study type.", summary = "Update study type", responses = {
            @ApiResponse(responseCode = "200",
                    description = "Update successful",
                    content = @Content(
                            schema = @Schema(implementation = StudyType.class))),
            @ApiResponse(responseCode = "400",
                    description = "Update failed",
                    content = @Content(
                            schema = @Schema(implementation = ResponseError.class)))
    })
    @POST
    public Response updateStudyType(@RequestBody StudyType c) {
        if(c == null) throw new NoRequestBodyException();
        c = cB.updateStudyType(c);
        return Response.ok().entity(c).build();
    }

}
