package api.sources;

import api.interceptors.annotations.LogApiCalls;
import beans.crud.*;
import com.kumuluz.ee.rest.beans.QueryParameters;
import entities.*;
import entities.address.Country;
import entities.address.Municipality;
import entities.address.PostAddress;
import entities.curriculum.Course;
import entities.curriculum.Curriculum;
import entities.curriculum.PartOfCurriculum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import pojo.CodelistsData;
import utils.AuthUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Logger;

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
    @Inject private StudyTypeBean studyTypeB;
    @Inject private StudyKindBean studyKindB;
    @Inject private ProfessorBean professorB;
    @Inject private KlasiusSrvBean klasiusSrvB;
    @Inject private MunicipalityBean municipalityB;
    @Inject private StudyFormBean studyFormB;
    @Inject private CurriculumBean curriculumB;
    @Inject private PartOfCurriculumBean pocB;

    @Operation(description = "Returns a list of codelists.", summary = "Get list of codelists",
            responses = {
            @ApiResponse(responseCode = "200",
                    description = "List of codelists",
                    content = @Content(
                            schema = @Schema(implementation = CodelistsData.class))
            )}
    )
    @GET
    public Response getCodeLists(@Context HttpServletRequest requestContext) {
        /*
        ------ UNCOMMENT THIS AFTER TOKEN IS BEING SENT IN THE HEADER --------
        HashSet<String> allowedRoles = new HashSet<String>();
        allowedRoles.add("Administrator");

        String authHeader = requestContext.getHeader("Authorization");
        log.info(String.format("Auth header: %s", authHeader));

        if(authHeader == null || !AuthUtils.hasProperRole(authHeader, allowedRoles))
            return Response.status(Response.Status.UNAUTHORIZED).build();
        */

        List<CodelistsData> cld = new ArrayList<>();
        // TODO: this can be optimized

        // države
        cld.add(new CodelistsData("country", "Države", "countries", countryB.getCountries(new QueryParameters()).size(), new Country().getColumnNames(), new Country().getColumnTypes()));
        // TODO: občine
        cld.add(new CodelistsData("municipality", "Občine", "municipalities", municipalityB.getMunicipalities(new QueryParameters()).size(), new Municipality().getColumnNames(), new Municipality().getColumnTypes()));
        // pošte
        cld.add(new CodelistsData("post_address", "Poštne številke", "postaddresses", postAddressB.getPostAddresses(new QueryParameters()).size(), new PostAddress().getColumnNames(), new PostAddress().getColumnTypes()));
        // študijski program (+ študijska stopnja)
        cld.add(new CodelistsData("study_program", "Študijski programi", "studyprograms", studyProgramB.getStudyPrograms(new QueryParameters()).size(), new StudyProgram().getColumnNames(), new StudyProgram().getColumnTypes()));
        cld.add(new CodelistsData("study_degree", "Stopnje študija", "studydegrees", studyDegreeB.getStudyDegrees(new QueryParameters()).size(), new StudyDegree().getColumnNames(), new StudyDegree().getColumnTypes()));
        // študijsko leto
        cld.add(new CodelistsData("study_year", "Študijska leta", "studyyears", studyYearB.getStudyYears(new QueryParameters()).size(), new StudyYear().getColumnNames(), new StudyYear().getColumnTypes()));
        // vrsta študija (KLASIUS SRV)
        cld.add(new CodelistsData("klasius_srv", "KLASIUS SRV", "klasius", klasiusSrvB.getKlasiusSrvs(new QueryParameters()).size(), new KlasiusSrv().getColumnNames(), new KlasiusSrv().getColumnTypes()));
        // vrsta vpisa
        cld.add(new CodelistsData("study_type", "Vrste vpisa", "studytypes", studyTypeB.getStudyTypes(new QueryParameters()).size(), new StudyType().getColumnNames(), new StudyType().getColumnTypes()));
        // način študija
        cld.add(new CodelistsData("study_kind", "Načini študija", "studykinds", studyKindB.getStudyKinds(new QueryParameters()).size(), new StudyKind().getColumnNames(), new StudyKind().getColumnTypes()));
        // oblika študija
        cld.add(new CodelistsData("study_form", "Oblika študija", "studyforms", studyFormB.getStudyForms(new QueryParameters()).size(), new StudyForm().getColumnNames(), new StudyForm().getColumnTypes()));
        // predmeti
        cld.add(new CodelistsData("course", "Predmeti", "courses", courseB.getCourses(new QueryParameters()).size(), new Course().getColumnNames(), new Course().getColumnTypes()));
        // predavatelji
        cld.add(new CodelistsData("professor", "Predavatelji", "professors", professorB.getAllProfessors(new QueryParameters()).size(), new Professor().getColumnNames(), new Professor().getColumnTypes()));
        // predmetnik(-i)
        cld.add(new CodelistsData("curriculum", "Predmetnik", "curriculum", curriculumB.getEntireCurriculum(new QueryParameters()).size(), new Curriculum().getColumnNames(), new Curriculum().getColumnTypes()));
        // deli predmetnikov
        cld.add(new CodelistsData("poc", "Deli predmetnikov", "poc", pocB.getAllPOC(new QueryParameters()).size(), new PartOfCurriculum().getColumnNames(), new PartOfCurriculum().getColumnTypes()));

        return Response.ok(cld).build();
    }

}
