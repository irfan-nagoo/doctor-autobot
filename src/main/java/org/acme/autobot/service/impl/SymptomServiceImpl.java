package org.acme.autobot.service.impl;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.quarkus.panache.common.Page;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.acme.autobot.domain.SymptomObject;
import org.acme.autobot.entity.Symptom;
import org.acme.autobot.exception.RecordNotFoundException;
import org.acme.autobot.mapper.SymptomMapper;
import org.acme.autobot.repository.SymptomRepository;
import org.acme.autobot.request.SymptomRequest;
import org.acme.autobot.response.BaseResponse;
import org.acme.autobot.response.SymptomResponse;
import org.acme.autobot.service.SymptomService;
import org.acme.autobot.util.SortUtil;

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
public class SymptomServiceImpl implements SymptomService {

    private final SymptomRepository symptomRepository;
    private final SymptomMapper symptomMapper;

    @Override
    public Uni<SymptomResponse> findSymptomAll(int pageNo, int pageSize, String sortBy, String sortDirection) {
        log.info("Processing find all Symptoms request");
        return symptomRepository.findAll(SortUtil.getSort(sortBy, sortDirection)).page(Page.of(pageNo, pageSize))
                .list()
                .onItem().ifNotNull().transform(x -> x.stream().map(symptomMapper::symptomToSymptomObject))
                .onItem().ifNull().failWith(new RecordNotFoundException(NOT_FOUND_ERROR_MSG))
                .onItem().ifNotNull().transform(SymptomServiceImpl::buildSymptomResponse);
    }

    @Override
    public Uni<SymptomResponse> findSymptomById(Long id) {
        log.info("Processing find Symptom by Id [{}] request", id);
        return symptomRepository.findById(id)
                .onItem().ifNotNull().transform(x -> Stream.of(symptomMapper.symptomToSymptomObject(x)))
                .onItem().ifNull().failWith(new RecordNotFoundException(NOT_FOUND_ERROR_MSG))
                .onItem().ifNotNull().transform(SymptomServiceImpl::buildSymptomResponse);
    }

    @Override
    public Uni<SymptomResponse> findSymptomByCui(String cui) {
        log.info("Processing find Symptom by CUI [{}] request", cui);
        return symptomRepository.findByCui(cui)
                .onItem().ifNotNull().transform(x -> Stream.of(symptomMapper.symptomToSymptomObject(x)))
                .onItem().ifNull().failWith(new RecordNotFoundException(NOT_FOUND_ERROR_MSG))
                .onItem().ifNotNull().transform(SymptomServiceImpl::buildSymptomResponse);
    }

    @Override
    public Uni<SymptomResponse> searchSymptomByName(String name, int pageNo, int pageSize) {
        log.info("Processing search Symptom by name [{}] request", name);
        return symptomRepository.searchByName(PERCENT + name.toLowerCase() + PERCENT).page(Page.of(pageNo, pageSize)).list()
                .onItem().ifNotNull().transform(x -> x.stream().map(symptomMapper::symptomToSymptomObject))
                .onItem().ifNull().failWith(new RecordNotFoundException(NOT_FOUND_ERROR_MSG))
                .onItem().ifNotNull().transform(SymptomServiceImpl::buildSymptomResponse);
    }

    @Override
    public Uni<SymptomResponse> saveSymptom(SymptomRequest symptomRequest) {
        log.info("Processing save Symptom request");
        return symptomRepository.persist(symptomMapper.symptomObjectToSymptom(symptomRequest.getSymptom()))
                .onItem().ifNotNull().transform(x -> Stream.of(symptomMapper.symptomToSymptomObject(x)))
                .onItem().ifNotNull().transform(SymptomServiceImpl::buildSymptomResponse);
    }

    @Override
    public Uni<SymptomResponse> updateSymptom(Long id, SymptomRequest symptomRequest) {
        log.info("Processing update Symptom for Id [{}] request", id);
        return symptomRepository.findById(id)
                .onItem().ifNotNull().transform(x -> update(x, symptomRequest))
                .onItem().ifNull().failWith(new RecordNotFoundException(NOT_FOUND_ERROR_MSG))
                .onItem().ifNotNull().transform(x -> Stream.of(symptomMapper.symptomToSymptomObject(x)))
                .onItem().ifNotNull().transform(SymptomServiceImpl::buildSymptomResponse);
    }

    @Override
    public Uni<BaseResponse> deleteSymptom(Long id) {
        log.info("Processing delete Symptom by Id [{}] request", id);
        return symptomRepository.findById(id)
                .onItem().ifNotNull().call(symptomRepository::delete)
                .onItem().ifNull().failWith(new RecordNotFoundException(NOT_FOUND_ERROR_MSG))
                .onItem().ifNotNull().transform(x -> new BaseResponse(HttpResponseStatus.OK.reasonPhrase(), RECORD_DELETED_MSG));
    }

    private static SymptomResponse buildSymptomResponse(Stream<SymptomObject> symptomStream) {
        List<SymptomObject> symptomList = symptomStream.collect(Collectors.toList());
        return new SymptomResponse(HttpResponseStatus.OK.reasonPhrase(),
                String.format(TOTAL_RECORD_MSG, symptomList.size()), symptomList);
    }

    private static Symptom update(Symptom symptom, SymptomRequest symptomRequest) {
        SymptomObject symptomObject = symptomRequest.getSymptom();
        symptom.setCui(symptomObject.getCui());
        symptom.setName(symptomObject.getName());
        if (symptomObject.getStatus() != null) {
            symptom.setStatus(symptomObject.getStatus().toString());
        }
        symptom.setUpdatedBy(symptomObject.getUpdatedBy());
        symptom.setUpdateDate(LocalDateTime.now());
        return symptom;
    }
}
