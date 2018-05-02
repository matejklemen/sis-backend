package api.sources;

import api.exceptions.NoRequestBodyException;
import api.interceptors.annotations.LogApiCalls;
import beans.crud.PostAddressBean;
import com.kumuluz.ee.rest.beans.QueryParameters;
import entities.address.PostAddress;
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
@Path("postaddresses")
@ApplicationScoped
@LogApiCalls
@Tags(value = @Tag(name = "post addresses"))
public class PostAddressSource {

    private final Logger log = Logger.getLogger(this.getClass().getName());

    @Context
    protected UriInfo uriInfo;

    @Inject
    private PostAddressBean pab;

    @Operation(description = "Returns a list of post addresses.", summary = "Get list of post addresses", responses = {
            @ApiResponse(responseCode = "200",
                    description = "List of post addresses",
                    content = @Content(
                            schema = @Schema(implementation
                                    = PostAddress.class))
            )},
            parameters = {
                    @Parameter(name = "offset", description = "Starting point",in = ParameterIn.QUERY),
                    @Parameter(name = "limit", description = "Number of returned entities", in = ParameterIn.QUERY),
                    @Parameter(name = "order", description = "Order", in = ParameterIn.QUERY),
                    @Parameter(name = "search", description = "Search", in = ParameterIn.QUERY)
            })
    @GET
    public Response getPostAddresses(@QueryParam("search") String searchQuery) {
        QueryParameters query = QueryParameters.query(uriInfo.getRequestUri().getQuery()).build();
        List enl = pab.getPostAddresses(query, searchQuery);
        return Response
                .ok(enl)
                .header("X-Total-Count", pab.getPostAddresses(new QueryParameters(), searchQuery).size())
                .build();
    }

    @GET
    @Path("count")
    public Response getNumberOfPostAdresses() {
        return Response.status(Response.Status.OK).entity(pab.getPostAddresses(new QueryParameters()).size()).build();
    }

    @Operation(description = "Returns a post address with specified id.", summary = "Get post address by id", responses = {
            @ApiResponse(responseCode = "200",
                    description = "Post address by id",
                    content = @Content(
                            schema = @Schema(implementation = PostAddress.class))
            ),
            @ApiResponse(responseCode = "404",
                    description = "Post address by id doesn't exist",
                    content = @Content(
                            schema = @Schema(implementation = ResponseError.class))
            )
    })
    @Path("{id}")
    @GET
    public Response getPostAddress(@PathParam("id") int id) {
        return Response.ok().entity(pab.getPostAddress(id)).build();
    }

    @Operation(description = "Inserts a new post address.", summary = "Insert post address", responses = {
            @ApiResponse(responseCode = "200",
                    description = "Insert successful",
                    content = @Content(
                            schema = @Schema(implementation = PostAddress.class))),
            @ApiResponse(responseCode = "400",
                    description = "Insert failed",
                    content = @Content(
                            schema = @Schema(implementation = ResponseError.class)))
    })
    @PUT
    public Response createPostAddress(@RequestBody PostAddress pa) {
        if(pa == null) throw new NoRequestBodyException();
        if(pab.existsPostAddress(pa.getId())) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ResponseError.errorIdAlreadyExists()).build();
        }
        pa = pab.insertPostAddress(pa);
        return Response.ok().entity(pa).build();
    }

    @Operation(description = "Deletes a post address with specified id.", summary = "Delete post address", responses = {
            @ApiResponse(responseCode = "200",
                    description = "Delete successful",
                    content = @Content(
                            schema = @Schema(implementation = PostAddress.class))),
    })
    @Path("{id}")
    @DELETE
    public Response deletePostAddress(@PathParam("id") int id) {
        pab.deletePostAddress(id);
        return Response.ok().build();
    }

    @Operation(description = "Updates an existing post address.", summary = "Update post address", responses = {
            @ApiResponse(responseCode = "200",
                    description = "Update successful",
                    content = @Content(
                            schema = @Schema(implementation = PostAddress.class))),
            @ApiResponse(responseCode = "400",
                    description = "Update failed",
                    content = @Content(
                            schema = @Schema(implementation = ResponseError.class)))
    })
    @POST
    public Response updatePostAddress(@RequestBody PostAddress pa) {
        if(pa == null) throw new NoRequestBodyException();
        pa = pab.updatePostAddress(pa);
        return Response.ok().entity(pa).build();
    }
}

