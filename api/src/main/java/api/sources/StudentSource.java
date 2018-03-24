package api.sources;

import beans.crud.StudentBean;
import entities.Student;

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
    private StudentBean sdB;

    /*
        Returns:
        - OK (200)
        - BAD_REQUEST (400)
        - FORBIDDEN (403)
     */

    @GET
    public Response getStudents() {
        return Response.ok(sdB.getStudents()).build();
    }

    @Path("{id}")
    @GET
    public Response getStudentById(@PathParam("id") int id) {
        Student sd = sdB.getStudentById(id);
        return Response.ok(sd).build();
    }

    @Path("s/{regno}")
    @GET
    public Response getStudentByStudentId(@PathParam("regno") String regno) {
        Student sd = sdB.getStudentByRegisterNumber(regno);
        return Response.ok(sd).build();
    }

    @Path("search")
    @GET
    public Response getStudentsWithEmptySearch() {
        return getStudents();
    }

    @Path("search/{query}")
    @GET
    public Response getStudents(@PathParam("query") String query) {
        List sdl = sdB.searchStudents(query);
        return Response.ok(sdl).build();
    }

}
