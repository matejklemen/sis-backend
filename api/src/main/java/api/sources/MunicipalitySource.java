package api.sources;

import api.exceptions.NoRequestBodyException;
import api.interceptors.annotations.LogApiCalls;
import api.mappers.ResponseError;
import beans.crud.MunicipalityBean;
import entities.address.Municipality;
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
@Path("municipalities")
@ApplicationScoped
@LogApiCalls
@Tags(value = @Tag(name = "municipalities"))
public class MunicipalitySource {

    @Inject
    private MunicipalityBean pab;

    @Operation(description = "Returns a list of municipalities.", summary = "Get list of municipalities", responses = {
            @ApiResponse(responseCode = "200",
                    description = "List of municipalities",
                    content = @Content(
                            schema = @Schema(implementation
                                    = Municipality.class))
            )
    })
    @GET
    public Response getMunicipalityes() {
        return Response.ok().entity(pab.getMunicipalities()).build();
    }

    @Operation(description = "Returns a municipality with specified id.", summary = "Get municipality by id", responses = {
            @ApiResponse(responseCode = "200",
                    description = "Municipality by id",
                    content = @Content(
                            schema = @Schema(implementation = Municipality.class))
            ),
            @ApiResponse(responseCode = "404",
                    description = "Municipality by id doesn't exist",
                    content = @Content(
                            schema = @Schema(implementation = ResponseError.class))
            )
    })
    @Path("{id}")
    @GET
    public Response getMunicipality(@PathParam("id") int id) {
        return Response.ok().entity(pab.getMunicipality(id)).build();
    }

    @Operation(description = "Inserts a new municipality.", summary = "Insert municipality", responses = {
            @ApiResponse(responseCode = "200",
                    description = "Insert successful",
                    content = @Content(
                            schema = @Schema(implementation = Municipality.class))),
            @ApiResponse(responseCode = "400",
                    description = "Insert failed",
                    content = @Content(
                            schema = @Schema(implementation = ResponseError.class)))
    })
    @PUT
    public Response createMunicipality(@RequestBody Municipality pa) {
        if(pa == null) throw new NoRequestBodyException();
        if(pab.existsMunicipality(pa.getId())) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ResponseError(400, "ID already exists")).build();
        }
        pa = pab.insertMunicipality(pa);
        return Response.ok().entity(pa).build();
    }

    @Operation(description = "Deletes a municipality with specified id.", summary = "Delete municipality", responses = {
            @ApiResponse(responseCode = "200",
                    description = "Delete successful",
                    content = @Content(
                            schema = @Schema(implementation = Municipality.class))),
    })
    @Path("{id}")
    @DELETE
    public Response deleteMunicipality(@PathParam("id") int id) {
        pab.deleteMunicipality(id);
        return Response.ok().build();
    }

    @Operation(description = "Updates an existing municipality.", summary = "Update municipality", responses = {
            @ApiResponse(responseCode = "200",
                    description = "Update successful",
                    content = @Content(
                            schema = @Schema(implementation = Municipality.class))),
            @ApiResponse(responseCode = "400",
                    description = "Update failed",
                    content = @Content(
                            schema = @Schema(implementation = ResponseError.class)))
    })
    @POST
    public Response updateMunicipality(@RequestBody Municipality pa) {
        if(pa == null) throw new NoRequestBodyException();
        pa = pab.updateMunicipality(pa);
        return Response.ok().entity(pa).build();
    }
}

