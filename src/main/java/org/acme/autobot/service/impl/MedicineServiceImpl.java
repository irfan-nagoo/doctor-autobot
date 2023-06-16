package org.acme.autobot.service.impl;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.quarkus.panache.common.Page;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.acme.autobot.domain.MedicineObject;
import org.acme.autobot.entity.Medicine;
import org.acme.autobot.exception.RecordNotFoundException;
import org.acme.autobot.mapper.MedicineMapper;
import org.acme.autobot.repository.MedicineRepository;
import org.acme.autobot.request.MedicineRequest;
import org.acme.autobot.response.BaseResponse;
import org.acme.autobot.response.MedicineResponse;
import org.acme.autobot.service.MedicineService;
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
public class MedicineServiceImpl implements MedicineService {

    private final MedicineRepository medicineRepository;
    private final MedicineMapper medicineMapper;

    @Override
    public Uni<MedicineResponse> findMedicineAll(int pageNo, int pageSize, String sortBy, String sortDirection) {
        log.info("Processing find all Medicine request");
        return medicineRepository.findAll(SortUtil.getSort(sortBy, sortDirection)).page(Page.of(pageNo, pageSize)).list()
                .onItem().ifNotNull().transform(x -> x.stream().map(medicineMapper::medicineToMedicineObject))
                .onItem().ifNull().failWith(new RecordNotFoundException(NOT_FOUND_ERROR_MSG))
                .onItem().ifNotNull().transform(MedicineServiceImpl::buildMedicineResponse);
    }

    @Override
    public Uni<MedicineResponse> findMedicineById(Long id) {
        log.info("Processing find Medicine by Id [{}] request", id);
        return medicineRepository.findById(id)
                .onItem().ifNotNull().transform(x -> Stream.of(medicineMapper.medicineToMedicineObject(x)))
                .onItem().ifNull().failWith(new RecordNotFoundException(NOT_FOUND_ERROR_MSG))
                .onItem().ifNotNull().transform(MedicineServiceImpl::buildMedicineResponse);
    }

    @Override
    public Uni<MedicineResponse> findMedicineByCui(String cui) {
        log.info("Processing find Medicine by CUI [{}] request", cui);
        return medicineRepository.findByCui(cui)
                .onItem().ifNotNull().transform(x -> Stream.of(medicineMapper.medicineToMedicineObject(x)))
                .onItem().ifNull().failWith(new RecordNotFoundException(NOT_FOUND_ERROR_MSG))
                .onItem().ifNotNull().transform(MedicineServiceImpl::buildMedicineResponse);
    }

    @Override
    public Uni<MedicineResponse> searchMedicineByName(String name, int pageNo, int pageSize) {
        log.info("Processing search Medicine by name [{}] request", name);
        return medicineRepository.searchByName(PERCENT + name.toLowerCase() + PERCENT).page(Page.of(pageNo, pageSize)).list()
                .onItem().ifNotNull().transform(x -> x.stream().map(medicineMapper::medicineToMedicineObject))
                .onItem().ifNull().failWith(new RecordNotFoundException(NOT_FOUND_ERROR_MSG))
                .onItem().ifNotNull().transform(MedicineServiceImpl::buildMedicineResponse);
    }

    @Override
    public Uni<MedicineResponse> saveMedicine(MedicineRequest medicineRequest) {
        log.info("Processing save Medicine request");
        return medicineRepository.persist(medicineMapper.medicineObjectToMedicine(medicineRequest.getMedicine()))
                .onItem().ifNotNull().transform(x -> Stream.of(medicineMapper.medicineToMedicineObject(x)))
                .onItem().ifNotNull().transform(MedicineServiceImpl::buildMedicineResponse);
    }

    @Override
    public Uni<MedicineResponse> updateMedicine(Long id, MedicineRequest medicineRequest) {
        log.info("Processing update Medicine for Id [{}] request", id);
        return medicineRepository.findById(id)
                .onItem().ifNotNull().transform(x -> update(x, medicineRequest))
                .onItem().ifNull().failWith(new RecordNotFoundException(NOT_FOUND_ERROR_MSG))
                .onItem().ifNotNull().transform(x -> Stream.of(medicineMapper.medicineToMedicineObject(x)))
                .onItem().ifNotNull().transform(MedicineServiceImpl::buildMedicineResponse);
    }

    @Override
    public Uni<BaseResponse> deleteMedicine(Long id) {
        log.info("Processing delete Medicine by Id [{}] request", id);
        return medicineRepository.findById(id)
                .onItem().ifNotNull().call(medicineRepository::delete)
                .onItem().ifNull().failWith(new RecordNotFoundException(NOT_FOUND_ERROR_MSG))
                .onItem().ifNotNull().transform(x -> new BaseResponse(HttpResponseStatus.OK.reasonPhrase(), RECORD_DELETED_MSG));
    }

    private static MedicineResponse buildMedicineResponse(Stream<MedicineObject> medicineStream) {
        List<MedicineObject> medicineList = medicineStream.collect(Collectors.toList());
        return new MedicineResponse(HttpResponseStatus.OK.reasonPhrase(),
                String.format(TOTAL_RECORD_MSG, medicineList.size()), medicineList);
    }

    private static Medicine update(Medicine medicine, MedicineRequest medicineRequest) {
        MedicineObject medicineObject = medicineRequest.getMedicine();
        medicine.setCui(medicineObject.getCui());
        medicine.setName(medicineObject.getName());
        medicine.setSource(medicineObject.getSource());
        medicine.setSemType1(medicineObject.getSemType1());
        medicine.setSemType2(medicineObject.getSemType2());
        medicine.setSemType3(medicineObject.getSemType3());
        if (medicineObject.getStatus() != null) {
            medicine.setStatus(medicineObject.getStatus().toString());
        }
        medicine.setUpdatedBy(medicineObject.getUpdatedBy());
        medicine.setUpdateDate(LocalDateTime.now());
        return medicine;
    }
}
