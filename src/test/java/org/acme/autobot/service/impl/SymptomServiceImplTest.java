package org.acme.autobot.service.impl;

import io.quarkus.hibernate.reactive.panache.PanacheQuery;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.quarkus.test.vertx.RunOnVertxContext;
import io.quarkus.test.vertx.UniAsserter;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import org.acme.autobot.entity.Symptom;
import org.acme.autobot.repository.SymptomRepository;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@QuarkusTest
@SuppressWarnings("unchecked")
class SymptomServiceImplTest {

    @InjectMock
    SymptomRepository symptomRepository;

    @Inject
    SymptomServiceImpl symptomService;

    @RunOnVertxContext
    @Test
    void findSymptomAll(UniAsserter uniAsserter) {
        /*var symptom = new Symptom();
        symptom.setCui("C100000");
        symptom.setStatus("ACTIVE");
        var panacheQuery = mock(PanacheQuery.class);
        when(panacheQuery.page(any())).thenReturn(panacheQuery);
        when(panacheQuery.list()).thenReturn(Uni.createFrom().item(List.of(symptom)));
        uniAsserter.execute(() -> when(symptomRepository.findAll(any())).thenReturn(panacheQuery));
        uniAsserter.assertThat(() -> symptomService.findSymptomAll(0, 10, "name", "asc"),
                response -> {
                    assertEquals(1, response.getSymptoms().size());
                    assertEquals("C100000", response.getSymptoms().get(0).getCui());
                    assertEquals("Active", response.getSymptoms().get(0).getStatus().getValue());
                });*/
    }

    @Test
    void findSymptomById() {
    }

    @Test
    void findSymptomByCui() {
    }

    @Test
    void searchSymptomByName() {
    }

    @Test
    void saveSymptom() {
    }

    @Test
    void updateSymptom() {
    }

    @Test
    void deleteSymptom() {
    }
}