package api.sources;

import api.exceptions.NoRequestBodyException;
import api.interceptors.annotations.LogApiCalls;
import api.mappers.ResponseError;
import beans.crud.StudyYearBean;
import entities.StudyYear;
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
@Path("studyyears")
@ApplicationScoped
@LogApiCalls
@Tags(value = @Tag(name = "study year"))
public class StudyYearSource {

    @Inject
    private StudyYearBean cb;

    @GET
    public Response getCountries() {
        return Response.ok().entity(cb.getStudyYears()).build();
    }

    @Path("{id}")
    @GET
    public Response getStudyYear(@PathParam("id") int id) {
        return Response.ok().entity(cb.getStudyYear(id)).build();
    }

    @PUT
    public Response createStudyYear(@RequestBody StudyYear c) {
        if(c == null) throw new NoRequestBodyException();
        if(cb.existsStudyYear(c.getId())) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ResponseError(400, "ID already exists")).build();
        }
        cb.insertStudyYear(c);
        return Response.ok().entity(c).build();
    }

    @Path("{id}")
    @DELETE
    public Response deleteStudyYear(@PathParam("id") int id) {
        cb.deleteStudyYear(id);
        return Response.ok().build();
    }

    @POST
    public Response updateStudyYear(@RequestBody StudyYear c) {
        if(c == null) throw new NoRequestBodyException();
        cb.updateStudyYear(c);
        return Response.ok().entity(c).build();
    }
}

