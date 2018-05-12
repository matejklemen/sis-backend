package api.sources;

import api.interceptors.annotations.LogApiCalls;
import beans.logic.ArtificialDataBean;
import entities.EnrolmentToken;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
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

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("generate")
@ApplicationScoped
@LogApiCalls
@Tags(value = @Tag(name = "artificial data generation"))
public class ArtificialDataSource {

    @Inject private ArtificialDataBean adb;

    @Operation(description = "Data generation for existing student", summary = "Generate student courses, enrolments, " +
            "enrolment tokens, exam sign ups for pre-existing student",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Generation sucessful"
                    )},
            parameters = {
                @Parameter(name = "toyearofprogram", in = ParameterIn.QUERY, description = "Specifies up to " +
                        "which year of program to generate student data. For example, specifying toyearofprogram = 3 " +
                        "will generate data for 1st year, 2nd year and 3rd year."),
                @Parameter(name = "studyyear", in = ParameterIn.QUERY, description = "ID of study year"),
                @Parameter(name = "studyprogram", in = ParameterIn.QUERY, description = "ID of study program"),
                @Parameter(name = "student", in = ParameterIn.QUERY, description = "ID of student (needs to exist " +
                        "before call)")
            }
    )
    @Path("student")
    @PUT
    public Response generateStudentData(@QueryParam("toyearofprogram") int toYearOfProgram,
                                        @QueryParam("studyyear") int idStudyYear,
                                        @QueryParam("studyprogram") String idStudyProgram,
                                        @QueryParam("student") int idStudent) {

        adb.caller(idStudent, idStudyProgram, idStudyYear, toYearOfProgram);

        return Response.status(Response.Status.OK).build();
    }

}
