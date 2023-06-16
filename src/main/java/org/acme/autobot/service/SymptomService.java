package org.acme.autobot.service;

import io.smallrye.mutiny.Uni;
import org.acme.autobot.request.SymptomRequest;
import org.acme.autobot.response.BaseResponse;
import org.acme.autobot.response.SymptomResponse;

/**
 * @author irfan.nagoo
 */
public interface SymptomService {

    Uni<SymptomResponse> findSymptomAll(int pageNo, int pageSize, String sortBy, String sortDirection);

    Uni<SymptomResponse> findSymptomById(Long id);

    Uni<SymptomResponse> findSymptomByCui(String cui);

    Uni<SymptomResponse> searchSymptomByName(String name, int pageNo, int pageSize);

    Uni<SymptomResponse> saveSymptom(SymptomRequest symptomRequest);

    Uni<SymptomResponse> updateSymptom(Long id, SymptomRequest symptomRequest);

    Uni<BaseResponse> deleteSymptom(Long id);
}
