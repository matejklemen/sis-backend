package api.sources;

import api.interceptors.annotations.LogApiCalls;
import beans.logic.StudentImport;
import entities.Student;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import pojo.FileData;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("files")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
@LogApiCalls
@Tags(value = @Tag(name = "files"))
public class FileSource {

    @Inject
    private StudentImport si;

    @PUT
    public Response putFile(FileData file){
        List<Student> sp = si.ParseStudentData(file.getFileData());
        return Response.status(Response.Status.OK).entity(sp).build();
    }
}

