package api.sources;

import beans.crud.EnrolmentBean;
import beans.crud.StudentBean;
import entities.Enrolment;
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
@Path("enrolments")
@ApplicationScoped
public class EnrolmentSource {
    private Logger log = Logger.getLogger(getClass().getSimpleName());

    @Inject
    private EnrolmentBean enB;

    /*
        Returns:
        - OK (200)
        - BAD_REQUEST (400)
        - FORBIDDEN (403)
     */
    @Path("{id}")
    @GET
    public Response getEnrolmentById(@PathParam("id") int id) {
        Enrolment en = enB.getEnrolmentById(id);
        return Response.ok(en).build();
    }

}
