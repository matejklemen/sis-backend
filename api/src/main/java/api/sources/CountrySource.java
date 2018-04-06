package api.sources;

import api.exceptions.NoRequestBodyException;
import api.interceptors.annotations.LogApiCalls;
import api.mappers.ResponseError;
import beans.crud.CountryBean;
import entities.Country;
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
@Path("countries")
@ApplicationScoped
@LogApiCalls
@Tags(value = @Tag(name = "country"))
public class CountrySource {

    @Inject
    private CountryBean cb;

    @GET
    public Response getCountries() {
        return Response.ok().entity(cb.getCountries()).build();
    }

    @Path("{id}")
    @GET
    public Response getCountry(@PathParam("id") int id) {
        return Response.ok().entity(cb.getCountry(id)).build();
    }

    @PUT
    public Response createCountry(@RequestBody Country c) {
        if(c == null) throw new NoRequestBodyException();
        if(cb.existsCountry(c.getId())) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ResponseError(400, "ID already exists")).build();
        }
        cb.insertCountry(c);
        return Response.ok().entity(c).build();
    }

    @Path("{id}")
    @DELETE
    public Response deleteCountry(@PathParam("id") int id) {
        cb.deleteCountry(id);
        return Response.ok().build();
    }

    @POST
    public Response updateCountry(@RequestBody Country c) {
        if(c == null) throw new NoRequestBodyException();
        cb.updateCountry(c);
        return Response.ok().entity(c).build();
    }
}

