package api.sources;

import beans.logic.StudentImport;
import pojo.FileData;
import pojo.StudentProfile;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.logging.Logger;

@Path("files")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class FileSource {

    private Logger log = Logger.getLogger(getClass().getSimpleName());

    @Inject
    private StudentImport si;

    @PUT
    public Response putFile(FileData file){
        try {
            List<StudentProfile> sp = si.ParseStudentData(file.getFileData());
            return Response.status(Response.Status.OK).entity(sp).build();
        }
        catch (Exception ex){
            log.info("Error in putFile method. Message: "+ex.toString());
            ex.printStackTrace();
            return Response.serverError().build();
        }
    }
}

