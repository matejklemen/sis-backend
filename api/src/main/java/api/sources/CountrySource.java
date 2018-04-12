package api.sources;

import api.exceptions.NoRequestBodyException;
import api.interceptors.annotations.LogApiCalls;
import api.mappers.ResponseError;
import beans.crud.CountryBean;
import entities.address.Country;
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
@Path("countries")
@ApplicationScoped
@LogApiCalls
@Tags(value = @Tag(name = "countries"))
public class CountrySource {

    @Inject
    private CountryBean cb;

    @Operation(description = "Returns a list of countries.", summary = "Get list of countries", responses = {
            @ApiResponse(responseCode = "200",
                    description = "List of countries",
                    content = @Content(
                            schema = @Schema(implementation
                                    = Country.class))
            )
    })
    @GET
    public Response getCountries(@QueryParam("deleted") boolean deleted) {
        if(deleted)
            return Response.ok().entity(cb.getDeletedCountries()).build();
        return Response.ok().entity(cb.getCountries()).build();
    }

    @Operation(description = "Returns a country with specified id.", summary = "Get country by id", responses = {
            @ApiResponse(responseCode = "200",
                    description = "Country by id",
                    content = @Content(
                            schema = @Schema(implementation = Country.class))
            ),
            @ApiResponse(responseCode = "404",
                    description = "Country by id doesn't exist",
                    content = @Content(
                            schema = @Schema(implementation = ResponseError.class))
            )
    })
    @Path("{id}")
    @GET
    public Response getCountry(@PathParam("id") int id) {
        return Response.ok().entity(cb.getCountry(id)).build();
    }

    @Operation(description = "Inserts a new country.", summary = "Insert country", responses = {
            @ApiResponse(responseCode = "200",
                    description = "Insert successful",
                    content = @Content(
                            schema = @Schema(implementation = Country.class))),
            @ApiResponse(responseCode = "400",
                    description = "Insert failed",
                    content = @Content(
                            schema = @Schema(implementation = ResponseError.class)))
    })
    @PUT
    public Response createCountry(@RequestBody Country c) {
        if(c == null) throw new NoRequestBodyException();
        if(cb.existsCountry(c.getId())) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ResponseError(400, "ID already exists")).build();
        }
        c = cb.insertCountry(c);
        return Response.ok().entity(c).build();
    }

    @Operation(description = "Deletes a country with specified id.", summary = "Delete country", responses = {
            @ApiResponse(responseCode = "200",
                    description = "Delete successful",
                    content = @Content(
                            schema = @Schema(implementation = Country.class))),
    })
    @Path("{id}")
    @DELETE
    public Response deleteCountry(@PathParam("id") int id) {
        cb.deleteCountry(id);
        return Response.ok().build();
    }

    @Operation(description = "Updates an existing country.", summary = "Update country", responses = {
            @ApiResponse(responseCode = "200",
                    description = "Update successful",
                    content = @Content(
                            schema = @Schema(implementation = Country.class))),
            @ApiResponse(responseCode = "400",
                    description = "Update failed",
                    content = @Content(
                            schema = @Schema(implementation = ResponseError.class)))
    })
    @POST
    public Response updateCountry(@RequestBody Country c) {
        if(c == null) throw new NoRequestBodyException();
        c = cb.updateCountry(c);
        return Response.ok().entity(c).build();
    }
}

