package org.acme.autobot.service.impl;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.quarkus.test.vertx.RunOnVertxContext;
import io.quarkus.test.vertx.UniAsserter;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import org.acme.autobot.analyzer.DiseaseAnalyzer;
import org.acme.autobot.entity.Disease;
import org.acme.autobot.entity.Medicine;
import org.acme.autobot.entity.Symptom;
import org.acme.autobot.exception.RecordNotFoundException;
import org.acme.autobot.repository.DiseaseRepository;
import org.acme.autobot.repository.SymptomRepository;
import org.acme.autobot.request.DiagnosisRequest;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@QuarkusTest
@RunOnVertxContext
class DiagnosisServiceImplTest {

    @InjectMock
    DiseaseRepository diseaseRepository;

    @InjectMock
    SymptomRepository symptomRepository;

    @InjectMock
    DiseaseAnalyzer diseaseAnalyzer;

    @Inject
    DiagnosisServiceImpl diagnosisService;

    @Test
    void preliminaryDiagnosis(UniAsserter uniAsserter) {
        var diagnosisRequest = new DiagnosisRequest();
        diagnosisRequest.setFeelingTired(true);
        var symptom = new Symptom();
        symptom.setCui("C0015672");
        symptom.setStatus("ACTIVE");
        var minDisease = new Disease();
        minDisease.setCui("C0015672");
        minDisease.setStatus("ACTIVE");
        uniAsserter
                .execute(() -> when(symptomRepository.findByCuiList(any())).thenReturn(Uni.createFrom().item(List.of(symptom))))
                .execute(() -> when(diseaseRepository.findByCuiList(List.of("C0015672")))
                        .thenReturn(Uni.createFrom().item(List.of(minDisease))))
                .execute(() -> when(diseaseAnalyzer.analyze(any())).thenReturn(List.of("C100002")))
                .assertThat(() -> diagnosisService.preliminaryDiagnosis(diagnosisRequest),
                        response -> {
                            assertEquals(1, response.getDiagnosis().getSymptoms().size());
                            assertEquals("C0015672", response.getDiagnosis().getSymptoms().get(0).getCui());
                            assertEquals(1, response.getDiagnosis().getMinorDiseases().size());
                            assertEquals("C0015672", response.getDiagnosis().getMinorDiseases().get(0).getCui());
                            assertNull(response.getDiagnosis().getMajorDiseases());
                        });
    }

    @Test
    void preliminaryDiagnosis_WithMajorDisease(UniAsserter uniAsserter) {
        var diagnosisRequest = new DiagnosisRequest();
        diagnosisRequest.setFeelingTired(true);
        diagnosisRequest.setHaveChill(true);
        diagnosisRequest.setHaveFever(true);
        var symptom = new Symptom();
        symptom.setCui("C0015967");
        symptom.setStatus("ACTIVE");
        var minDisease = new Disease();
        minDisease.setCui("C0015967");
        minDisease.setStatus("ACTIVE");
        var majDisease = new Disease();
        majDisease.setCui("C100002");
        majDisease.setStatus("ACTIVE");
        var medicine = new Medicine();
        medicine.setCui("C100003");
        medicine.setStatus("ACTIVE");
        majDisease.setMedicines(Set.of(medicine));
        uniAsserter
                .execute(() -> when(symptomRepository.findByCuiList(any())).thenReturn(Uni.createFrom().item(List.of(symptom))))
                .execute(() -> when(diseaseRepository.findByCuiList(List.of("C0015967", "C0085593", "C0015672")))
                        .thenReturn(Uni.createFrom().item(List.of(minDisease))))
                .execute(() -> when(diseaseAnalyzer.analyze(any())).thenReturn(List.of("C100002")))
                .execute(() -> when(diseaseRepository.findByCuiList(List.of("C100002")))
                        .thenReturn(Uni.createFrom().item(List.of(majDisease))))
                .assertThat(() -> diagnosisService.preliminaryDiagnosis(diagnosisRequest),
                        response -> {
                            assertEquals(1, response.getDiagnosis().getSymptoms().size());
                            assertEquals("C0015967", response.getDiagnosis().getSymptoms().get(0).getCui());
                            assertEquals(1, response.getDiagnosis().getMinorDiseases().size());
                            assertEquals("C0015967", response.getDiagnosis().getMinorDiseases().get(0).getCui());
                            assertEquals(1, response.getDiagnosis().getMajorDiseases().size());
                            assertEquals("C100002", response.getDiagnosis().getMajorDiseases().get(0).getCui());
                            assertEquals(1, response.getDiagnosis().getMajorDiseases().get(0).getMedicines().size());
                            assertEquals("C100003", response.getDiagnosis().getMajorDiseases().get(0).getMedicines().get(0).getCui());
                        });
    }

    @Test
    void preliminaryDiagnosis_Exception(UniAsserter uniAsserter) {
        uniAsserter
                .assertFailedWith(() -> diagnosisService.preliminaryDiagnosis(new DiagnosisRequest()),
                        RecordNotFoundException.class);
    }
}