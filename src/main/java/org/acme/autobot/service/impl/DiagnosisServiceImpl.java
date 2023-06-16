package org.acme.autobot.service.impl;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.acme.autobot.analyzer.DiseaseAnalyzer;
import org.acme.autobot.constants.SymptomType;
import org.acme.autobot.domain.DiagnosisObject;
import org.acme.autobot.entity.Disease;
import org.acme.autobot.entity.Medicine;
import org.acme.autobot.entity.Symptom;
import org.acme.autobot.exception.RecordNotFoundException;
import org.acme.autobot.repository.DiseaseRepository;
import org.acme.autobot.repository.SymptomRepository;
import org.acme.autobot.request.DiagnosisRequest;
import org.acme.autobot.response.DiagnosisResponse;
import org.acme.autobot.service.DiagnosisService;
import org.acme.autobot.vo.DiagnosisVO;
import org.apache.commons.collections4.CollectionUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.acme.autobot.constants.MessagingConstants.PROCESS_SUCCESS_MSG;
import static org.acme.autobot.constants.MessagingConstants.SYMPTOM_FOUND_ERROR_MSG;

/**
 * @author irfan.nagoo
 */

@ApplicationScoped
@WithTransaction
@RequiredArgsConstructor
@Setter
@Slf4j
public class DiagnosisServiceImpl implements DiagnosisService {

    @ConfigProperty(name = "org.acme.autobot.major.disease.threshold")
    private Integer majorDiseaseThreshHold;

    @ConfigProperty(name = "org.acme.autobot.major.disease.maxRecords")
    private Integer majorDiseaseMaxRecords;

    @ConfigProperty(name = "org.acme.autobot.minor.disease.maxRecords")
    private Integer minorDiseaseMaxRecords;

    @ConfigProperty(name = "org.acme.autobot.medicine.maxRecords")
    private Integer medicineMaxRecords;

    private final SymptomRepository symptomRepository;
    private final DiseaseRepository diseaseRepository;
    private final DiseaseAnalyzer diseaseAnalyzer;

    @Override
    public Uni<DiagnosisResponse> preliminaryDiagnosis(DiagnosisRequest diagnosisRequest) {
        log.info("Processing Preliminary Diagnosis request");
        var symptomCuiList = SymptomType.getSymptomTypeList(diagnosisRequest).stream()
                .map(SymptomType::getCui)
                .collect(Collectors.toList());
        if (symptomCuiList.isEmpty()){
            throw new RecordNotFoundException(SYMPTOM_FOUND_ERROR_MSG);
        }

        // Symptoms
        var symptoms = symptomRepository.findByCuiList(symptomCuiList)
                .onItem().ifNotNull().transform(x -> transformSymptoms(x, symptomCuiList));

        // Minor Diseases
        var finalSymptomCuiList = getSlice(symptomCuiList, minorDiseaseMaxRecords);
        var minorDiseases = diseaseRepository.findByCuiList(finalSymptomCuiList)
                .onItem().ifNotNull().transform(x -> transformDiseases(x, finalSymptomCuiList));

        // Major Diseases
        Uni<List<DiagnosisVO>> majorDiseases = Uni.createFrom().nullItem();
        if (symptomCuiList.size() >= majorDiseaseThreshHold) {
            List<String> diseaseCuiList = diseaseAnalyzer.analyze(symptomCuiList);
            var finalDiseaseCuiList = getSlice(diseaseCuiList, majorDiseaseMaxRecords);
            majorDiseases = diseaseRepository.findByCuiList(finalDiseaseCuiList)
                    .onItem().ifNotNull().transform(x -> transformDiseases(x, finalDiseaseCuiList));
        }

        // Combine and build response
        return Uni.combine().all()
                .unis(symptoms, minorDiseases, majorDiseases)
                .usingConcurrencyOf(1)
                .combinedWith(DiagnosisServiceImpl::buildDiagnosisResponse);
    }

    private List<DiagnosisVO> transformSymptoms(List<Symptom> symptomList, List<String> cuiList) {
        List<DiagnosisVO> symptoms = new ArrayList<>();
        cuiList.forEach(cui -> {
            var symptomOpt = symptomList.stream()
                    .filter(x -> x.getCui().equalsIgnoreCase(cui))
                    .findAny();
            if (symptomOpt.isPresent()) {
                var symptom = new DiagnosisVO(symptomOpt.get().getCui(), symptomOpt.get().getName(), null);
                symptoms.add(symptom);
            }
        });
        return symptoms;
    }

    private List<DiagnosisVO> transformDiseases(List<Disease> diseasesList, List<String> cuiList) {
        List<DiagnosisVO> diseases = new ArrayList<>();
        cuiList.forEach(cui -> {
            var diseaseOpt = diseasesList.stream()
                    .filter(x -> x.getCui().equalsIgnoreCase(cui))
                    .findAny();
            if (diseaseOpt.isPresent()) {
                var disease = new DiagnosisVO(diseaseOpt.get().getCui(), diseaseOpt.get().getName(), null);
                // add medicine records if exists
                if (CollectionUtils.isNotEmpty(diseaseOpt.get().getMedicines())) {
                    List<Medicine> medicineList = new ArrayList<>(diseaseOpt.get().getMedicines());
                    medicineList = getMedicineSlice(medicineList, medicineMaxRecords);
                    var medicines = medicineList.stream()
                            .map(x -> new DiagnosisVO(x.getCui(), x.getName(), null))
                            .collect(Collectors.toList());

                    disease.setMedicines(medicines);
                }
                diseases.add(disease);
            }
        });
        return diseases;
    }

    private static List<String> getSlice(List<String> list, int maxSize) {
        return list.size() > maxSize ? list.subList(0, maxSize) : list;
    }

    private static List<Medicine> getMedicineSlice(List<Medicine> list, int maxSize) {
        return list.size() > maxSize ? list.subList(0, maxSize) : list;
    }

    @SuppressWarnings("unchecked")
    private static DiagnosisResponse buildDiagnosisResponse(List<?> listOfList) {
        var diagnosis = new DiagnosisObject((List<DiagnosisVO>) listOfList.get(0), (List<DiagnosisVO>) listOfList.get(1),
                (List<DiagnosisVO>) listOfList.get(2));
        return new DiagnosisResponse(HttpResponseStatus.OK.reasonPhrase(), PROCESS_SUCCESS_MSG, diagnosis);
    }
}
