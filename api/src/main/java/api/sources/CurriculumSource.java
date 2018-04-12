package api.sources;

import api.exceptions.NoRequestBodyException;
import api.interceptors.annotations.LogApiCalls;
import api.mappers.ResponseError;
import beans.crud.CurriculumBean;
import entities.curriculum.Curriculum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
import java.util.LinkedList;
import java.util.List;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("curriculum")
@ApplicationScoped
@LogApiCalls
@Tags(value = @Tag(name = "curriculum"))
public class CurriculumSource {

    @Inject
    CurriculumBean cb;

    @Operation(description = "Retrieves entire curriculum.", summary = "Get entire curriculum", responses = {
            @ApiResponse(responseCode = "200",
                    description = "Retrieved data",
                    content = @Content(
                            schema = @Schema(implementation = Curriculum.class)))
    })
    @GET
    public Response getEntireCurriculum(@QueryParam("deleted") boolean deleted) {
        if(deleted)
            return Response.status(Response.Status.OK).entity(cb.getDeletedEntireCurriculum()).build();
        return Response.status(Response.Status.OK).entity(cb.getEntireCurriculum()).build();
    }

    @Operation(description = "Retrieves curriculum by curriculum ID.", summary = "Get curriculum for curriculum ID", responses = {
            @ApiResponse(responseCode = "200",
                    description = "Retrieved curriculum",
                    content = @Content(
                            schema = @Schema(implementation = Curriculum.class))),
            @ApiResponse(responseCode = "404",
                    description = "No curriculum found for specified curriculum ID",
                    content = @Content(
                            schema = @Schema(implementation = ResponseError.class)
                    ))
    })
    @Path("{id-curriculum}")
    @GET
    public Response getCurriculumByCurriculumId(@PathParam(value = "id-curriculum") int idCurriculum) {
        Curriculum c = cb.getCurriculumByIdCurriculum(idCurriculum);

        return c == null ? Response.status(Response.Status.NOT_FOUND).build():
                Response.status(Response.Status.OK).entity(c).build();
    }

    @Operation(description = "Retrieves curriculum for particular study program.", summary = "Get curriculum for specified study program", responses = {
            @ApiResponse(responseCode = "200",
                    description = "Retrieved curriculum",
                    content = @Content(
                            schema = @Schema(implementation = Curriculum.class))),
            @ApiResponse(responseCode = "404",
                        description = "No curriculum found for specified study program",
                        content = @Content(
                                schema = @Schema(implementation = ResponseError.class)
                        ))
    })
    @Path("studyprogram/{study-program-id}")
    @GET
    public Response getCurriculumByStudyProgramId(@PathParam(value = "study-program-id") String studyProgramId) {
        List<Curriculum> c = cb.getCurriculumByStudyProgramId(studyProgramId);

        return c == null ? Response.status(Response.Status.NOT_FOUND).build():
                Response.status(Response.Status.OK).entity(c).build();
    }

    @Operation(description = "Retrieves curriculum for particular study program for particular study year and particular grade.", summary = "Get curriculum for specified study program, year and grade", responses = {
            @ApiResponse(responseCode = "200",
                    description = "Retrieved curriculum",
                    content = @Content(
                            schema = @Schema(implementation = Curriculum.class))),
            @ApiResponse(responseCode = "400",
                    description = "The year is written incorrectly. Make sure it is in format XXXXXXXX (8 numbers), for example 2016/2017 would be written as 20162017.",
                    content = @Content(
                            schema = @Schema(implementation = ResponseError.class)
                    )),
            @ApiResponse(responseCode = "404",
                    description = "No curriculum found for specified parameters",
                    content = @Content(
                            schema = @Schema(implementation = ResponseError.class)
                    ))
    })
    @Path("{study-year}/{study-program-id}/{year-of-program}")
    @GET
    public Response getAvailableCurriculumForProgramAndYear(@PathParam(value = "study-year") @Parameter(required = true, description = "Study year (for example 2017/2018) written without the dash (\"/\"), e.g. \"20172018\"") String studyYear,
                                                   @PathParam(value = "study-program-id") String studyProgramId,
                                                   @PathParam(value = "year-of-program") @Parameter(description = "Grade that the student is in") int yearOfProgram) {
        if (studyYear.length() != 8)
            return Response.status(Response.Status.BAD_REQUEST).build();

        // input is in format XXXXXXXX -> convert into XXXX/XXXX
        studyYear = String.format("%s/%s", studyYear.substring(0, 4), studyYear.substring(4, 8));

        List<Curriculum> cModule = cb.getModuleCourses(studyYear, studyProgramId, yearOfProgram);
        List<Curriculum> cMandatory = cb.getMandatoryCourses(studyYear, studyProgramId, yearOfProgram);
        List<Curriculum> cSpecialEle = cb.getSpecialistElectiveCourses(studyYear, studyProgramId, yearOfProgram);
        List<Curriculum> cGeneralEle = cb.getGeneralElectiveCourses(studyYear, studyProgramId, yearOfProgram);

        List<Curriculum> c = new LinkedList<Curriculum>();
        c.addAll(cMandatory);
        c.addAll(cModule);
        c.addAll(cSpecialEle);
        c.addAll(cGeneralEle);

        return c == null ? Response.status(Response.Status.NOT_FOUND).build():
                Response.status(Response.Status.OK).entity(c).build();
    }

    @Operation(description = "Inserts a new curriculum.", summary = "Insert curriculum", responses = {
            @ApiResponse(responseCode = "200",
                    description = "Insert successful",
                    content = @Content(
                            schema = @Schema(implementation = Curriculum.class))),
            @ApiResponse(responseCode = "400",
                    description = "Insert failed",
                    content = @Content(
                            schema = @Schema(implementation = ResponseError.class)))
    })
    @PUT
    public Response createCurriculum(@RequestBody Curriculum c) {
        if(c == null) throw new NoRequestBodyException();
        if(cb.existsCurriculum(c.getId())) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ResponseError(400, "ID already exists")).build();
        }
        c = cb.insertCurriculum(c);
        return Response.ok().entity(c).build();
    }

    @Operation(description = "Deletes a curriculum with specified id.", summary = "Delete curriculum", responses = {
            @ApiResponse(responseCode = "200",
                    description = "Delete successful",
                    content = @Content(
                            schema = @Schema(implementation = Curriculum.class))),
    })
    @Path("{id}")
    @DELETE
    public Response deleteCurriculum(@PathParam("id") int id) {
        cb.deleteCurriculum(id);
        return Response.ok().build();
    }

    @Operation(description = "Updates an existing curriculum.", summary = "Update curriculum", responses = {
            @ApiResponse(responseCode = "200",
                    description = "Update successful",
                    content = @Content(
                            schema = @Schema(implementation = Curriculum.class))),
            @ApiResponse(responseCode = "400",
                    description = "Update failed",
                    content = @Content(
                            schema = @Schema(implementation = ResponseError.class)))
    })
    @POST
    public Response updateCurriculum(@RequestBody Curriculum c) {
        if(c == null) throw new NoRequestBodyException();
        c = cb.updateCurriculum(c);
        return Response.ok().entity(c).build();
    }

}
