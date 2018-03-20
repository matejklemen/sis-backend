package api.Sources;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;
import javax.ws.rs.core.*;

import com.kumuluz.ee.cors.annotations.CrossOrigin;
import pojo.File;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("file")
@CrossOrigin(allowOrigin = "http://localhost:3000")
public class FileSource {

    private Logger log = Logger.getLogger(getClass().getSimpleName());

    @PUT
    public Response putFile(File file){
        log.info("put request in file content: "+file.getFileData());
        return Response.ok().build();
    }
}
