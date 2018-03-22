package api.Sources;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;
import javax.ws.rs.core.*;

import Beans.CRUD.StudentDataBean;
import com.kumuluz.ee.cors.annotations.CrossOrigin;
import pojo.File;
import Beans.Logic.StudentImport;

@Path("files")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
@CrossOrigin(allowOrigin = "http://localhost:3000", supportedMethods = "GET, POST, HEAD, OPTIONS, DELETE, PUT")
public class FileSource {

    private Logger log = Logger.getLogger(getClass().getSimpleName());

    @Inject
    private StudentImport si;

    @PUT
    public Response putFile(File file){
        try {
            log.info("put request: "+si.toString());
            si.ParseStudentData(file.getFileData());
            return Response.status(Response.Status.OK).build();
        }
        catch (Exception ex){
            log.info("Error in putFile method. Message: "+ex.toString());
            ex.printStackTrace();
            return Response.serverError().build();
        }
    }
}
