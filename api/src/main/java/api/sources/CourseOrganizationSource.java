package api.sources;

import api.exceptions.NoRequestBodyException;
import api.interceptors.annotations.LogApiCalls;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import pojo.ResponseError;
import beans.crud.CourseOrganizationBean;
import com.kumuluz.ee.rest.beans.QueryParameters;
import entities.curriculum.CourseOrganization;
import entities.curriculum.Curriculum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.logging.Logger;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("course-organization")
@ApplicationScoped
@LogApiCalls
@Tags(value = @Tag(name = "course organization"))
public class CourseOrganizationSource {

    private final Logger log = Logger.getLogger(this.getClass().getName());

    @Context
    protected UriInfo uriInfo;

    @Inject
    CourseOrganizationBean cob;

    @GET
    public Response getCourseOrganizations() {
        QueryParameters query = QueryParameters.query(uriInfo.getRequestUri().getQuery()).build();
        // clone the query, but remove the 'offset' and 'limit' part, so we get the actual count
        // instead of the count without the already seen values
        QueryParameters defaultQuery = new QueryParameters();

        QueryParameters q2 = QueryParameters.query(uriInfo.getRequestUri().getQuery()).build();
        q2.setOffset(defaultQuery.getLimit());
        q2.setLimit(defaultQuery.getOffset());

        return Response.status(Response.Status.OK)
                .entity(cob.getCourseOrganizations(q2))
                .build();
    }

    @GET
    @Path("count")
    public Response getNumberOfCourseOrganizations() {
        return Response.status(Response.Status.OK).entity(cob.getCourseOrganizations(new QueryParameters()).size()).build();
    }

    @Operation(description = "Retrieves course organizations for particular course ID.", summary = "Get curriculum by course ID.", responses = {
            @ApiResponse(responseCode = "200",
                    description = "Retrieved course organizations",
                    content = @Content(
                            schema = @Schema(implementation = Curriculum.class))),
            @ApiResponse(responseCode = "404",
                    description = "No course organization found for specified study program",
                    content = @Content(
                            schema = @Schema(implementation = ResponseError.class)
                    ))
    })
    @GET
    @Path("/course/{courseId}")
    public Response getCourseOrganizationByCourseId(@PathParam(value = "courseId") @Parameter(description = "Official course ID") int courseId) {
        List<CourseOrganization> courseOrganizations = cob.getCourseOrganizationsByCourseId(courseId);

        return courseOrganizations == null ? Response.status(Response.Status.NOT_FOUND).entity(ResponseError.error404()).build():
                Response.status(Response.Status.OK).entity(courseOrganizations).build();
    }

    @Operation(description = "Retrieves course organization for particular professor ID.", summary = "Get course organization by professor ID.", responses = {
            @ApiResponse(responseCode = "200",
                    description = "Retrieved course organizations",
                    content = @Content(
                            schema = @Schema(implementation = CourseOrganization.class))),
            @ApiResponse(responseCode = "404",
                    description = "No course organizations found for specified study program",
                    content = @Content(
                            schema = @Schema(implementation = ResponseError.class)
                    ))
    })
    @GET
    @Path("/professor/{professorId}")
    public Response getCourseOrganizationByProfessorId(@PathParam(value = "professorId") @Parameter(description = "Autogenerated professor ID, see \"GET /api/professor\" for help.") int professorId) {
        List<CourseOrganization> courseOrganizations = cob.getCourseOrganizationByProfessorId(professorId);

        return courseOrganizations == null ? Response.status(Response.Status.NOT_FOUND).entity(ResponseError.error404()).build():
                Response.status(Response.Status.OK).entity(courseOrganizations).build();
    }

    @Operation(description = "Retrieves course organizations for particular study year.", summary = "Get course organizations for study year.", responses = {
            @ApiResponse(responseCode = "200",
                    description = "Retrieved course organizations",
                    content = @Content(
                            schema = @Schema(implementation = CourseOrganization.class))),
            @ApiResponse(responseCode = "400",
                    description = "The year is written incorrectly. Make sure it is in format XXXXXXXX (8 numbers), for example 2016/2017 would be written as 20162017.",
                    content = @Content(
                            schema = @Schema(implementation = ResponseError.class)
                    )),
            @ApiResponse(responseCode = "404",
                    description = "No course organizations found for specified parameter",
                    content = @Content(
                            schema = @Schema(implementation = ResponseError.class)
                    ))
    })
    @GET
    @Path("{studyYear}")
    public Response getCourseOrganizationByStudyYear(@PathParam(value = "studyYear") @Parameter(description = "Study year (for example 2017/2018) written without the dash (\"/\"), e.g. \"20172018\"") String studyYear) {
        if(studyYear.length() != 8)
            return Response.status(Response.Status.BAD_REQUEST).build();

        studyYear = String.format("%s/%s", studyYear.substring(0, 4), studyYear.substring(4, 8));

        List<CourseOrganization> courseOrganizations = cob.getCourseOrganizationByStudyYear(studyYear);

        return courseOrganizations == null ? Response.status(Response.Status.NOT_FOUND).entity(ResponseError.error404()).build():
                Response.status(Response.Status.OK).entity(courseOrganizations).build();
    }

    @Operation(description = "Updates an existing course organization.", summary = "Update course organization", responses = {
            @ApiResponse(responseCode = "200",
                    description = "Update successful",
                    content = @Content(
                            schema = @Schema(implementation = CourseOrganization.class))),
            @ApiResponse(responseCode = "400",
                    description = "Update failed",
                    content = @Content(
                            schema = @Schema(implementation = ResponseError.class)))
    })
    @POST
    public Response updateCountry(@RequestBody CourseOrganization co) {
        if(co == null) throw new NoRequestBodyException();
        co = cob.updateCourseOrganization(co);
        return Response.ok().entity(co).build();
    }
}
