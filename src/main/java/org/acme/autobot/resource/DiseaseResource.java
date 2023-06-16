package org.acme.autobot.resource;

import io.smallrye.mutiny.Uni;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lombok.RequiredArgsConstructor;
import org.acme.autobot.request.DiseaseRequest;
import org.acme.autobot.response.BaseResponse;
import org.acme.autobot.response.DiseaseResponse;
import org.acme.autobot.service.DiseaseService;
import org.jboss.resteasy.reactive.RestPath;
import org.jboss.resteasy.reactive.RestQuery;

@Path("/disease")
@RequiredArgsConstructor
public class DiseaseResource {

    private final DiseaseService diseaseService;

    /**
     * This paginated and sorted API returns the list of all the Diseases
     *
     * @param pageNo        Page number
     * @param pageSize      Page Size
     * @param sortBy        Comma separated sort by fields
     * @param sortDirection Comma separated sort direction for each sort by field
     * @return List of all the Diseases
     */
    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<DiseaseResponse> findDiseaseAll(@DefaultValue("0") @RestQuery int pageNo,
                                               @DefaultValue("10") @RestQuery int pageSize,
                                               @DefaultValue("name") @RestQuery String sortBy,
                                               @DefaultValue("asc") @RestQuery String sortDirection) {
        return diseaseService.findDiseaseAll(pageNo, pageSize, sortBy, sortDirection);
    }

    /**
     * This API returns the Disease record as per the given id.
     *
     * @param id id of the Disease
     * @return Disease record
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<DiseaseResponse> findDiseaseById(@RestPath Long id) {
        return diseaseService.findDiseaseById(id);
    }

    /**
     * This API returns the Disease record as the given CUI.
     *
     * @param cui CUI of Disease
     * @return Disease record
     */
    @GET
    @Path("/cui/{cui}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<DiseaseResponse> findDiseaseByCui(@RestPath String cui) {
        return diseaseService.findDiseaseByCui(cui);
    }

    /**
     * This API return the paginated list of Diseases as the given search string.
     *
     * @param name     Search string for Disease name
     * @param pageNo   Page number
     * @param pageSize Page size
     * @return List of matching Diseases records
     */
    @GET
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<DiseaseResponse> searchDiseaseByName(@DefaultValue("a") @RestQuery String name,
                                                    @DefaultValue("0") @RestQuery int pageNo,
                                                    @DefaultValue("10") @RestQuery int pageSize) {
        return diseaseService.searchDiseaseByName(name, pageNo, pageSize);
    }

    /**
     * This API saves the Disease record
     *
     * @param diseaseRequest Disease request
     * @return Saved Disease record
     */
    @POST
    @Path("/save")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<DiseaseResponse> saveDisease(@Valid DiseaseRequest diseaseRequest) {
        return diseaseService.saveDisease(diseaseRequest);
    }

    /**
     * This API updates the Disease record
     *
     * @param diseaseRequest Disease request
     * @return Updated Disease record
     */
    @PUT
    @Path("/update/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<DiseaseResponse> saveDisease(@RestPath Long id, @Valid DiseaseRequest diseaseRequest) {
        return diseaseService.updateDisease(id, diseaseRequest);
    }

    /**
     * This API deletes the Disease record as per the given id.
     *
     * @param id id of Disease
     * @return Success/Failure response
     */
    @DELETE
    @Path("/delete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<BaseResponse> deleteDisease(@RestPath Long id) {
        return diseaseService.deleteDisease(id);
    }
}
