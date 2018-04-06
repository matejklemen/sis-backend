package api.sources;

import api.exceptions.NoRequestBodyException;
import api.interceptors.annotations.LogApiCalls;
import api.mappers.ResponseError;
import beans.crud.PostAddressBean;
import entities.PostAddress;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("postaddresses")
@ApplicationScoped
@LogApiCalls
@Tags(value = @Tag(name = "post address"))
public class PostAddressSource {

    @Inject
    private PostAddressBean pab;

    @GET
    public Response getPostAdresses() {
        return Response.ok().entity(pab.getPostAddresses()).build();
    }

    @Path("{id}")
    @GET
    public Response getPostAddress(@PathParam("id") int id) {
        return Response.ok().entity(pab.getPostAddress(id)).build();
    }

    @PUT
    public Response createPostAddress(@RequestBody PostAddress pa) {
        if(pa == null) throw new NoRequestBodyException();
        if(pab.existsPostAddress(pa.getId())) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ResponseError(400, "ID already exists")).build();
        }
        pab.insertPostAddress(pa);
        return Response.ok().entity(pa).build();
    }

    @Path("{id}")
    @DELETE
    public Response deletePostAddress(@PathParam("id") int id) {
        pab.deletePostAddress(id);
        return Response.ok().build();
    }

    @POST
    public Response updatePostAddress(@RequestBody PostAddress pa) {
        if(pa == null) throw new NoRequestBodyException();
        pab.updatePostAddress(pa);
        return Response.ok().entity(pa).build();
    }
}

