package org.acme.autobot.resource;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.core.MediaType;
import org.acme.autobot.response.BaseResponse;
import org.acme.autobot.response.DiseaseResponse;
import org.acme.autobot.service.impl.DiseaseServiceImpl;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@QuarkusTest
class DiseaseResourceTest {

    private static final String SUCCESS_MSG = "Success";

    @InjectMock
    DiseaseServiceImpl diseaseService;

    @Test
    void findDiseaseAll() {
        when(diseaseService.findDiseaseAll(anyInt(), anyInt(), anyString(), anyString()))
                .thenReturn(Uni.createFrom().item(new DiseaseResponse(HttpResponseStatus.OK.reasonPhrase(),
                        SUCCESS_MSG, null)));
        given()
                .when().get("/disease/all")
                .then()
                .statusCode(HttpResponseStatus.OK.code())
                .body(is(containsString(SUCCESS_MSG)));
    }

    @Test
    void findDiseaseById() {
        when(diseaseService.findDiseaseById(anyLong()))
                .thenReturn(Uni.createFrom().item(new DiseaseResponse(HttpResponseStatus.OK.reasonPhrase(),
                        SUCCESS_MSG, null)));
        given()
                .pathParams("id", "10000")
                .when().get("/disease/{id}")
                .then()
                .statusCode(HttpResponseStatus.OK.code())
                .body(is(containsString(SUCCESS_MSG)));
    }

    @Test
    void findDiseaseByCui() {
        when(diseaseService.findDiseaseByCui(anyString()))
                .thenReturn(Uni.createFrom().item(new DiseaseResponse(HttpResponseStatus.OK.reasonPhrase(),
                        SUCCESS_MSG, null)));
        given()
                .pathParams("cui", "C00001")
                .when().get("/disease/cui/{cui}")
                .then()
                .statusCode(HttpResponseStatus.OK.code())
                .body(is(containsString(SUCCESS_MSG)));
    }

    @Test
    void searchDiseaseByName() {
        when(diseaseService.searchDiseaseByName(anyString(), anyInt(), anyInt()))
                .thenReturn(Uni.createFrom().item(new DiseaseResponse(HttpResponseStatus.OK.reasonPhrase(),
                        SUCCESS_MSG, null)));
        given()
                .queryParam("name", "Fever")
                .when().get("/disease/search")
                .then()
                .statusCode(HttpResponseStatus.OK.code())
                .body(is(containsString(SUCCESS_MSG)));
    }

    @Test
    void saveDisease() {
        when(diseaseService.saveDisease(any()))
                .thenReturn(Uni.createFrom().item(new DiseaseResponse(HttpResponseStatus.OK.reasonPhrase(),
                        SUCCESS_MSG, null)));
        given()
                .contentType(MediaType.APPLICATION_JSON)
                .when().post("/disease/save")
                .then()
                .statusCode(HttpResponseStatus.OK.code())
                .body(is(containsString(SUCCESS_MSG)));
    }

    @Test
    void updateDisease() {
        when(diseaseService.updateDisease(anyLong(), any()))
                .thenReturn(Uni.createFrom().item(new DiseaseResponse(HttpResponseStatus.OK.reasonPhrase(),
                        SUCCESS_MSG, null)));
        given()
                .contentType(MediaType.APPLICATION_JSON)
                .pathParams("id", "10000")
                .when().put("/disease/update/{id}")
                .then()
                .statusCode(HttpResponseStatus.OK.code())
                .body(is(containsString(SUCCESS_MSG)));
    }

    @Test
    void deleteDisease() {
        when(diseaseService.deleteDisease(anyLong()))
                .thenReturn(Uni.createFrom().item(new BaseResponse(HttpResponseStatus.OK.reasonPhrase(),
                        SUCCESS_MSG)));
        given()
                .pathParams("id", "10000")
                .when().delete("/disease/delete/{id}")
                .then()
                .statusCode(HttpResponseStatus.OK.code())
                .body(is(containsString(SUCCESS_MSG)));
    }
}