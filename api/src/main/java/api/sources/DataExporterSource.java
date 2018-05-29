package api.sources;

import api.interceptors.annotations.LogApiCalls;
import beans.crud.EnrolmentConfirmationRequestBean;
import beans.logic.DataExporterBean;
import com.kumuluz.ee.rest.beans.QueryParameters;
import entities.logic.TableData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import pojo.ResponseError;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

@Consumes(MediaType.APPLICATION_JSON)
@Path("dataexporter")
@ApplicationScoped
@LogApiCalls
@Tags(value = @Tag(name = "dataexporter"))
@ApiResponse(
        responseCode = "404",
        description = "Failed to generate requested export file.",
        content = @Content(schema = @Schema(implementation = ResponseError.class))
)
@ApiResponse(
        responseCode = "200",
        description = "Requested file byte stream.",
        headers = {
                @Header(name = "Content-Disposition", description = "Header that suggest browser to download file."),
                @Header(name = "X-Export-Filename", description = "Suggested filename generated based on request.")
        }
)
@Produces("application/pdf")
public class DataExporterSource {
    private final Logger log = Logger.getLogger(this.getClass().getName());

    // Note: "Content-Disposition" header "pove" browserju, da naj brskalnik naredi download vsebine. Zal v nasem primeru
    //       ne dostopamo direktno do fajlov (z urljem) ampk se prejmejo na klienta z AJAXom (HTTP) in se sele na to
    //       prikazejo... dodal sem header "X-Export-Filename", ki vsebuje predlagano ime datoteke.
    @Inject
    private DataExporterBean dataExporterBean;
    @Inject
    private EnrolmentConfirmationRequestBean ecrB;
    @Context
    protected UriInfo uriInfo;

    @Operation(description = "Generates a PDF file from table data", summary = "Generate PDF")
    @Path("tablepdf")
    @POST
    public Response returnTablePdf(@RequestBody TableData table) {
        ByteArrayInputStream bios = dataExporterBean.generateTablePdf(table);
        String filename = String.format("izvoz_%s.pdf", new SimpleDateFormat("yyyyddMM_HHmmss").format(new Date()));
        return Response
                .ok(bios)
                .type("application/pdf")
                .header("Content-Disposition", "attachment; filename=" + filename)
                .header("X-Export-Filename", filename)
                .build();
    }

    @Operation(description = "Generates a CSV file from table data", summary = "Generate CSV")
    @Path("tablecsv")
    @Produces("text/csv")
    @POST
    public Response returnTableCsv(@RequestBody TableData table) {
        ByteArrayInputStream bios = dataExporterBean.generateTableCsv(table);
        String filename = String.format("izvoz_%s.csv", new SimpleDateFormat("yyyyddMM_HHmmss").format(new Date()));
        return Response
                .ok(bios)
                .type("text/csv")
                .header("Content-Disposition", "attachment; filename=" + filename)
                .header("X-Export-Filename", filename)
                .build();
    }

    @Operation(description = "Generates enrolment sheet in PDF form for student with given studenId. NOTE: studentId != registerNumber", summary = "Generate PDF enrolment sheet")
    @Path("enrolmentsheet/{studentId}")
    @GET
    public Response returnEnrolmentSheet(@PathParam("studentId") int studentId) {
        //get enrolment
        ByteArrayInputStream bais = dataExporterBean.generateEnrolmentSheet(studentId);
        String filename = String.format("vpisnilist_%d.pdf", studentId);
        return Response
                .ok(bais)
                .type("application/pdf")
                .header("Content-Disposition", "attachment; filename=" + filename)
                .header("X-Export-Filename", filename)
                .build();
    }

    @Operation(description = "Generates enrolment confirmation in PDF form for student with given studenId. NOTE: studentId != registerNumber", summary = "Generate PDF enrolment confirmation")
    @Path("enrolmentconfirmation/{studentId}")
    @GET
    public Response returnEnrolmentConfirmation(@PathParam("studentId") int studentId) {
        ByteArrayInputStream bais = dataExporterBean.generateEnrolmentConfirmation(studentId);
        String filename = String.format("potrdilo_%d.pdf", studentId);
        return Response
                .ok(bais)
                .type("application/pdf")
                .header("Content-Disposition", "attachment; filename=" + filename)
                .header("X-Export-Filename", filename)
                .build();
    }

    @Operation(description = "Generates index in PDF form for student with given studenId. NOTE: studentId != registerNumber", summary = "Generate PDF index")
    @Path("indexpdf/{studentId}")
    @GET
    public Response returnIndexPdf(@PathParam("studentId") int studentId,
                                @QueryParam("full") boolean full) {
        ByteArrayInputStream bais = dataExporterBean.genarateIndexPdf(studentId, full);
        String filename = String.format("index_%d.pdf", studentId);
        return Response
                .ok(bais)
                .type("application/pdf")
                .header("Content-Disposition", "attachment; filename=" + filename)
                .header("X-Export-Filename", filename)
                .build();
    }

    @Operation(description = "Generates index in CSV form for student with given studenId. NOTE: studentId != registerNumber", summary = "Generate CSV index")
    @Path("indexcsv/{studentId}")
    @GET
    public Response returnIndexCsv(@PathParam("studentId") int studentId,
                                @QueryParam("full") boolean full) {
        ByteArrayInputStream bais = dataExporterBean.generateIndexCsv(studentId, full);
        String filename = String.format("index_%d.csv", studentId);
        return Response
                .ok(bais)
                .type("text/csv")
                .header("Content-Disposition", "attachment; filename=" + filename)
                .header("X-Export-Filename", filename)
                .build();
    }

    @Operation(description = "Puts new request for enrolment confirmation for student", summary = "Put new enrolment confirmation request")
    @Path("requests/{studentId}/{copies}")
    @PUT
    public Response putEnrolmentConfirmationRequest(@PathParam("studentId") int studentId, @PathParam("copies") int copies) {
        ecrB.insertNewEnrolmentConfirmationRequest(studentId,copies);
        return Response.ok().build();
    }

    @Operation(description = "Gets all requests (for enrolment confirmation)", summary = "Gets all requests")
    @Parameters({
            @Parameter(name = "offset", description = "Starting point",in = ParameterIn.QUERY),
            @Parameter(name = "limit", description = "Number of returned entities", in = ParameterIn.QUERY),
            @Parameter(name = "order", description = "Order", in = ParameterIn.QUERY),
            @Parameter(name = "search", description = "Search", in = ParameterIn.QUERY)
    })
    @Path("requests")
    @GET
    @Produces("application/json")
    public Response getAllRequests() {
        QueryParameters query = QueryParameters.query(uriInfo.getRequestUri().getQuery()).build();
        return Response
                .ok(ecrB.getAllRequests(query))
                .header("X-Total-Count", ecrB.getAllRequests().size())
                .build();
    }

    @Operation(description = "Deletes requests for given id", summary = "Deletes requests")
    @Path("requests/{requestId}")
    @DELETE
    public Response deleteEnrolmentConfirmationRequest(@PathParam("requestId") int requestId) {
        ecrB.deleteRequest(requestId);
        return Response.ok().build();
    }

}
