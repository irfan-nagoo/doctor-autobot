package org.acme.autobot.service.impl;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.quarkus.panache.common.Page;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.acme.autobot.domain.DiseaseObject;
import org.acme.autobot.domain.MedicineObject;
import org.acme.autobot.domain.SymptomObject;
import org.acme.autobot.entity.Disease;
import org.acme.autobot.exception.RecordNotFoundException;
import org.acme.autobot.mapper.DiseaseMapper;
import org.acme.autobot.mapper.MedicineMapper;
import org.acme.autobot.mapper.SymptomMapper;
import org.acme.autobot.repository.DiseaseRepository;
import org.acme.autobot.request.DiseaseRequest;
import org.acme.autobot.response.BaseResponse;
import org.acme.autobot.response.DiseaseResponse;
import org.acme.autobot.service.DiseaseService;
import org.acme.autobot.util.SortUtil;
import org.apache.commons.collections4.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.acme.autobot.constants.AutoBotConstants.PERCENT;
import static org.acme.autobot.constants.MessagingConstants.*;

/**
 * @author irfan.nagoo
 */

@ApplicationScoped
@WithTransaction
@RequiredArgsConstructor
@Slf4j
public class DiseaseServiceImpl implements DiseaseService {

    private final DiseaseRepository diseaseRepository;
    private final DiseaseMapper diseaseMapper;
    private final MedicineMapper medicineMapper;
    private final SymptomMapper symptomMapper;

    @Override
    public Uni<DiseaseResponse> findDiseaseAll(int pageNo, int pageSize, String sortBy, String sortDirection) {
        log.info("Processing find all Diseases request");
        return diseaseRepository.findAll(SortUtil.getSort(sortBy, sortDirection)).page(Page.of(pageNo, pageSize)).list()
                .onItem().ifNotNull().transform(x -> x.stream().map(diseaseMapper::diseaseToDiseaseObject))
                .onItem().ifNull().failWith(new RecordNotFoundException(NOT_FOUND_ERROR_MSG))
                .onItem().ifNotNull().transform(DiseaseServiceImpl::buildDiseaseResponse);
    }

    @Override
    public Uni<DiseaseResponse> findDiseaseById(Long id) {
        log.info("Processing find Disease by Id [{}] request", id);
        return diseaseRepository.findById(id)
                .onItem().ifNotNull().transform(x -> Stream.of(diseaseMapper.diseaseToDiseaseObject(x)))
                .onItem().ifNull().failWith(new RecordNotFoundException(NOT_FOUND_ERROR_MSG))
                .onItem().ifNotNull().transform(DiseaseServiceImpl::buildDiseaseResponse);
    }

    @Override
    public Uni<DiseaseResponse> findDiseaseByCui(String cui) {
        log.info("Processing find Disease by CUI [{}] request", cui);
        return diseaseRepository.findByCui(cui)
                .onItem().ifNotNull().transform(x -> Stream.of(diseaseMapper.diseaseToDiseaseObject(x)))
                .onItem().ifNull().failWith(new RecordNotFoundException(NOT_FOUND_ERROR_MSG))
                .onItem().ifNotNull().transform(DiseaseServiceImpl::buildDiseaseResponse);
    }

    @Override
    public Uni<DiseaseResponse> searchDiseaseByName(String name, int pageNo, int pageSize) {
        log.info("Processing search Disease by name [{}] request", name);
        return diseaseRepository.searchByName(PERCENT + name.toLowerCase() + PERCENT).page(Page.of(pageNo, pageSize)).list()
                .onItem().ifNotNull().transform(x -> x.stream().map(diseaseMapper::diseaseToDiseaseObject))
                .onItem().ifNull().failWith(new RecordNotFoundException(NOT_FOUND_ERROR_MSG))
                .onItem().ifNotNull().transform(DiseaseServiceImpl::buildDiseaseResponse);
    }

    @Override
    public Uni<DiseaseResponse> saveDisease(DiseaseRequest diseaseRequest) {
        log.info("Processing save Disease request");
        return diseaseRepository.persist(diseaseMapper.diseaseObjectToDisease(diseaseRequest.getDisease()))
                .onItem().ifNotNull().transform(x -> Stream.of(diseaseMapper.diseaseToDiseaseObject(x)))
                .onItem().ifNotNull().transform(DiseaseServiceImpl::buildDiseaseResponse);
    }

    @Override
    public Uni<DiseaseResponse> updateDisease(Long id, DiseaseRequest diseaseRequest) {
        log.info("Processing update Disease for Id [{}] request", id);
        return diseaseRepository.findById(id)
                .onItem().ifNotNull().transform(x -> update(x, diseaseRequest))
                .onItem().ifNull().failWith(new RecordNotFoundException(NOT_FOUND_ERROR_MSG))
                .onItem().ifNotNull().transform(x -> Stream.of(diseaseMapper.diseaseToDiseaseObject(x)))
                .onItem().ifNotNull().transform(DiseaseServiceImpl::buildDiseaseResponse);
    }

    @Override
    public Uni<BaseResponse> deleteDisease(Long id) {
        log.info("Processing delete Disease by Id [{}] request", id);
        return diseaseRepository.findById(id)
                .onItem().ifNotNull().call(diseaseRepository::delete)
                .onItem().ifNull().failWith(new RecordNotFoundException(NOT_FOUND_ERROR_MSG))
                .onItem().ifNotNull().transform(x -> new BaseResponse(HttpResponseStatus.OK.reasonPhrase(), RECORD_DELETED_MSG));
    }

    private static DiseaseResponse buildDiseaseResponse(Stream<DiseaseObject> diseaseStream) {
        List<DiseaseObject> diseaseList = diseaseStream.collect(Collectors.toList());
        return new DiseaseResponse(HttpResponseStatus.OK.reasonPhrase(),
                String.format(TOTAL_RECORD_MSG, diseaseList.size()), diseaseList);
    }

    private Disease update(Disease disease, DiseaseRequest diseaseRequest) {
        DiseaseObject diseaseObject = diseaseRequest.getDisease();
        disease.setName(diseaseObject.getName());
        disease.setSemType1(diseaseObject.getSemType1());
        if (diseaseObject.getStatus() != null) {
            disease.setStatus(diseaseObject.getStatus().toString());
        }
        // update medicines
        if (CollectionUtils.isNotEmpty(diseaseObject.getMedicines())) {
            List<MedicineObject> medicineObjectList = diseaseObject.getMedicines();
            disease.setMedicines(medicineObjectList.stream()
                    .map(medicineMapper::medicineObjectToMedicine)
                    .collect(Collectors.toSet()));
        } else {
            disease.setMedicines(null);
        }

        // update symptom
        if (CollectionUtils.isNotEmpty(diseaseObject.getSymptoms())) {
            List<SymptomObject> symptomObjectList = diseaseObject.getSymptoms();
            disease.setSymptoms(symptomObjectList.stream()
                    .map(symptomMapper::symptomObjectToSymptom)
                    .collect(Collectors.toSet()));
        } else {
            disease.setSymptoms(null);
        }
        disease.setUpdatedBy(diseaseObject.getUpdatedBy());
        disease.setUpdateDate(LocalDateTime.now());
        return disease;
    }
}
