package api.sources;

import api.exceptions.NoRequestBodyException;
import api.interceptors.annotations.LogApiCalls;
import beans.crud.PartOfCurriculumBean;
import com.kumuluz.ee.rest.beans.QueryParameters;
import entities.curriculum.PartOfCurriculum;
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
@Path("poc")
@ApplicationScoped
@LogApiCalls
@Tags(value = @Tag(name = "part of curriculum"))
public class PartOfCurriculumSource {

    private final Logger log = Logger.getLogger(this.getClass().getName());

    @Context
    protected UriInfo uriInfo;

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
                            schema = @Schema(implementation = ResponseError.class)))},
            parameters = {
                    @Parameter(name = "offset", description = "Starting point",in = ParameterIn.QUERY),
                    @Parameter(name = "limit", description = "Number of returned entities", in = ParameterIn.QUERY),
                    @Parameter(name = "order", description = "Order", in = ParameterIn.QUERY),
                    @Parameter(name = "search", description = "Search", in = ParameterIn.QUERY)
            })
    @GET
    public Response getAllPOC(@QueryParam("search") String searchQuery) {
        QueryParameters query = QueryParameters.query(uriInfo.getRequestUri().getQuery()).build();
        List enl = pocb.getPOC(query, searchQuery);
        return enl == null ? Response.status(Response.Status.NOT_FOUND).entity(ResponseError.error404()).build() :
                Response.ok(enl)
                        .header("X-Total-Count", pocb.getPOC(new QueryParameters(), searchQuery).size())
                        .build();
    }

    @GET
    @Path("count")
    public Response getNumberOfPOC() {
        return Response.status(Response.Status.OK).entity(pocb.getPOC(new QueryParameters()).size()).build();
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
    @Path("mod")
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

    @Operation(description = "Inserts a new part of curriculum.", summary = "Insert part of curriculum", responses = {
            @ApiResponse(responseCode = "200",
                    description = "Insert successful",
                    content = @Content(
                            schema = @Schema(implementation = PartOfCurriculum.class))),
            @ApiResponse(responseCode = "400",
                    description = "Insert failed",
                    content = @Content(
                            schema = @Schema(implementation = ResponseError.class)))
    })
    @PUT
    public Response createPartOfCurriculum(@RequestBody PartOfCurriculum c) {
        if(c == null) throw new NoRequestBodyException();
        if(pocb.existsPartOfCurriculum(c.getId())) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ResponseError.errorIdAlreadyExists()).build();
        }
        c = pocb.insertPartOfCurriculum(c);
        return Response.ok().entity(c).build();
    }

    @Operation(description = "Deletes a part of curriculum with specified id.", summary = "Delete part of curriculum", responses = {
            @ApiResponse(responseCode = "200",
                    description = "Delete successful",
                    content = @Content(
                            schema = @Schema(implementation = PartOfCurriculum.class))),
    })
    @Path("{id}")
    @DELETE
    public Response deletePartOfCurriculum(@PathParam("id") int id) {
        pocb.deletePartOfCurriculum(id);
        return Response.ok().build();
    }

    @Operation(description = "Updates an existing part of curriculum.", summary = "Update part of curriculum", responses = {
            @ApiResponse(responseCode = "200",
                    description = "Update successful",
                    content = @Content(
                            schema = @Schema(implementation = PartOfCurriculum.class))),
            @ApiResponse(responseCode = "400",
                    description = "Update failed",
                    content = @Content(
                            schema = @Schema(implementation = ResponseError.class)))
    })
    @POST
    public Response updatePartOfCurriculum(@RequestBody PartOfCurriculum c) {
        if(c == null) throw new NoRequestBodyException();
        c = pocb.updatePartOfCurriculum(c);
        return Response.ok().entity(c).build();
    }
}
