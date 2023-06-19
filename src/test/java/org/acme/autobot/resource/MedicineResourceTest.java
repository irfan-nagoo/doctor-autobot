package org.acme.autobot.resource;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.core.MediaType;
import org.acme.autobot.response.BaseResponse;
import org.acme.autobot.response.MedicineResponse;
import org.acme.autobot.service.impl.MedicineServiceImpl;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@QuarkusTest
class MedicineResourceTest {

    private static final String SUCCESS_MSG = "Success";

    @InjectMock
    MedicineServiceImpl medicineService;

    @Test
    void findMedicineAll() {
        when(medicineService.findMedicineAll(anyInt(), anyInt(), anyString(), anyString()))
                .thenReturn(Uni.createFrom().item(new MedicineResponse(HttpResponseStatus.OK.reasonPhrase(),
                        SUCCESS_MSG, null)));
        given()
                .when().get("/medicine/all")
                .then()
                .statusCode(HttpResponseStatus.OK.code())
                .body(is(containsString(SUCCESS_MSG)));
    }

    @Test
    void findMedicineById() {
        when(medicineService.findMedicineById(anyLong()))
                .thenReturn(Uni.createFrom().item(new MedicineResponse(HttpResponseStatus.OK.reasonPhrase(),
                        SUCCESS_MSG, null)));
        given()
                .pathParams("id", "10000")
                .when().get("/medicine/{id}")
                .then()
                .statusCode(HttpResponseStatus.OK.code())
                .body(is(containsString(SUCCESS_MSG)));
    }

    @Test
    void findMedicineByCui() {
        when(medicineService.findMedicineByCui(anyString()))
                .thenReturn(Uni.createFrom().item(new MedicineResponse(HttpResponseStatus.OK.reasonPhrase(),
                        SUCCESS_MSG, null)));
        given()
                .pathParams("cui", "C00001")
                .when().get("/medicine/cui/{cui}")
                .then()
                .statusCode(HttpResponseStatus.OK.code())
                .body(is(containsString(SUCCESS_MSG)));
    }

    @Test
    void searchMedicineByName() {
        when(medicineService.searchMedicineByName(anyString(), anyInt(), anyInt()))
                .thenReturn(Uni.createFrom().item(new MedicineResponse(HttpResponseStatus.OK.reasonPhrase(),
                        SUCCESS_MSG, null)));
        given()
                .queryParam("name", "Fever")
                .when().get("/medicine/search")
                .then()
                .statusCode(HttpResponseStatus.OK.code())
                .body(is(containsString(SUCCESS_MSG)));
    }

    @Test
    void saveMedicine() {
        when(medicineService.saveMedicine(any()))
                .thenReturn(Uni.createFrom().item(new MedicineResponse(HttpResponseStatus.OK.reasonPhrase(),
                        SUCCESS_MSG, null)));
        given()
                .contentType(MediaType.APPLICATION_JSON)
                .when().post("/medicine/save")
                .then()
                .statusCode(HttpResponseStatus.OK.code())
                .body(is(containsString(SUCCESS_MSG)));
    }

    @Test
    void updateMedicine() {
        when(medicineService.updateMedicine(anyLong(), any()))
                .thenReturn(Uni.createFrom().item(new MedicineResponse(HttpResponseStatus.OK.reasonPhrase(),
                        SUCCESS_MSG, null)));
        given()
                .contentType(MediaType.APPLICATION_JSON)
                .pathParams("id", "10000")
                .when().put("/medicine/update/{id}")
                .then()
                .statusCode(HttpResponseStatus.OK.code())
                .body(is(containsString(SUCCESS_MSG)));
    }

    @Test
    void deleteMedicine() {
        when(medicineService.deleteMedicine(anyLong()))
                .thenReturn(Uni.createFrom().item(new BaseResponse(HttpResponseStatus.OK.reasonPhrase(),
                        SUCCESS_MSG)));
        given()
                .pathParams("id", "10000")
                .when().delete("/medicine/delete/{id}")
                .then()
                .statusCode(HttpResponseStatus.OK.code())
                .body(is(containsString(SUCCESS_MSG)));
    }
}