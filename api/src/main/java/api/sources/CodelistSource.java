package api.sources;

import api.interceptors.annotations.LogApiCalls;
import beans.crud.*;
import entities.*;
import entities.address.Country;
import entities.address.Municipality;
import entities.address.PostAddress;
import entities.curriculum.Course;
import entities.curriculum.Curriculum;
import entities.curriculum.PartOfCurriculum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import pojo.CodelistsData;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
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
    @Inject private StudyTypeBean studyTypeB;
    @Inject private StudyKindBean studyKindB;
    @Inject private ProfessorBean professorB;
    @Inject private KlasiusSrvBean klasiusSrvB;
    @Inject private MunicipalityBean municipalityB;
    @Inject private StudyFormBean studyFormB;
    @Inject private CurriculumBean curriculumB;
    @Inject private PartOfCurriculumBean pocB;

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
        cld.add(new CodelistsData("municipality", "Občine", "municipalities", municipalityB.getMunicipalities().size(), new Municipality().getColumnNames(), new Municipality().getColumnTypes()));
        // pošte
        cld.add(new CodelistsData("post_address", "Poštne številke", "postaddresses", postAddressB.getPostAddresses().size(), new PostAddress().getColumnNames(), new PostAddress().getColumnTypes()));
        // študijski program (+ študijska stopnja)
        cld.add(new CodelistsData("study_program", "Študijski programi", "studyprograms", studyProgramB.getStudyPrograms().size(), new StudyProgram().getColumnNames(), new StudyProgram().getColumnTypes()));
        cld.add(new CodelistsData("study_degree", "Stopnje študija", "studydegrees", studyDegreeB.getStudyDegrees().size(), new StudyDegree().getColumnNames(), new StudyDegree().getColumnTypes()));
        // študijsko leto
        cld.add(new CodelistsData("study_year", "Študijska leta", "studyyears", studyYearB.getStudyYears().size(), new StudyYear().getColumnNames(), new StudyYear().getColumnTypes()));
        // vrsta študija (KLASIUS SRV)
        cld.add(new CodelistsData("klasius_srv", "KLASIUS SRV", "klasius", klasiusSrvB.getKlasiusSrvs().size(), new KlasiusSrv().getColumnNames(), new KlasiusSrv().getColumnTypes()));
        // vrsta vpisa
        cld.add(new CodelistsData("study_type", "Vrste študija", "studytypes", studyTypeB.getStudyTypes().size(), new StudyType().getColumnNames(), new StudyType().getColumnTypes()));
        // način študija
        cld.add(new CodelistsData("study_kind", "Načini študija", "studykinds", studyKindB.getStudyKinds().size(), new StudyKind().getColumnNames(), new StudyKind().getColumnTypes()));
        // oblika študija
        cld.add(new CodelistsData("study_form", "Oblika študija", "studyforms", studyFormB.getStudyForms().size(), new StudyForm().getColumnNames(), new StudyForm().getColumnTypes()));
        // predmeti
        cld.add(new CodelistsData("course", "Predmeti", "courses", courseB.getCourses().size(), new Course().getColumnNames(), new Course().getColumnTypes()));
        // predavatelji
        cld.add(new CodelistsData("professor", "Predavatelji", "professors", professorB.getAllProfessors().size(), new Professor().getColumnNames(), new Professor().getColumnTypes()));
        // predmetnik(-i)
        cld.add(new CodelistsData("curriculum", "Predmetnik", "curriculum", curriculumB.getEntireCurriculum().size(), new Curriculum().getColumnNames(), new Curriculum().getColumnTypes()));
        // deli predmetnikov
        cld.add(new CodelistsData("poc", "Deli predmetnikov", "poc", pocB.getAllPOC().size(), new PartOfCurriculum().getColumnNames(), new PartOfCurriculum().getColumnTypes()));

        return Response.ok(cld).build();
    }

}
