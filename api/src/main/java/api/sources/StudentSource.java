package api.sources;

import beans.crud.StudentDataBean;
import entities.StudentData;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.logging.Logger;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("students")
@ApplicationScoped
public class StudentSource {
    private Logger log = Logger.getLogger(getClass().getSimpleName());

    @Inject
    private StudentDataBean sdB;

    /*
        Returns:
        - OK (200)
        - BAD_REQUEST (400)
        - FORBIDDEN (403)
     */
    @Path("{id}")
    @GET
    public Response getStudentById(@PathParam("id") int id) {
        StudentData sd = sdB.getStudentById(id);
        return Response.ok(sd).build();
    }

    @Path("s/{sid}")
    @GET
    public Response getStudentByStudentId(@PathParam("sid") String sid) {
        StudentData sd = sdB.getStudentByStudentId(sid);
        return Response.ok(sd).build();
    }

    @Path("search/{query}")
    @GET
    public Response getStudents(@PathParam("query") String query) {
        List sdl = sdB.searchStudents(query);
        return Response.ok(sdl).build();
    }

}
