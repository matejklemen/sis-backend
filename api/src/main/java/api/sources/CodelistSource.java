package api.sources;

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
import java.util.logging.Logger;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("codelists")
@ApplicationScoped
public class CodelistSource {

    private Logger log = Logger.getLogger(getClass().getSimpleName());

    @Inject
    private CodelistBean clB;

    @Inject
    private CountryBean coB;

    @Inject
    private PostAddressBean paB;

    @Inject
    private CourseBean crB;

    @Inject
    private StudyProgramDegreeBean spdB;

    @Inject
    private StudyYearBean syB;

    @GET
    public Response getCodeLists() {
        List<CodelistsData> cld = new ArrayList<>();
        // TODO: this can be optimized

        // države
        cld.add(new CodelistsData("country", "Države", coB.getCountries().size(), new Country().getColumnNames(), new Country().getColumnTypes()));
        // TODO: občine

        // pošte
        cld.add(new CodelistsData("post_address", "Poštne številke", paB.getPostAdresses().size(), new PostAddress().getColumnNames(), new PostAddress().getColumnTypes()));
        // študijski program (+ študijska stopnja) in študijsko leto
        cld.add(new CodelistsData("study_program", "Študijski programi", spdB.getStudyPrograms().size(), new StudyProgram().getColumnNames(), new StudyProgram().getColumnTypes()));
        cld.add(new CodelistsData("study_degree", "Študijske stopnje", spdB.getStudyDegrees().size(), new StudyDegree().getColumnNames(), new StudyDegree().getColumnTypes()));
        cld.add(new CodelistsData("study_year", "Študijska leta", syB.getStudyYears().size(), new StudyYear().getColumnNames(), new StudyYear().getColumnTypes()));
        // TODO vrsta študija (KLASIUS SRV)

        // TODO vrsta vpisa

        // TODO način študija

        // TODO oblika študija

        // predmeti
        cld.add(new CodelistsData("course", "Predmeti", crB.getCourses().size(), new Course().getColumnNames(), new Course().getColumnTypes()));

        // TODO predavatelji

        return Response.ok(cld).build();
    }

    @Path("{name}")
    @GET
    public Response getCodeList(@PathParam("name") String name) {

        switch (name) {
            case "country":
                return Response.ok(coB.getCountries()).build();

            case "post_address":
                return Response.ok(paB.getPostAdresses()).build();

            case "study_program":
                return Response.ok(spdB.getStudyPrograms()).build();

            case "study_degree":
                return Response.ok(spdB.getStudyDegrees()).build();

            case "study_year":
                return Response.ok(syB.getStudyYears()).build();

            case "course":
                return Response.ok(crB.getCourses()).build();

            default:
                throw new NotFoundException("Codelist by this name isn't availiable.");

        }

    }

}
