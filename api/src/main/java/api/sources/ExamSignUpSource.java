package api.sources;

import api.interceptors.annotations.LogApiCalls;
import beans.crud.ExamSignUpBean;
import beans.logic.ExamSignUpLogicBean;
import entities.curriculum.ExamSignUp;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("exam-sign-up")
@ApplicationScoped
@LogApiCalls
@Tags(value = @Tag(name = "exam signup"))
public class ExamSignUpSource {

    @Inject
    ExamSignUpBean esb;
    @Inject
    ExamSignUpLogicBean esulb;

    @Path("{courseId}/{registrationNumber}")
    @GET
    public Response getExamSignUpsForExamTerm(@PathParam("registrationNumber") String registrationNumber, @PathParam("courseId") int courseId) {

        List<ExamSignUp> signups = esb.getExamSignUpsOnCourseForStudent(courseId, registrationNumber);

        return signups == null ? Response.status(Response.Status.NOT_FOUND).build():
                Response.status(Response.Status.OK).entity(signups).build();
    }

    @PUT
    public Response addExamSignUp(@QueryParam("studentId") Integer studentId, @QueryParam("studentCoursesId") Integer studentCoursesId, @QueryParam("courseExamTermId") Integer courseExamTermId) {
        ExamSignUp esu = esulb.addExamSignUp(studentId, studentCoursesId, courseExamTermId);

        return esu == null ? Response.status(Response.Status.NOT_FOUND).build():
                Response.status(Response.Status.OK).entity(esu).build();
    }

}
