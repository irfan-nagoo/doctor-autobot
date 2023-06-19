package org.acme.autobot.resource;

import io.smallrye.mutiny.Uni;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lombok.RequiredArgsConstructor;
import org.acme.autobot.request.SymptomRequest;
import org.acme.autobot.response.BaseResponse;
import org.acme.autobot.response.SymptomResponse;
import org.acme.autobot.service.SymptomService;
import org.jboss.resteasy.reactive.RestPath;
import org.jboss.resteasy.reactive.RestQuery;

@Path("/symptom")
@RequiredArgsConstructor
public class SymptomResource {

    private final SymptomService symptomService;

    /**
     * This paginated and sorted API returns the list of all the Symptoms
     *
     * @param pageNo        Page number
     * @param pageSize      Page Size
     * @param sortBy        Comma separated sort by fields
     * @param sortDirection Comma separated sort direction for each sort by field
     * @return List of all the Symptoms
     */
    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<SymptomResponse> findSymptomAll(@DefaultValue("0") @RestQuery int pageNo,
                                               @DefaultValue("10") @RestQuery int pageSize,
                                               @DefaultValue("name") @RestQuery String sortBy,
                                               @DefaultValue("asc") @RestQuery String sortDirection) {
        return symptomService.findSymptomAll(pageNo, pageSize, sortBy, sortDirection);
    }

    /**
     * This API returns the Symptom record as per the given id.
     *
     * @param id id of the Symptom
     * @return Symptom record
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<SymptomResponse> findSymptomById(@RestPath Long id) {
        return symptomService.findSymptomById(id);
    }

    /**
     * This API returns the Symptom record as the given CUI.
     *
     * @param cui CUI of Symptom
     * @return Symptom record
     */
    @GET
    @Path("/cui/{cui}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<SymptomResponse> findSymptomByCui(@RestPath String cui) {
        return symptomService.findSymptomByCui(cui);
    }

    /**
     * This API return the paginated list of Symptoms as the given search string.
     *
     * @param name     Search string for Symptom name
     * @param pageNo   Page number
     * @param pageSize Page size
     * @return List of matching Symptoms records
     */
    @GET
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<SymptomResponse> searchSymptomByName(@DefaultValue("a") @RestQuery String name,
                                                    @DefaultValue("0") @RestQuery int pageNo,
                                                    @DefaultValue("10") @RestQuery int pageSize) {
        return symptomService.searchSymptomByName(name, pageNo, pageSize);
    }

    /**
     * This API saves the Symptom record
     *
     * @param symptomRequest Symptom request
     * @return Saved Symptom record
     */
    @POST
    @Path("/save")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<SymptomResponse> saveSymptom(@Valid SymptomRequest symptomRequest) {
        return symptomService.saveSymptom(symptomRequest);
    }

    /**
     * This API updates the Symptom record
     *
     * @param symptomRequest Symptom request
     * @return Updated Symptom record
     */
    @PUT
    @Path("/update/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<SymptomResponse> updateSymptom(@RestPath Long id, @Valid SymptomRequest symptomRequest) {
        return symptomService.updateSymptom(id, symptomRequest);
    }

    /**
     * This API deletes the Symptom record as per the given id.
     *
     * @param id id of Symptom
     * @return Success/Failure response
     */
    @DELETE
    @Path("/delete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<BaseResponse> deleteSymptom(@RestPath Long id) {
        return symptomService.deleteSymptom(id);
    }
}
