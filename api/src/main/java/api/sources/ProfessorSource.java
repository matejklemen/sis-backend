package api.sources;

import api.interceptors.annotations.LogApiCalls;
import api.mappers.ResponseError;
import beans.crud.ProfessorBean;
import entities.Professor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("professor")
@ApplicationScoped
@LogApiCalls
@Tags(value = @Tag(name = "professor"))
public class ProfessorSource {

    @Inject
    ProfessorBean pb;

    @Operation(description = "Returns a list of professors.", summary = "Get list of professors.", responses = {
            @ApiResponse(responseCode = "200",
                    description = "List of professors",
                    content = @Content(
                            schema = @Schema(implementation = Professor.class))
            ),
            @ApiResponse(responseCode = "404",
                    description = "No professors found",
                    content = @Content(
                            schema = @Schema(implementation = ResponseError.class)
                    ))
    })
    @GET
    public Response getAllProfessors() {
        List<Professor> professors = pb.getAllProfessors();

        return professors == null ? Response.status(Response.Status.NOT_FOUND).build():
                Response.status(Response.Status.OK).entity(professors).build();
    }

    @Operation(description = "Insert a new professor entity.", summary = "Add new proffessor.", responses = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully added professor",
                    content = @Content(
                            schema = @Schema(implementation = Professor.class))
            ),
            @ApiResponse(responseCode = "400",
                    description = "First name or last name missing",
                    content = @Content(
                            schema = @Schema(implementation = ResponseError.class)
                    ))
    })
    @PUT
    public Response addProfessor(Professor p) {
        if(p.getFirstName() == null || p.getLastName1() == null)
            return Response.status(Response.Status.BAD_REQUEST).build();

        Professor profEntity = pb.addProfessor(p);

        return Response.status(Response.Status.OK).entity(profEntity).build();
    }
}
