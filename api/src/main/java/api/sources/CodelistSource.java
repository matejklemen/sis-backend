package api.sources;

import api.interceptors.annotations.LogApiCalls;
import beans.crud.*;
import beans.logic.CodelistBean;
import entities.*;
import entities.curriculum.Course;
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
public class CodelistSource {

    @Inject private CodelistBean codelistB;
    @Inject private CountryBean countryB;
    @Inject private PostAddressBean postAddressB;
    @Inject private CourseBean courseB;
    @Inject private StudyProgramBean studyProgramB;
    @Inject private StudyDegreeBean studyDegreeB;
    @Inject private StudyYearBean studyYearB;

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
