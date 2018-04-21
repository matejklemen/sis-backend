package api.sources;

import api.interceptors.annotations.LogApiCalls;
import pojo.ResponseError;
import beans.logic.DataExporterBean;
import entities.logic.TableData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.ByteArrayInputStream;
import java.util.logging.Logger;

@Consumes(MediaType.APPLICATION_JSON)
@Path("dataexporter")
@ApplicationScoped
@LogApiCalls
@Tags(value = @Tag(name = "dataexporter"))
public class DataExporterSource {
    private final Logger log = Logger.getLogger(this.getClass().getName());

    @Inject
    private DataExporterBean dataExporterBean;

    @Operation(description = "Returns a pdf file", summary = "Generates a pdf file from table data", responses = {
            @ApiResponse(responseCode = "200",
                    description = "Pdf file stream"
            ),
            @ApiResponse(responseCode = "404",
                    description = "Failed to generate pdf",
                    content = @Content(
                            schema = @Schema(implementation = ResponseError.class))
            )
    })
    @Path("tablepdf")
    @Produces("application/pdf")
    @POST
    public Response returnTablePdf(@RequestBody TableData table) throws Exception {
        ByteArrayInputStream bios = dataExporterBean.generateTablePdf(table);
        Response.ResponseBuilder responseBuilder = Response.ok((Object) bios);
        responseBuilder.type("application/pdf");
        responseBuilder.header("Content-Disposition", "filename=test.pdf");
        return responseBuilder.build();
    }

    /*
    @Path("tablecsv")
    @Produces("text/csv")
    @POST
    public Response returnTableCsv(@RequestBody Table table) {
        return Response.ok(dataExporterBean.generateTableCsv(table)).build();
    }
    */

    @Operation(description = "Returns enrolment sheet in pdf form", summary = "Generates enrolment sheet in pdf form for student with given studenId. NOTE: studentId != registerNumber", responses = {
            @ApiResponse(responseCode = "200",
                    description = "Pdf file stream"
            ),
            @ApiResponse(responseCode = "404",
                    description = "Failed to generate pdf",
                    content = @Content(
                            schema = @Schema(implementation = ResponseError.class))
            )
    })
    @Path("enrolmentSheet/{studentId}")
    @GET
    @Produces("application/pdf")
    public Response returnEnrolmentSheet(@PathParam("studentId") int studentId){
        //get enrolment
        ByteArrayInputStream bais = dataExporterBean.generateEnrolmentSheet();

        Response.ResponseBuilder responseBuilder = Response.ok((Object) bais);
        responseBuilder.type("application/pdf");
        responseBuilder.header("Content-Disposition", "filename=vpisni-list"+12345678+".pdf");
        return responseBuilder.build();
    }

}
