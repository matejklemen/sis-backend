package api.sources;

import api.exceptions.NoRequestBodyException;
import api.interceptors.annotations.LogApiCalls;
import api.mappers.ResponseError;
import beans.crud.StudyDegreeBean;
import entities.StudyDegree;
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
@Path("studydegrees")
@ApplicationScoped
@LogApiCalls
@Tags(value = @Tag(name = "study degree"))
public class StudyDegreeSource {

    @Inject
    private StudyDegreeBean cb;

    @GET
    public Response getCountries() {
        return Response.ok().entity(cb.getStudyDegrees()).build();
    }

    @Path("{id}")
    @GET
    public Response getStudyDegree(@PathParam("id") String id) {
        return Response.ok().entity(cb.getStudyDegree(id)).build();
    }

    @PUT
    public Response createStudyDegree(@RequestBody StudyDegree c) {
        if(c == null) throw new NoRequestBodyException();
        if(cb.existsStudyDegree(c.getId())) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ResponseError(400, "ID already exists")).build();
        }
        cb.insertStudyDegree(c);
        return Response.ok().entity(c).build();
    }

    @Path("{id}")
    @DELETE
    public Response deleteStudyDegree(@PathParam("id") String id) {
        cb.deleteStudyDegree(id);
        return Response.ok().build();
    }

    @POST
    public Response updateStudyDegree(@RequestBody StudyDegree c) {
        if(c == null) throw new NoRequestBodyException();
        cb.updateStudyDegree(c);
        return Response.ok().entity(c).build();
    }
}

