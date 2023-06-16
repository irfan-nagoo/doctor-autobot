package org.acme.autobot.service;

import io.smallrye.mutiny.Uni;
import org.acme.autobot.request.DiseaseRequest;
import org.acme.autobot.response.BaseResponse;
import org.acme.autobot.response.DiseaseResponse;

/**
 * @author irfan.nagoo
 */
public interface DiseaseService {

    Uni<DiseaseResponse> findDiseaseAll(int pageNo, int pageSize, String sortBy, String sortDirection);

    Uni<DiseaseResponse> findDiseaseById(Long id);

    Uni<DiseaseResponse> findDiseaseByCui(String cui);

    Uni<DiseaseResponse> searchDiseaseByName(String name, int pageNo, int pageSize);

    Uni<DiseaseResponse> saveDisease(DiseaseRequest diseaseRequest);

    Uni<DiseaseResponse> updateDisease(Long id, DiseaseRequest diseaseRequest);

    Uni<BaseResponse> deleteDisease(Long id);
}
