package api.sources;

import api.exceptions.NoRequestBodyException;
import api.interceptors.annotations.LogApiCalls;
import pojo.ResponseError;
import beans.crud.ProfessorBean;
import com.kumuluz.ee.rest.beans.QueryParameters;
import entities.Professor;
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
@Path("professors")
@ApplicationScoped
@LogApiCalls
@Tags(value = @Tag(name = "professor"))
public class ProfessorSource {

    private final Logger log = Logger.getLogger(this.getClass().getName());

    @Context
    protected UriInfo uriInfo;

    @Inject
    ProfessorBean pB;

    @Operation(description = "Returns a list of professors.", summary = "Get list of professors", responses = {
            @ApiResponse(responseCode = "200",
                    description = "List of professors",
                    content = @Content(
                            schema = @Schema(implementation = Professor.class))
            )},
            parameters = {
                    @Parameter(name = "offset", description = "Starting point",in = ParameterIn.QUERY),
                    @Parameter(name = "limit", description = "Number of returned entities", in = ParameterIn.QUERY),
                    @Parameter(name = "order", description = "Order", in = ParameterIn.QUERY)
            })
    @GET
    public Response getProfessors() {
        QueryParameters query = QueryParameters.query(uriInfo.getRequestUri().getQuery()).build();
        return Response
                .ok(pB.getAllProfessors(query))
                .header("X-Total-Count", pB.getAllProfessors(new QueryParameters()).size())
                .build();
    }

    @GET
    @Path("count")
    public Response getNumberOfProfessors() {
        return Response.status(Response.Status.OK).entity(pB.getAllProfessors(new QueryParameters()).size()).build();
    }

    @Operation(description = "Returns a professor with specified id.", summary = "Get professor by id", responses = {
            @ApiResponse(responseCode = "200",
                    description = "Professor by id",
                    content = @Content(
                            schema = @Schema(implementation = Professor.class))
            ),
            @ApiResponse(responseCode = "404",
                    description = "Professor by id doesn't exist",
                    content = @Content(
                            schema = @Schema(implementation = ResponseError.class))
            )
    })
    @Path("{id}")
    @GET
    public Response getProfessorById(@PathParam("id") int id) {
        return Response.ok(pB.getProfessor(id)).build();
    }

    @Operation(description = "Inserts a new professor.", summary = "Insert professor", responses = {
            @ApiResponse(responseCode = "200",
                    description = "Insert successful",
                    content = @Content(
                            schema = @Schema(implementation = Professor.class))),
            @ApiResponse(responseCode = "400",
                    description = "Insert failed",
                    content = @Content(
                            schema = @Schema(implementation = ResponseError.class)))
    })
    @PUT
    public Response createProfessor(@RequestBody Professor c) {
        if(c == null) throw new NoRequestBodyException();
        if(pB.existsProfessor(c.getId())) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ResponseError(400, "ID already exists")).build();
        }
        c = pB.insertProfessor(c);
        return Response.ok().entity(c).build();
    }

    @Operation(description = "Deletes a professor with specified id.", summary = "Delete professor", responses = {
            @ApiResponse(responseCode = "200",
                    description = "Delete successful",
                    content = @Content(
                            schema = @Schema(implementation = Professor.class))),
    })
    @Path("{id}")
    @DELETE
    public Response deleteProfessor(@PathParam("id") int id) {
        pB.deleteProfessor(id);
        return Response.ok().build();
    }

    @Operation(description = "Updates an existing professor.", summary = "Update professor", responses = {
            @ApiResponse(responseCode = "200",
                    description = "Update successful",
                    content = @Content(
                            schema = @Schema(implementation = Professor.class))),
            @ApiResponse(responseCode = "400",
                    description = "Update failed",
                    content = @Content(
                            schema = @Schema(implementation = ResponseError.class)))
    })
    @POST
    public Response updateProfessor(@RequestBody Professor c) {
        if(c == null) throw new NoRequestBodyException();
        c = pB.updateProfessor(c);
        return Response.ok().entity(c).build();
    }
    
}
