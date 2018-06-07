package api.sources;

import api.interceptors.annotations.LogApiCalls;
import beans.crud.EnrolmentBean;
import beans.crud.StudentBean;
import com.kumuluz.ee.rest.beans.QueryParameters;
import entities.Enrolment;
import entities.Student;
import entities.logic.CourseWithNumberOfEnrolledStudents;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import pojo.ResponseError;
import pojo.StudentSearchResult;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("students")
@ApplicationScoped
@LogApiCalls
@Tags(value = @Tag(name = "students"))
public class StudentSource {

    private final Logger log = Logger.getLogger(this.getClass().getName());

    @Context
    protected UriInfo uriInfo;

    @Inject
    private StudentBean sdB;
    @Inject
    private EnrolmentBean enB;

    @Operation(description = "Returns a list of students.", summary = "Get list of students", responses = {
            @ApiResponse(responseCode = "200",
                    description = "List of students",
                    content = @Content(
                            schema = @Schema(implementation = Student.class))
            )},
            parameters = {
                    @Parameter(name = "offset", description = "Starting point",in = ParameterIn.QUERY),
                    @Parameter(name = "limit", description = "Number of returned entities", in = ParameterIn.QUERY),
                    @Parameter(name = "order", description = "Order", in = ParameterIn.QUERY),
                    @Parameter(name = "filter", description = "Filter", in = ParameterIn.QUERY),
            })
    @GET
    public Response getStudents() {
        QueryParameters query = QueryParameters.query(uriInfo.getRequestUri().getQuery()).build();
        return Response.ok(sdB.getStudents(query)).build();
    }

    @Operation(description = "Returns a list of courses with number of enrolled students.", summary = "Get list of courses with number of enrolled students", responses = {
            @ApiResponse(responseCode = "200",
                    description = "List of courses with number of enrolled students",
                    content = @Content(
                            schema = @Schema(implementation = CourseWithNumberOfEnrolledStudents.class))
            )}
    )
    @GET
    @Path("countbycourses")
    public Response getNumberOfStudentsForEachCourse(@QueryParam("study_year") Integer studyYearId, @QueryParam("study_program") String studyProgramId, @QueryParam("year") Integer year) {
        if(studyYearId == null || studyProgramId == null || year == null) {
            return Response.status(400).entity(new ResponseError(400, "Manjkajo parametri study_year, stuy_program in year.")).build();
        }
        List<CourseWithNumberOfEnrolledStudents> cwnoes = sdB.getNumberOfStudentsForEachCourse(studyYearId, studyProgramId, year);
        return Response
                .ok(cwnoes)
                .build();
    }


    @GET
    @Path("count")
    public Response getNumberOfStudents() {
        return Response.status(Response.Status.OK).entity(sdB.getStudents(new QueryParameters()).size()).build();
    }

    @Operation(description = "Returns a student with specified id.", summary = "Get student by id", responses = {
            @ApiResponse(responseCode = "200",
                    description = "Student by id",
                    content = @Content(
                            schema = @Schema(implementation = Student.class))
            ),
            @ApiResponse(responseCode = "404",
                    description = "Student by id doesn't exist",
                    content = @Content(
                            schema = @Schema(implementation = ResponseError.class))
            )
    })
    @Path("{id}")
    @GET
    public Response getStudentById(@PathParam("id") int id) {
        Student sd = sdB.getStudent(id);
        return Response.ok(sd).build();
    }

    @Operation(description = "Returns a list of students that match the search query. Query can match the start of either the register number, name or surname. It's possible that the returned list is empty.", summary = "Get list of students by search query", responses = {
            @ApiResponse(responseCode = "200",
                    description = "List of students by search query",
                    content = @Content(
                            schema = @Schema(implementation
                                    = StudentSearchResult.class))
            )},
            parameters = {
                    @Parameter(name = "offset", description = "Starting point",in = ParameterIn.QUERY),
                    @Parameter(name = "limit", description = "Number of returned entities", in = ParameterIn.QUERY),
                    @Parameter(name = "order", description = "Order", in = ParameterIn.QUERY),
            })
    @Path("search/{query}")
    @GET
    public Response searchStudents(@PathParam("query") String searchQuery, @QueryParam("study_year") Integer studyYearId, @QueryParam("study_program") String studyProgramId, @QueryParam("year") Integer year) {
        QueryParameters paramQuery = QueryParameters.query(uriInfo.getRequestUri().getQuery()).build();
        List sdl = sdB.searchStudents(paramQuery, searchQuery, studyYearId, studyProgramId, year);
        return Response
                .ok(sdl)
                .header("X-Total-Count", sdB.searchStudents(new QueryParameters(), searchQuery, studyYearId, studyProgramId, year))
                .build();
    }

    @Operation(description = "Returns a list of students that are enrolled to specified course in specified study year.", summary = "Get list of students enrolled to course", responses = {
            @ApiResponse(responseCode = "200",
                    description = "List of students",
                    content = @Content(
                            schema = @Schema(implementation = Student.class))
            ),
            @ApiResponse(responseCode = "400",
                    description = "course or study_year parameter missing",
                    content = @Content(
                            schema = @Schema(implementation = ResponseError.class))
            )},
            parameters = {
                    @Parameter(name = "offset", description = "Starting point",in = ParameterIn.QUERY),
                    @Parameter(name = "limit", description = "Number of returned entities", in = ParameterIn.QUERY),
                    @Parameter(name = "order", description = "Order", in = ParameterIn.QUERY),
                    @Parameter(name = "filter", description = "Filter", in = ParameterIn.QUERY),
                    @Parameter(name = "course", required = true, description = "Course id that students are enrolled in", in = ParameterIn.QUERY),
                    @Parameter(name = "study_year", required = true, description = "Study year id that students are enrolled in", in = ParameterIn.QUERY),
                    @Parameter(name = "study_program", description = "Study program id that students are enrolled in", in = ParameterIn.QUERY),
                    @Parameter(name = "year", description = "Year that students are enrolled in", in = ParameterIn.QUERY)
            })
    @Path("enrolled")
    @GET
    public Response getStudentsByCourse(@QueryParam("course") Integer courseId, @QueryParam("study_year") Integer studyYearId, @QueryParam("study_program") String studyProgramId, @QueryParam("year") Integer year) {
        if(courseId == null || studyYearId == null) {
            return Response.status(400).entity(new ResponseError(400, "Manjkata parametra course in study_year.")).build();
        }
        QueryParameters queryParameters = QueryParameters.query(uriInfo.getRequestUri().getQuery()).build();
        List<Student> sdl = sdB.getStudentsByCourse(queryParameters, courseId, studyYearId, studyProgramId, year);
        return Response
                .ok(sdl)
                .header("X-Total-Count", sdl.size())
                .build();
    }

    @Path("currently-enrolled")
    @GET
    public Response getCurrentlyEnrolledStudents() {
        List<Enrolment> enrolments = enB.getEnrolmentsForCurrentYear();

        List<Student> students = new ArrayList<>(enrolments.size());

        for(int i = 0; i < enrolments.size(); i++)
            students.add(i, enrolments.get(i).getStudent());

        return Response.status(Response.Status.OK).entity(students).build();
    }

}
