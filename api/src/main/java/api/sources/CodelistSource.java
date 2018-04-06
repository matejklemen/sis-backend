package api.sources;

import api.interceptors.annotations.LogApiCalls;
import api.mappers.ResponseError;
import beans.crud.*;
import entities.*;
import entities.curriculum.Course;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import pojo.CodelistsData;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("codelists")
@ApplicationScoped
@LogApiCalls
@Tags(value = @Tag(name = "codelists"))
public class CodelistSource {

    @Inject private CountryBean countryB;
    @Inject private PostAddressBean postAddressB;
    @Inject private CourseBean courseB;
    @Inject private StudyProgramBean studyProgramB;
    @Inject private StudyDegreeBean studyDegreeB;
    @Inject private StudyYearBean studyYearB;

    @Operation(description = "Returns a list of codelists.", summary = "Get list of codelists", responses = {
            @ApiResponse(responseCode = "200",
                    description = "List of codelists",
                    content = @Content(
                            schema = @Schema(implementation = CodelistsData.class))
            )
    })
    @GET
    public Response getCodeLists() {
        List<CodelistsData> cld = new ArrayList<>();
        // TODO: this can be optimized

        // države
        cld.add(new CodelistsData("country", "Države", "countries", countryB.getCountries().size(), new Country().getColumnNames(), new Country().getColumnTypes()));
        // TODO: občine

        // pošte
        cld.add(new CodelistsData("post_address", "Poštne številke", "postadresses", postAddressB.getPostAddresses().size(), new PostAddress().getColumnNames(), new PostAddress().getColumnTypes()));
        // študijski program (+ študijska stopnja) in študijsko leto
        cld.add(new CodelistsData("study_program", "Študijski programi", "studyprograms", studyProgramB.getStudyPrograms().size(), new StudyProgram().getColumnNames(), new StudyProgram().getColumnTypes()));
        cld.add(new CodelistsData("study_degree", "Študijske stopnje", "studydegrees", studyDegreeB.getStudyDegrees().size(), new StudyDegree().getColumnNames(), new StudyDegree().getColumnTypes()));
        cld.add(new CodelistsData("study_year", "Študijska leta", "studyyears", studyYearB.getStudyYears().size(), new StudyYear().getColumnNames(), new StudyYear().getColumnTypes()));
        // TODO vrsta študija (KLASIUS SRV)

        // TODO vrsta vpisa

        // TODO način študija

        // TODO oblika študija

        // predmeti
        cld.add(new CodelistsData("course", "Predmeti", "courses", courseB.getCourses().size(), new Course().getColumnNames(), new Course().getColumnTypes()));

        // TODO predavatelji

        return Response.ok(cld).build();
    }

    @Operation(description = "Returns a codelist with specified name - a list of entries. This is the same as calling the codelist's own endpoint (i.e. calling '/codelists/country' is the same as calling '/countries'). Usage of this endpoint is discouraged, use entities' own endpoints instead.", summary = "Get codelist entries by codelist name", responses = {
            @ApiResponse(responseCode = "200",
                    description = "List of entries in the codelist"
            ),
            @ApiResponse(responseCode = "404",
                    description = "Codelist by name doesn't exist or is unavailiable",
                    content = @Content(
                            schema = @Schema(implementation = ResponseError.class))
            )
    })
    @Path("{name}")
    @GET
    public Response getCodeList(@PathParam("name") String name) {

        switch (name) {
            case "country":
                return Response.ok(countryB.getCountries()).build();

            case "post_address":
                return Response.ok(postAddressB.getPostAddresses()).build();

            case "study_program":
                return Response.ok(studyProgramB.getStudyPrograms()).build();

            case "study_degree":
                return Response.ok(studyDegreeB.getStudyDegrees()).build();

            case "study_year":
                return Response.ok(studyYearB.getStudyYears()).build();

            case "course":
                return Response.ok(courseB.getCourses()).build();

            default:
                throw new NotFoundException("Codelist by this name isn't availiable.");

        }

    }

}
