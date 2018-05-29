package api.sources;

import api.exceptions.NoRequestBodyException;
import api.interceptors.annotations.LogApiCalls;
import beans.crud.AgreementBean;
import com.kumuluz.ee.rest.beans.QueryParameters;
import entities.Agreement;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
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
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("agreement")
@ApplicationScoped
@LogApiCalls
@Tags(value = @Tag(name = "agreement"))
public class AgreementSource {

    private final Logger log = Logger.getLogger(this.getClass().getName());

    @Context
    protected UriInfo uriInfo;

    @Inject
    private AgreementBean ab;

    @GET
    public Response getAllAgreements() {
        log.info("Calling getAllAgreements()...");
        QueryParameters query = QueryParameters.query(uriInfo.getRequestUri().getQuery()).build();

        // clone the query, but remove the 'offset' and 'limit' part, so we get the actual count
        // instead of the count without the already seen values
        QueryParameters defaultQuery = new QueryParameters();

        QueryParameters q2 = QueryParameters.query(uriInfo.getRequestUri().getQuery()).build();
        q2.setLimit(defaultQuery.getLimit());
        q2.setOffset(defaultQuery.getOffset());

        List<Agreement> agreements = ab.getAgreements(query);

        return Response
                .ok(agreements)
                .header("X-Total-Count", ab.getAgreements(q2).size())
                .build();
    }

    @GET
    @Path("{id}")
    public Response getAgreementById(@PathParam("id") int idAgreement) {
        Agreement agreement = ab.getAgreementById(idAgreement);

        if(agreement == null)
            return Response.status(Response.Status.BAD_REQUEST).entity(
                    new ResponseError(400, String.format("ni sklepa z ID-jem %d ... ", idAgreement))).build();

        return Response.status(Response.Status.OK).entity(agreement).build();
    }

    @GET
    @Path("student")
    public Response getAgreementsForStudent(@QueryParam("studentid") int idStudent) {
        log.info("Calling getAgreementsForStudent()...");
        List<Agreement> agreements = ab.getAgreementsForStudent(idStudent);

        if(agreements == null || agreements.isEmpty())
            return Response.status(Response.Status.NOT_FOUND).entity(new ResponseError(404,
                    String.format("Noben sklep ni bil najden za študenta z ID-jem %d...", idStudent))).build();

        return Response.status(Response.Status.OK).entity(agreements).build();
    }

    @POST
    public Response updateAgreement(@RequestBody Agreement agreement) {
        if(agreement == null)
            throw new NoRequestBodyException();

        if(!sanityCheckDates(agreement))
            return Response.status(Response.Status.BAD_REQUEST).entity(new ResponseError(400, "datum konca veljavnosti sklepa je pred " +
                    "datumom izdaje sklepa")).build();

        agreement = ab.updateAgreement(agreement);

        return Response.status(Response.Status.OK).entity(agreement).build();
    }

    @PUT
    public Response createAgreement(@RequestBody Agreement agreement) {
        if(agreement == null)
            throw new NoRequestBodyException();

        if(!sanityCheckDates(agreement))
            return Response.status(Response.Status.BAD_REQUEST).entity(new ResponseError(400, "datum konca veljavnosti sklepa je pred " +
                    "datumom izdaje sklepa")).build();

        agreement = ab.insertAgreement(agreement);

        return Response.ok().entity(agreement).build();
    }

    @DELETE
    @Path("{id}")
    public Response deleteAgreement(@PathParam("id") int idAgreement) {
        ab.deleteAgreement(idAgreement);

        return Response.ok().build();
    }

    // a bit of idiot-proofing
    private boolean sanityCheckDates(Agreement agreement) {
        Date issuedDate = agreement.getIssueDateObject();
        Date validUntilDate = agreement.getValidUntilObject();

        // the field is optional
        if(validUntilDate == null)
            return true;

        // 'valid until' should be after 'issue date'
        if(validUntilDate.before(issuedDate))
            return false;

        return true;
    }

}
