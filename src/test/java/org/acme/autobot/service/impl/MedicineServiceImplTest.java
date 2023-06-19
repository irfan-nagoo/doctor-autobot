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
import org.acme.autobot.domain.MedicineObject;
import org.acme.autobot.entity.Medicine;
import org.acme.autobot.exception.RecordNotFoundException;
import org.acme.autobot.repository.MedicineRepository;
import org.acme.autobot.request.MedicineRequest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@QuarkusTest
@RunOnVertxContext
@SuppressWarnings("unchecked")
class MedicineServiceImplTest {
    
    @InjectMock
    MedicineRepository medicineRepository;

    @Inject
    MedicineServiceImpl medicineService;
    
    @Test
    void findMedicineAll(UniAsserter uniAsserter) {
        var medicine = new Medicine();
        medicine.setCui("C100000");
        medicine.setStatus("ACTIVE");
        var panacheQuery = mock(PanacheQuery.class);
        uniAsserter
                .execute(() -> when(panacheQuery.page(any())).thenReturn(panacheQuery))
                .execute(() -> when(panacheQuery.list()).thenReturn(Uni.createFrom().item(List.of(medicine))))
                .execute(() -> when(medicineRepository.findAll(any())).thenReturn(panacheQuery))
                .assertThat(() -> medicineService.findMedicineAll(0, 10, "name", "asc"),
                        response -> {
                            assertEquals(1, response.getMedicines().size());
                            assertEquals("C100000", response.getMedicines().get(0).getCui());
                            assertEquals("Active", response.getMedicines().get(0).getStatus().getValue());
                        });
    }
    
    @Test
    void findMedicineAll_Exception(UniAsserter uniAsserter) {
        var panacheQuery = mock(PanacheQuery.class);
        uniAsserter
                .execute(() -> when(panacheQuery.page(any())).thenReturn(panacheQuery))
                .execute(() -> when(panacheQuery.list()).thenReturn(Uni.createFrom().nullItem()))
                .execute(() -> when(medicineRepository.findAll(any())).thenReturn(panacheQuery))
                .assertFailedWith(() -> medicineService.findMedicineAll(0, 10, "name", "asc"),
                        RecordNotFoundException.class);
    }

    @Test
    void findMedicineById(UniAsserter uniAsserter) {
        var medicine = new Medicine();
        medicine.setCui("C100000");
        medicine.setStatus("ACTIVE");
        uniAsserter
                .execute(() -> when(medicineRepository.findById(anyLong())).thenReturn(Uni.createFrom().item(medicine)))
                .assertThat(() -> medicineService.findMedicineById(1000L),
                        response -> {
                            assertEquals(1, response.getMedicines().size());
                            assertEquals("C100000", response.getMedicines().get(0).getCui());
                            assertEquals("Active", response.getMedicines().get(0).getStatus().getValue());
                        });
    }

    @Test
    void findMedicineByCui(UniAsserter uniAsserter) {
        var medicine = new Medicine();
        medicine.setCui("C100000");
        medicine.setStatus("ACTIVE");
        uniAsserter
                .execute(() -> when(medicineRepository.findByCui(anyString())).thenReturn(Uni.createFrom().item(medicine)))
                .assertThat(() -> medicineService.findMedicineByCui("C100000"),
                        response -> {
                            assertEquals(1, response.getMedicines().size());
                            assertEquals("C100000", response.getMedicines().get(0).getCui());
                            assertEquals("Active", response.getMedicines().get(0).getStatus().getValue());
                        });
    }

    @Test
    void searchMedicineByName(UniAsserter uniAsserter) {
        var medicine = new Medicine();
        medicine.setCui("C100000");
        medicine.setName("quarkus");
        medicine.setStatus("ACTIVE");
        var panacheQuery = mock(PanacheQuery.class);
        uniAsserter
                .execute(() -> when(panacheQuery.page(any())).thenReturn(panacheQuery))
                .execute(() -> when(panacheQuery.list()).thenReturn(Uni.createFrom().item(List.of(medicine))))
                .execute(() -> when(medicineRepository.searchByName(anyString())).thenReturn(panacheQuery))
                .assertThat(() -> medicineService.searchMedicineByName("quarkus", 0, 10),
                        response -> {
                            assertEquals(1, response.getMedicines().size());
                            assertEquals("C100000", response.getMedicines().get(0).getCui());
                            assertEquals("Active", response.getMedicines().get(0).getStatus().getValue());
                        });
    }

    @Test
    void saveMedicine(UniAsserter uniAsserter) {
        var request = new MedicineRequest(new MedicineObject());
        var medicine = new Medicine();
        medicine.setCui("C100000");
        medicine.setStatus("ACTIVE");
        uniAsserter
                .execute(() -> when(medicineRepository.persist(any(Medicine.class))).thenReturn(Uni.createFrom().item(medicine)))
                .assertThat(() -> medicineService.saveMedicine(request),
                        response -> {
                            assertEquals(1, response.getMedicines().size());
                            assertEquals("C100000", response.getMedicines().get(0).getCui());
                            assertEquals("Active", response.getMedicines().get(0).getStatus().getValue());
                        });
    }

    @Test
    void updateMedicine(UniAsserter uniAsserter) {
        var medicineObject = new MedicineObject();
        medicineObject.setCui("C100000");
        medicineObject.setStatus(StatusType.ACTIVE);
        var request = new MedicineRequest(medicineObject);
        uniAsserter
                .execute(() -> when(medicineRepository.findById(anyLong())).thenReturn(Uni.createFrom().item(new Medicine())))
                .assertThat(() -> medicineService.updateMedicine(1000L, request),
                        response -> {
                            assertEquals(1, response.getMedicines().size());
                            assertEquals("C100000", response.getMedicines().get(0).getCui());
                            assertEquals("Active", response.getMedicines().get(0).getStatus().getValue());
                        });
    }

    @Test
    void deleteMedicine(UniAsserter uniAsserter) {
        var medicine = new Medicine();
        medicine.setCui("C100000");
        medicine.setStatus("ACTIVE");
        uniAsserter
                .execute(() -> when(medicineRepository.findById(anyLong())).thenReturn(Uni.createFrom().item(medicine)))
                .assertThat(() -> medicineService.deleteMedicine(1000L),
                        response -> assertEquals(HttpResponseStatus.OK.reasonPhrase(), response.getStatus()));
    }
}