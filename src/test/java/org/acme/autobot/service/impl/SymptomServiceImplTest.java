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
import org.acme.autobot.domain.SymptomObject;
import org.acme.autobot.entity.Symptom;
import org.acme.autobot.exception.RecordNotFoundException;
import org.acme.autobot.repository.SymptomRepository;
import org.acme.autobot.request.SymptomRequest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@QuarkusTest
@RunOnVertxContext
@SuppressWarnings("unchecked")
class SymptomServiceImplTest {

    @InjectMock
    SymptomRepository symptomRepository;

    @Inject
    SymptomServiceImpl symptomService;

    @Test
    void findSymptomAll(UniAsserter uniAsserter) {
        var symptom = new Symptom();
        symptom.setCui("C100000");
        symptom.setStatus("ACTIVE");
        var panacheQuery = mock(PanacheQuery.class);
        uniAsserter
                .execute(() -> when(panacheQuery.page(any())).thenReturn(panacheQuery))
                .execute(() -> when(panacheQuery.list()).thenReturn(Uni.createFrom().item(List.of(symptom))))
                .execute(() -> when(symptomRepository.findAll(any())).thenReturn(panacheQuery))
                .assertThat(() -> symptomService.findSymptomAll(0, 10, "name", "asc"),
                        response -> {
                            assertEquals(1, response.getSymptoms().size());
                            assertEquals("C100000", response.getSymptoms().get(0).getCui());
                            assertEquals("Active", response.getSymptoms().get(0).getStatus().getValue());
                        });
    }

    @Test
    void findSymptomAll_Exception(UniAsserter uniAsserter) {
        var panacheQuery = mock(PanacheQuery.class);
        uniAsserter
                .execute(() -> when(panacheQuery.page(any())).thenReturn(panacheQuery))
                .execute(() -> when(panacheQuery.list()).thenReturn(Uni.createFrom().nullItem()))
                .execute(() -> when(symptomRepository.findAll(any())).thenReturn(panacheQuery))
                .assertFailedWith(() -> symptomService.findSymptomAll(0, 10, "name", "asc"),
                        RecordNotFoundException.class);
    }

    @Test
    void findSymptomById(UniAsserter uniAsserter) {
        var symptom = new Symptom();
        symptom.setCui("C100000");
        symptom.setStatus("ACTIVE");
        uniAsserter
                .execute(() -> when(symptomRepository.findById(anyLong())).thenReturn(Uni.createFrom().item(symptom)))
                .assertThat(() -> symptomService.findSymptomById(1000L),
                        response -> {
                            assertEquals(1, response.getSymptoms().size());
                            assertEquals("C100000", response.getSymptoms().get(0).getCui());
                            assertEquals("Active", response.getSymptoms().get(0).getStatus().getValue());
                        });
    }

    @Test
    void findSymptomByCui(UniAsserter uniAsserter) {
        var symptom = new Symptom();
        symptom.setCui("C100000");
        symptom.setStatus("ACTIVE");
        uniAsserter
                .execute(() -> when(symptomRepository.findByCui(anyString())).thenReturn(Uni.createFrom().item(symptom)))
                .assertThat(() -> symptomService.findSymptomByCui("C100000"),
                        response -> {
                            assertEquals(1, response.getSymptoms().size());
                            assertEquals("C100000", response.getSymptoms().get(0).getCui());
                            assertEquals("Active", response.getSymptoms().get(0).getStatus().getValue());
                        });
    }

    @Test
    void searchSymptomByName(UniAsserter uniAsserter) {
        var symptom = new Symptom();
        symptom.setCui("C100000");
        symptom.setName("quarkus");
        symptom.setStatus("ACTIVE");
        var panacheQuery = mock(PanacheQuery.class);
        uniAsserter
                .execute(() -> when(panacheQuery.page(any())).thenReturn(panacheQuery))
                .execute(() -> when(panacheQuery.list()).thenReturn(Uni.createFrom().item(List.of(symptom))))
                .execute(() -> when(symptomRepository.searchByName(anyString())).thenReturn(panacheQuery))
                .assertThat(() -> symptomService.searchSymptomByName("quarkus", 0, 10),
                        response -> {
                            assertEquals(1, response.getSymptoms().size());
                            assertEquals("C100000", response.getSymptoms().get(0).getCui());
                            assertEquals("Active", response.getSymptoms().get(0).getStatus().getValue());
                        });
    }

    @Test
    void saveSymptom(UniAsserter uniAsserter) {
        var request = new SymptomRequest(new SymptomObject());
        var symptom = new Symptom();
        symptom.setCui("C100000");
        symptom.setStatus("ACTIVE");
        uniAsserter
                .execute(() -> when(symptomRepository.persist(any(Symptom.class))).thenReturn(Uni.createFrom().item(symptom)))
                .assertThat(() -> symptomService.saveSymptom(request),
                        response -> {
                            assertEquals(1, response.getSymptoms().size());
                            assertEquals("C100000", response.getSymptoms().get(0).getCui());
                            assertEquals("Active", response.getSymptoms().get(0).getStatus().getValue());
                        });
    }

    @Test
    void updateSymptom(UniAsserter uniAsserter) {
        var symptomObject = new SymptomObject();
        symptomObject.setCui("C100000");
        symptomObject.setStatus(StatusType.ACTIVE);
        var request = new SymptomRequest(symptomObject);
        uniAsserter
                .execute(() -> when(symptomRepository.findById(anyLong())).thenReturn(Uni.createFrom().item(new Symptom())))
                .assertThat(() -> symptomService.updateSymptom(1000L, request),
                        response -> {
                            assertEquals(1, response.getSymptoms().size());
                            assertEquals("C100000", response.getSymptoms().get(0).getCui());
                            assertEquals("Active", response.getSymptoms().get(0).getStatus().getValue());
                        });
    }

    @Test
    void deleteSymptom(UniAsserter uniAsserter) {
        var symptom = new Symptom();
        symptom.setCui("C100000");
        symptom.setStatus("ACTIVE");
        uniAsserter
                .execute(() -> when(symptomRepository.findById(anyLong())).thenReturn(Uni.createFrom().item(symptom)))
                .assertThat(() -> symptomService.deleteSymptom(1000L),
                        response -> assertEquals(HttpResponseStatus.OK.reasonPhrase(), response.getStatus()));
    }
}