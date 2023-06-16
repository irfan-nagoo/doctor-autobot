package org.acme.autobot.resource;

import io.smallrye.mutiny.Uni;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lombok.RequiredArgsConstructor;
import org.acme.autobot.request.MedicineRequest;
import org.acme.autobot.response.BaseResponse;
import org.acme.autobot.response.MedicineResponse;
import org.acme.autobot.service.MedicineService;
import org.jboss.resteasy.reactive.RestPath;
import org.jboss.resteasy.reactive.RestQuery;

@Path("/medicine")
@RequiredArgsConstructor
public class MedicineResource {

    private final MedicineService medicineService;

    /**
     * This paginated and sorted API returns the list of all the Medicines
     *
     * @param pageNo        Page number
     * @param pageSize      Page Size
     * @param sortBy        Comma separated sort by fields
     * @param sortDirection Comma separated sort direction for each sort by field
     * @return List of all the Medicines
     */
    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<MedicineResponse> findMedicineAll(@DefaultValue("0") @RestQuery int pageNo,
                                               @DefaultValue("10") @RestQuery int pageSize,
                                               @DefaultValue("name") @RestQuery String sortBy,
                                               @DefaultValue("asc") @RestQuery String sortDirection) {
        return medicineService.findMedicineAll(pageNo, pageSize, sortBy, sortDirection);
    }

    /**
     * This API returns the Medicine record as per the given id.
     *
     * @param id id of the Medicine
     * @return Medicine record
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<MedicineResponse> findMedicineById(@RestPath Long id) {
        return medicineService.findMedicineById(id);
    }

    /**
     * This API returns the Medicine record as the given CUI.
     *
     * @param cui CUI of Medicine
     * @return Medicine record
     */
    @GET
    @Path("/cui/{cui}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<MedicineResponse> findMedicineByCui(@RestPath String cui) {
        return medicineService.findMedicineByCui(cui);
    }

    /**
     * This API return the paginated list of Medicines as the given search string.
     *
     * @param name     Search string for Medicine name
     * @param pageNo   Page number
     * @param pageSize Page size
     * @return List of matching Medicines records
     */
    @GET
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<MedicineResponse> searchMedicineByName(@DefaultValue("a") @RestQuery String name,
                                                    @DefaultValue("0") @RestQuery int pageNo,
                                                    @DefaultValue("10") @RestQuery int pageSize) {
        return medicineService.searchMedicineByName(name, pageNo, pageSize);
    }

    /**
     * This API saves the Medicine record
     *
     * @param medicineRequest Medicine request
     * @return Saved Medicine record
     */
    @POST
    @Path("/save")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<MedicineResponse> saveMedicine(@Valid MedicineRequest medicineRequest) {
        return medicineService.saveMedicine(medicineRequest);
    }

    /**
     * This API updates the Medicine record
     *
     * @param medicineRequest Medicine request
     * @return Updated Medicine record
     */
    @PUT
    @Path("/update/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<MedicineResponse> saveMedicine(@RestPath Long id, @Valid MedicineRequest medicineRequest) {
        return medicineService.updateMedicine(id, medicineRequest);
    }

    /**
     * This API deletes the Medicine record as per the given id.
     *
     * @param id id of Medicine
     * @return Success/Failure response
     */
    @DELETE
    @Path("/delete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<BaseResponse> deleteMedicine(@RestPath Long id) {
        return medicineService.deleteMedicine(id);
    }
}
