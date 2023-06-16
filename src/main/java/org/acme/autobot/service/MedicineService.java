package org.acme.autobot.service;

import io.smallrye.mutiny.Uni;
import org.acme.autobot.request.MedicineRequest;
import org.acme.autobot.response.BaseResponse;
import org.acme.autobot.response.MedicineResponse;

/**
 * @author irfan.nagoo
 */
public interface MedicineService {

    Uni<MedicineResponse> findMedicineAll(int pageNo, int pageSize, String sortBy, String sortDirection);

    Uni<MedicineResponse> findMedicineById(Long id);

    Uni<MedicineResponse> findMedicineByCui(String cui);

    Uni<MedicineResponse> searchMedicineByName(String name, int pageNo, int pageSize);

    Uni<MedicineResponse> saveMedicine(MedicineRequest medicineRequest);

    Uni<MedicineResponse> updateMedicine(Long id, MedicineRequest medicineRequest);

    Uni<BaseResponse> deleteMedicine(Long id);
}
