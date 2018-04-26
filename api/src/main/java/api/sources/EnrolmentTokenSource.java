package api.sources;

import api.interceptors.annotations.LogApiCalls;
import beans.crud.*;
import beans.logic.EnrolmentPolicyBean;
import entities.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import pojo.ResponseError;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("tokens")
@ApplicationScoped
@LogApiCalls
@Tags(value = {@Tag(name = "enrolments"), @Tag(name = "tokens")})
public class EnrolmentTokenSource {

    @Inject
    private EnrolmentTokenBean etb;

    @Inject
    private EnrolmentBean eb;

    @Inject
    private EnrolmentPolicyBean epb;

    @Inject
    private StudentBean sb;
    @Inject private StudyYearBean syb;
    @Inject private StudyProgramBean spb;
    @Inject private StudyFormBean sfb;
    @Inject private StudyKindBean skb;
    @Inject private StudyTypeBean stb;
    @Inject private KlasiusSrvBean ksb;


    @Operation(description = "Returns newly created enrolment token for given id", summary = "Create enrolment token by student id", responses = {
            @ApiResponse(responseCode = "200",
                    description = "Created token for id",
                    content = @Content(
                            schema = @Schema(implementation
                                    = EnrolmentToken.class))
            ),
            @ApiResponse(responseCode = "404",
                    description = "No student with this id",
                    content = @Content(
                            schema = @Schema(implementation
                                    = ResponseError.class))
            )
    })
    @Path("{id}")
    @PUT
    public Response putToken(@PathParam("id") int id) {
        Enrolment e = eb.getLastEnrolmentByStudentId(id);
        EnrolmentToken et = EnrolmentToken.createEnrolmentToken(e);

        et.setFreeChoice(epb.hasStudentFreeChoiceOfCurriculum(et.getStudent()));

        et = etb.putEnrolmentToken(et);

        return Response.ok().entity(et).build();
    }

    @Operation(description = "Returns newly created enrolment token for given id and first year student", summary = "Create enrolment token by student id", responses = {
            @ApiResponse(responseCode = "200",
                    description = "Created token for id",
                    content = @Content(
                            schema = @Schema(implementation
                                    = EnrolmentToken.class))
            ),
            @ApiResponse(responseCode = "404",
                    description = "No student with this id",
                    content = @Content(
                            schema = @Schema(implementation
                                    = ResponseError.class))
            )
    })
    @Path("first/{id}")
    @PUT
    public Response putTokenForFirstTime(@PathParam("id") int id) {

        Student s = sb.getStudent(id);
        StudyType st = stb.getStudyType(1);
        StudyKind sk = skb.getStudyKind(1);
        StudyForm sf = sfb.getStudyForm(1);
        StudyYear sy = syb.getStudyYear(5);

        EnrolmentToken et = EnrolmentToken.createFirstEnrolmentToken(s,sk,st,sy,sf);

        et.setFreeChoice(false);

        // try to infer the default KLASIUS SRV
        StudyProgram sp = s.getStudyProgram();
        Integer idDefaultKlasius = 16204;

        if(sp != null)
            idDefaultKlasius = sp.getId().equals("1000470") ? 16203: 16204;
        else {
            // study program is not selected so set default value (= BUN RI)
            et.setStudyProgram(spb.getStudyProgram("1000468"));
        }

        et.setKlasiusSrv(ksb.getKlasiusSrv(idDefaultKlasius));
        et = etb.putEnrolmentToken(et);

        return Response.ok().entity(et).build();
    }

    @Operation(description = "Returns enrolment token for student id, if it exists", summary = "Get enrolment token by student id",
            parameters = {
                    @Parameter(name = "studentId", description = "Student id", in = ParameterIn.QUERY, schema = @Schema(type = "int")),
            },
            responses = {
            @ApiResponse(responseCode = "200",
                    description = "Enrolment token for student id",
                    content = @Content(
                            schema = @Schema(implementation
                                    = EnrolmentToken.class))
            ),
            @ApiResponse(responseCode = "404",
                    description = "No enrolment token with this student id",
                    content = @Content(
                            schema = @Schema(implementation
                                    = ResponseError.class))
            )
    })
    @GET
    public Response getToken(@QueryParam("studentId") int studentId){
        EnrolmentToken et = etb.getLastEnrolmentTokenByStudentId(studentId);
        return Response.ok().entity(et).build();
    }

    @Operation(description = "Deletes a token with specified id", summary = "Delete enrolment token",
            responses = {
                    @ApiResponse(responseCode = "204",
                            description = "Delete successful"
                    ),
                    @ApiResponse(responseCode = "403",
                            description = "Delete is forbidden because token was used",
                            content = @Content(
                                    schema = @Schema(implementation
                                            = ResponseError.class))
                    )
            })
    @Path("{id}")
    @DELETE
    public Response removeToken(@PathParam("id") int id){
        if(!etb.getEnrolmentTokenById(id).isUsed()){
            etb.deleteEnrolmentToken(id);
            return Response.noContent().build();
        }
        else{
            return Response.status(403).build(); //Forbidden
        }
    }

    @Operation(description = "Updates an existing enrolment token.", summary = "Update enrolment token",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Update successful",
                            content = @Content(
                                    schema = @Schema(implementation
                                            = EnrolmentToken.class))
                    ),
                    @ApiResponse(responseCode = "404",
                            description = "Update failed",
                            content = @Content(
                                    schema = @Schema(implementation
                                            = ResponseError.class))
                    ),
                    @ApiResponse(responseCode = "403",
                            description = "Post is forbidden because token was used",
                            content = @Content(
                                    schema = @Schema(implementation
                                            = ResponseError.class))
                    )
            })
    @POST
    public Response postToken(@RequestBody EnrolmentToken token){
        if(!etb.getEnrolmentTokenById(token.getId()).isUsed()) {
            // update KLASIUS SRV
            StudyProgram sp = token.getStudyProgram();
            Integer idDefaultKlasius = 16204;

            if(sp != null)
                idDefaultKlasius = sp.getId().equals("1000470") ? 16203: 16204;

            token.setKlasiusSrv(ksb.getKlasiusSrv(idDefaultKlasius));

            token.setStudent(etb.getEnrolmentTokenById(token.getId()).getStudent());
            EnrolmentToken et = etb.updateEnrolmentToken(token);
            return Response.ok().entity(et).build();
        }
        else{
            return Response.status(403).build(); //Forbidden
        }
    }
}

