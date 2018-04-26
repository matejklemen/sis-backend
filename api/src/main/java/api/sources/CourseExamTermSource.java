package api.sources;

import api.interceptors.annotations.LogApiCalls;
import beans.crud.CourseExamTermBean;
import beans.logic.CourseExamTermValidationBean;
import entities.curriculum.CourseExamTerm;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import pojo.ResponseError;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
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

    @Inject
    private CourseExamTermValidationBean cetvb;
    @Inject
    private CourseExamTermBean cetb;

    @PUT
    public Response insertExamTerm(CourseExamTerm cet) {
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

    @GET
    @Path("{id}")
    public Response getExamTermByIdExamTerm(@PathParam("id") int id) {
        CourseExamTerm cet = cetb.getExamTermById(id);

        return cet == null? Response.status(Response.Status.NOT_FOUND).build():
                Response.status(Response.Status.OK).entity(cet).build();
    }

}
