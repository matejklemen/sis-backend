package api.sources;

import api.exceptions.NoRequestBodyException;
import api.interceptors.annotations.LogApiCalls;
import api.mappers.ResponseError;
import beans.crud.KlasiusSrvBean;
import com.kumuluz.ee.rest.beans.QueryParameters;
import entities.KlasiusSrv;
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
@Path("klasius")
@ApplicationScoped
@LogApiCalls
@Tags(value = @Tag(name = "KLASIUS SRV"))
public class KlasiusSrvSource {

    private final Logger log = Logger.getLogger(this.getClass().getName());

    @Context
    protected UriInfo uriInfo;

    @Inject
    private KlasiusSrvBean cB;

    @Operation(description = "Returns a list of KLASIUS SRVs.", summary = "Get list of KLASIUS SRVs", responses = {
            @ApiResponse(responseCode = "200",
                    description = "List of KLASIUS SRVs",
                    content = @Content(
                            schema = @Schema(implementation = KlasiusSrv.class))
            )},
            parameters = {
                    @Parameter(name = "offset", description = "Starting point",in = ParameterIn.QUERY),
                    @Parameter(name = "limit", description = "Number of returned entities", in = ParameterIn.QUERY),
                    @Parameter(name = "order", description = "Order", in = ParameterIn.QUERY)
            })
    @GET
    public Response getKlasiusSrvs() {
        QueryParameters query = QueryParameters.query(uriInfo.getRequestUri().getQuery()).build();
        return Response
                .ok(cB.getKlasiusSrvs(query))
                .header("X-Total-Count", cB.getKlasiusSrvs(new QueryParameters()).size())
                .build();
    }

    @GET
    @Path("count")
    public Response getNumberOfKlasiusSrvs() {
        QueryParameters query = QueryParameters.query(uriInfo.getRequestUri().getQuery()).build();

        return Response.status(Response.Status.OK).entity(cB.getKlasiusSrvs(query).size()).build();
    }

    @Operation(description = "Returns a KLASIUS SRV with specified id.", summary = "Get KLASIUS SRV by id", responses = {
            @ApiResponse(responseCode = "200",
                    description = "KLASIUS SRV by id",
                    content = @Content(
                            schema = @Schema(implementation = KlasiusSrv.class))
            ),
            @ApiResponse(responseCode = "404",
                    description = "KLASIUS SRV by id doesn't exist",
                    content = @Content(
                            schema = @Schema(implementation = ResponseError.class))
            )
    })
    @Path("{id}")
    @GET
    public Response getKlasiusSrvById(@PathParam("id") int id) {
        return Response.ok(cB.getKlasiusSrv(id)).build();
    }

    @Operation(description = "Inserts a new KLASIUS SRV.", summary = "Insert KLASIUS SRV", responses = {
            @ApiResponse(responseCode = "200",
                    description = "Insert successful",
                    content = @Content(
                            schema = @Schema(implementation = KlasiusSrv.class))),
            @ApiResponse(responseCode = "400",
                    description = "Insert failed",
                    content = @Content(
                            schema = @Schema(implementation = ResponseError.class)))
    })
    @PUT
    public Response createKlasiusSrv(@RequestBody KlasiusSrv c) {
        if(c == null) throw new NoRequestBodyException();
        if(cB.existsKlasiusSrv(c.getId())) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ResponseError(400, "ID already exists")).build();
        }
        c = cB.insertKlasiusSrv(c);
        return Response.ok().entity(c).build();
    }

    @Operation(description = "Deletes a KLASIUS SRV with specified id.", summary = "Delete KLASIUS SRV", responses = {
            @ApiResponse(responseCode = "200",
                    description = "Delete successful",
                    content = @Content(
                            schema = @Schema(implementation = KlasiusSrv.class))),
    })
    @Path("{id}")
    @DELETE
    public Response deleteKlasiusSrv(@PathParam("id") int id) {
        cB.deleteKlasiusSrv(id);
        return Response.ok().build();
    }

    @Operation(description = "Updates an existing KLASIUS SRV.", summary = "Update KLASIUS SRV", responses = {
            @ApiResponse(responseCode = "200",
                    description = "Update successful",
                    content = @Content(
                            schema = @Schema(implementation = KlasiusSrv.class))),
            @ApiResponse(responseCode = "400",
                    description = "Update failed",
                    content = @Content(
                            schema = @Schema(implementation = ResponseError.class)))
    })
    @POST
    public Response updateKlasiusSrv(@RequestBody KlasiusSrv c) {
        if(c == null) throw new NoRequestBodyException();
        c = cB.updateKlasiusSrv(c);
        return Response.ok().entity(c).build();
    }

}
