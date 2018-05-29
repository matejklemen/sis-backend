package api.sources;

import api.interceptors.annotations.LogApiCalls;
import beans.crud.*;
import beans.logic.CourseExamTermValidationBean;
import com.kumuluz.ee.rest.beans.QueryParameters;
import entities.Enrolment;
import entities.curriculum.CourseExamTerm;
import entities.curriculum.CourseOrganization;
import entities.curriculum.ExamSignUp;
import entities.curriculum.StudentCourses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import pojo.ResponseError;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.NoResultException;
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
@Path("course-exam-term")
@ApplicationScoped
@LogApiCalls
@Tags(value = @Tag(name = "course exam term"))
public class CourseExamTermSource {
    private final Logger log = Logger.getLogger(this.getClass().getName());

    @Context
    protected UriInfo uriInfo;

    @Inject private CourseExamTermValidationBean cetvb;
    @Inject private CourseExamTermBean cetb;
    @Inject private EnrolmentBean eb;
    @Inject private StudentCoursesBean scb;
    @Inject private CourseOrganizationBean cob;
    @Inject private ExamSignUpBean esub;
    @Inject private StudyYearBean syb;

    @Operation(description = "Returns a list of all exam terms.", summary = "Get list of exam terms",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "List of exam terms",
                            content = @Content(
                                    schema = @Schema(implementation = CourseExamTerm.class))
                    )},
            parameters = {
                    @Parameter(name = "offset", description = "Starting point",in = ParameterIn.QUERY),
                    @Parameter(name = "limit", description = "Number of returned entities", in = ParameterIn.QUERY),
                    @Parameter(name = "order", description = "Order", in = ParameterIn.QUERY),
                    @Parameter(name = "filter", description = "Filter the returned results, more info at https://github.com/kumuluz/kumuluzee-rest#examples", in = ParameterIn.QUERY)
            })
    @GET
    public Response getAllExamTerms() {
        QueryParameters query = QueryParameters.query(uriInfo.getRequestUri().getQuery()).build();

        // clone the query, but remove the 'offset' and 'limit' part, so we get the actual count
        // instead of the count without the already seen values
        QueryParameters defaultQuery = new QueryParameters();

        QueryParameters q2 = QueryParameters.query(uriInfo.getRequestUri().getQuery()).build();
        q2.setLimit(defaultQuery.getLimit());
        q2.setOffset(defaultQuery.getOffset());
        return Response
                .ok(cetb.getAllExamTerms(query))
                .header("X-Total-Count", cetb.getAllExamTerms(q2).size())
                .build();
    }

    @Operation(description = "Returns a list of all exam terms for given student (for last enrolment).", summary = "Get list of exam terms for student",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "List of exam terms",
                            content = @Content(
                                    schema = @Schema(implementation = CourseExamTerm.class))
                    ),
                    @ApiResponse(responseCode = "404",
                            description = "No student with this id.",
                            content = @Content(
                                    schema = @Schema(implementation = ResponseError.class)
                            ))
            })
    @GET
    @Path("student/{studentId}")
    public Response getAllExamTermsForStudent(@PathParam("studentId") int studentId) {

        // First we get last enrolment for student
        Enrolment e = eb.getLastEnrolmentByStudentId(studentId);

        // For each course we get exam term
        List<CourseExamTerm> lcet = new ArrayList<>();

        if(!e.isConfirmed())
            return Response.ok().entity(lcet).build();

        // Then we get all courses student is enrolled in
        List<StudentCourses> lsc = scb.getStudentCoursesByEnrolmentId(e.getId());

        for ( StudentCourses sc : lsc) {
            CourseOrganization co = cob.getCourseOrganizationsByCourseIdAndYear(sc.getCourse().getId(), syb.getCurrentStudyYear(0).getId());

            if(co == null)
                continue;

            List<CourseExamTerm> cet = cetb.getExamTermsByCourse(co.getId());

            if(cet == null)
                continue;

            List<CourseExamTerm> innerlcet = cetb.getExamTermsByCourse(co.getId());

            for (CourseExamTerm c : innerlcet){
                // Setting StudentCourses id as FE needs it
                c.setStudentCoursesId(sc.getIdStudentCourses());

                // Setting isSignUp flag as FE needs it
                c.setSignedUp(esub.checkIfAlreadySignedUpAndNotReturned(c.getId(), sc.getIdStudentCourses()));

                if(c.getSignedUp()) {
                    // Setting confirmed flag as FE needs it
                    c.setConfirmed(esub.getExamSignUp(c.getId(), sc.getIdStudentCourses()).isConfirmed());
                } else {
                    c.setConfirmed(false);
                }

                // Setting previous attempts [attempts this year, total attempts] for course, FE needs it
                c.setPreviousAttempts(
                        esub.getNumberOfExamTakingsInLatestEnrolment(c.getStudentCoursesId()),
                        esub.getNumberOfExamTakingsInAllEnrolments(studentId, c.getCourseOrganization().getCourse().getId())
                );
            }

            lcet.addAll(innerlcet);
        }

        return Response.ok().entity(lcet).build();
    }

    @Operation(description = "Returns an exam term object for specified ID of exam term.", summary = "Get exam term by ID",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Exam term object for specified ID",
                            content = @Content(
                                    schema = @Schema(implementation = CourseExamTerm.class))
                    ),
                    @ApiResponse(responseCode = "404",
                            description = "Exam term with this ID not found",
                            content = @Content(
                                    schema = @Schema(implementation = ResponseError.class)
                            ))
            })
    @GET
    @Path("{id}")
    public Response getExamTermByIdExamTerm(@PathParam("id") @Parameter(name = "id", description = "ID of wanted exam term object", in = ParameterIn.PATH)
                                                        int id) {
        CourseExamTerm cet = cetb.getExamTermById(id);

        return cet == null? Response.status(Response.Status.NOT_FOUND).entity(ResponseError.error404()).build():
                Response.status(Response.Status.OK).entity(cet).build();
    }

    @Operation(description = "Insert a new exam term object.", summary = "Insert exam term",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Exam term successfully inserted"
                    ),
                    @ApiResponse(responseCode = "400",
                            description = "Inserting new exam term failed",
                            content = @Content(
                                    schema = @Schema(implementation = ResponseError.class)
                            ))
            })
    @PUT
    public Response insertExamTerm(@Parameter(description = "New course exam term object") CourseExamTerm cet) {
        List<String> errors = cetvb.validateExamTermData(cet);

        if(errors.isEmpty()) {
            CourseExamTerm newCet = cetb.insertExamTerm(cet);
            return newCet == null ? Response.status(Response.Status.BAD_REQUEST).build():
                    Response.status(Response.Status.OK).entity(newCet).build();
        }
        else
            return Response.status(Response.Status.BAD_REQUEST).entity(
                    new ResponseError(400, errors.toArray(new String[0]))).build();
    }

    @Operation(description = "Update exam term object with same ID as the one provided.", summary = "Update exam term",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Updated exam term object",
                            content = @Content(
                                    schema = @Schema(implementation = CourseExamTerm.class))
                    ),
                    @ApiResponse(responseCode = "400",
                            description = "Updating exam term failed",
                            content = @Content(
                                    schema = @Schema(implementation = ResponseError.class)
                            ))
            })
    @POST
    public Response updateExamTerm(@Parameter(description = "Course exam term with updated fields") CourseExamTerm updatedCet) {
        List <String> errors = cetvb.validateExamTermData(updatedCet);

        if(errors.isEmpty()) {
            CourseExamTerm newCet = cetb.updateExamTerm(updatedCet);
            return newCet == null ? Response.status(Response.Status.BAD_REQUEST).build():
                    Response.status(Response.Status.OK).entity(newCet).build();
        }
        else
            return Response.status(Response.Status.BAD_REQUEST).entity(
                    new ResponseError(400, errors.toArray(new String[0]))).build();
    }

    @Operation(description = "Delete exam term object with specified ID.", summary = "Delete exam term",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Deleted exam term object"
                    ),
                    @ApiResponse(responseCode = "400",
                            description = "Deleting exam term failed",
                            content = @Content(
                                    schema = @Schema(implementation = ResponseError.class)
                            )
                    )
            })
    @DELETE
    @Path("{id}")
    public Response deleteExamTerm(@PathParam("id") @Parameter(description = "ID of exam term to delete", in = ParameterIn.PATH)
                                               int idExamTerm) {
        try {
            List<ExamSignUp> signups = esub.getExamSignUpsByExamTerm(idExamTerm);
            cetb.deleteExamTerm(idExamTerm);

            // when deleting an exam term, also delete the sign ups from students
            if(signups != null && !signups.isEmpty())
                esub.removeExamSignUps(signups);

            return Response.status(Response.Status.OK).build();
        }
        catch(NoResultException nre) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }
}
