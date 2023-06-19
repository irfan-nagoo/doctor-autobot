package org.acme.autobot.service.impl;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.quarkus.hibernate.reactive.panache.PanacheQuery;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.quarkus.test.vertx.RunOnVertxContext;
import io.quarkus.test.vertx.UniAsserter;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import org.acme.autobot.constants.StatusType;
import org.acme.autobot.domain.DiseaseObject;
import org.acme.autobot.entity.Disease;
import org.acme.autobot.exception.RecordNotFoundException;
import org.acme.autobot.repository.DiseaseRepository;
import org.acme.autobot.request.DiseaseRequest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@QuarkusTest
@RunOnVertxContext
@SuppressWarnings("unchecked")
class DiseaseServiceImplTest {

    @InjectMock
    DiseaseRepository diseaseRepository;

    @Inject
    DiseaseServiceImpl diseaseService;

    @Test
    void findDiseaseAll(UniAsserter uniAsserter) {
        var disease = new Disease();
        disease.setCui("C100000");
        disease.setStatus("ACTIVE");
        var panacheQuery = mock(PanacheQuery.class);
        uniAsserter
                .execute(() -> when(panacheQuery.page(any())).thenReturn(panacheQuery))
                .execute(() -> when(panacheQuery.list()).thenReturn(Uni.createFrom().item(List.of(disease))))
                .execute(() -> when(diseaseRepository.findAll(any())).thenReturn(panacheQuery))
                .assertThat(() -> diseaseService.findDiseaseAll(0, 10, "name", "asc"),
                        response -> {
                            assertEquals(1, response.getDiseases().size());
                            assertEquals("C100000", response.getDiseases().get(0).getCui());
                            assertEquals("Active", response.getDiseases().get(0).getStatus().getValue());
                        });
    }

    @Test
    void findDiseaseAll_Exception(UniAsserter uniAsserter) {
        var panacheQuery = mock(PanacheQuery.class);
        uniAsserter
                .execute(() -> when(panacheQuery.page(any())).thenReturn(panacheQuery))
                .execute(() -> when(panacheQuery.list()).thenReturn(Uni.createFrom().nullItem()))
                .execute(() -> when(diseaseRepository.findAll(any())).thenReturn(panacheQuery))
                .assertFailedWith(() -> diseaseService.findDiseaseAll(0, 10, "name", "asc"),
                        RecordNotFoundException.class);
    }

    @Test
    void findDiseaseById(UniAsserter uniAsserter) {
        var disease = new Disease();
        disease.setCui("C100000");
        disease.setStatus("ACTIVE");
        uniAsserter
                .execute(() -> when(diseaseRepository.findById(anyLong())).thenReturn(Uni.createFrom().item(disease)))
                .assertThat(() -> diseaseService.findDiseaseById(1000L),
                        response -> {
                            assertEquals(1, response.getDiseases().size());
                            assertEquals("C100000", response.getDiseases().get(0).getCui());
                            assertEquals("Active", response.getDiseases().get(0).getStatus().getValue());
                        });
    }

    @Test
    void findDiseaseByCui(UniAsserter uniAsserter) {
        var disease = new Disease();
        disease.setCui("C100000");
        disease.setStatus("ACTIVE");
        uniAsserter
                .execute(() -> when(diseaseRepository.findByCui(anyString())).thenReturn(Uni.createFrom().item(disease)))
                .assertThat(() -> diseaseService.findDiseaseByCui("C100000"),
                        response -> {
                            assertEquals(1, response.getDiseases().size());
                            assertEquals("C100000", response.getDiseases().get(0).getCui());
                            assertEquals("Active", response.getDiseases().get(0).getStatus().getValue());
                        });
    }

    @Test
    void searchDiseaseByName(UniAsserter uniAsserter) {
        var disease = new Disease();
        disease.setCui("C100000");
        disease.setName("quarkus");
        disease.setStatus("ACTIVE");
        var panacheQuery = mock(PanacheQuery.class);
        uniAsserter
                .execute(() -> when(panacheQuery.page(any())).thenReturn(panacheQuery))
                .execute(() -> when(panacheQuery.list()).thenReturn(Uni.createFrom().item(List.of(disease))))
                .execute(() -> when(diseaseRepository.searchByName(anyString())).thenReturn(panacheQuery))
                .assertThat(() -> diseaseService.searchDiseaseByName("quarkus", 0, 10),
                        response -> {
                            assertEquals(1, response.getDiseases().size());
                            assertEquals("C100000", response.getDiseases().get(0).getCui());
                            assertEquals("Active", response.getDiseases().get(0).getStatus().getValue());
                        });
    }

    @Test
    void saveDisease(UniAsserter uniAsserter) {
        var request = new DiseaseRequest(new DiseaseObject());
        var disease = new Disease();
        disease.setCui("C100000");
        disease.setStatus("ACTIVE");
        uniAsserter
                .execute(() -> when(diseaseRepository.persist(any(Disease.class))).thenReturn(Uni.createFrom().item(disease)))
                .assertThat(() -> diseaseService.saveDisease(request),
                        response -> {
                            assertEquals(1, response.getDiseases().size());
                            assertEquals("C100000", response.getDiseases().get(0).getCui());
                            assertEquals("Active", response.getDiseases().get(0).getStatus().getValue());
                        });
    }

    @Test
    void updateDisease(UniAsserter uniAsserter) {
        var diseaseObject = new DiseaseObject();
        diseaseObject.setCui("C100000");
        diseaseObject.setStatus(StatusType.ACTIVE);
        var request = new DiseaseRequest(diseaseObject);
        var disease = new Disease();
        disease.setCui("C100000");
        uniAsserter
                .execute(() -> when(diseaseRepository.findById(anyLong())).thenReturn(Uni.createFrom().item(disease)))
                .assertThat(() -> diseaseService.updateDisease(1000L, request),
                        response -> {
                            assertEquals(1, response.getDiseases().size());
                            assertEquals("C100000", response.getDiseases().get(0).getCui());
                            assertEquals("Active", response.getDiseases().get(0).getStatus().getValue());
                        });
    }

    @Test
    void deleteDisease(UniAsserter uniAsserter) {
        var disease = new Disease();
        disease.setCui("C100000");
        disease.setStatus("ACTIVE");
        uniAsserter
                .execute(() -> when(diseaseRepository.findById(anyLong())).thenReturn(Uni.createFrom().item(disease)))
                .assertThat(() -> diseaseService.deleteDisease(1000L),
                        response -> assertEquals(HttpResponseStatus.OK.reasonPhrase(), response.getStatus()));
    }
}