package api.sources;

import api.exceptions.NoRequestBodyException;
import api.interceptors.annotations.LogApiCalls;
import api.mappers.ResponseError;
import beans.crud.StudyProgramBean;
import entities.StudyProgram;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("studyprograms")
@ApplicationScoped
@LogApiCalls
@Tags(value = @Tag(name = "study program"))
public class StudyProgramSource {

    @Inject
    private StudyProgramBean cb;

    @GET
    public Response getCountries() {
        return Response.ok().entity(cb.getStudyPrograms()).build();
    }

    @Path("{id}")
    @GET
    public Response getStudyProgram(@PathParam("id") String id) {
        return Response.ok().entity(cb.getStudyProgram(id)).build();
    }

    @PUT
    public Response createStudyProgram(@RequestBody StudyProgram c) {
        if(c == null) throw new NoRequestBodyException();
        if(cb.existsStudyProgram(c.getId())) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ResponseError(400, "ID already exists")).build();
        }
        cb.insertStudyProgram(c);
        return Response.ok().entity(c).build();
    }

    @Path("{id}")
    @DELETE
    public Response deleteStudyProgram(@PathParam("id") String id) {
        cb.deleteStudyProgram(id);
        return Response.ok().build();
    }

    @POST
    public Response updateStudyProgram(@RequestBody StudyProgram c) {
        if(c == null) throw new NoRequestBodyException();
        cb.updateStudyProgram(c);
        return Response.ok().entity(c).build();
    }
}

