package api.sources;

import beans.crud.CountryBean;
import beans.crud.CourseBean;
import beans.crud.PostAddressBean;
import beans.crud.StudyProgramBean;
import beans.logic.CodelistBean;
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
    CodelistBean clB;

    @Inject
    CountryBean coB;

    @Inject
    PostAddressBean paB;

    @Inject
    CourseBean crB;

    @Inject
    StudyProgramBean spB;

    @GET
    public Response getCodeLists() {
        List<CodelistsData> cld = new ArrayList<>();
        // TODO: this can be optimized

        // države
        cld.add(new CodelistsData("country", "Države", coB.getCountries().size()));
        // TODO: občine

        // pošte
        cld.add(new CodelistsData("post_address", "Poštne številke", paB.getPostAdresses().size()));
        // študijski program (+ študijska stopnja)
        cld.add(new CodelistsData("study_program", "Študijski program", spB.getStudyPrograms().size()));
        // TODO vrsta študija (KLASIUS SRV)

        // TODO vrsta vpisa

        // TODO način študija

        // TODO oblika študija

        // predmeti
        cld.add(new CodelistsData("course", "Predmeti", crB.getCourses().size()));

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
                return Response.ok(spB.getStudyPrograms()).build();

            case "course":
                return Response.ok(crB.getCourses()).build();

            default:
                throw new NotFoundException("Codelist by this name isn't availiable.");

        }

    }

}
