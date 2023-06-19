package org.acme.autobot.resource;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.core.MediaType;
import org.acme.autobot.response.BaseResponse;
import org.acme.autobot.response.SymptomResponse;
import org.acme.autobot.service.impl.SymptomServiceImpl;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.*;

@QuarkusTest
class SymptomResourceTest {

    private static final String SUCCESS_MSG = "Success";

    @InjectMock
    SymptomServiceImpl symptomService;

    @Test
    void findSymptomAll() {
        when(symptomService.findSymptomAll(anyInt(), anyInt(), anyString(), anyString()))
                .thenReturn(Uni.createFrom().item(new SymptomResponse(HttpResponseStatus.OK.reasonPhrase(),
                        SUCCESS_MSG, null)));
        given()
                .when().get("/symptom/all")
                .then()
                .statusCode(HttpResponseStatus.OK.code())
                .body(is(containsString(SUCCESS_MSG)));
    }

    @Test
    void findSymptomById() {
        when(symptomService.findSymptomById(anyLong()))
                .thenReturn(Uni.createFrom().item(new SymptomResponse(HttpResponseStatus.OK.reasonPhrase(),
                        SUCCESS_MSG, null)));
        given()
                .pathParams("id", "10000")
                .when().get("/symptom/{id}")
                .then()
                .statusCode(HttpResponseStatus.OK.code())
                .body(is(containsString(SUCCESS_MSG)));
    }

    @Test
    void findSymptomByCui() {
        when(symptomService.findSymptomByCui(anyString()))
                .thenReturn(Uni.createFrom().item(new SymptomResponse(HttpResponseStatus.OK.reasonPhrase(),
                        SUCCESS_MSG, null)));
        given()
                .pathParams("cui", "C00001")
                .when().get("/symptom/cui/{cui}")
                .then()
                .statusCode(HttpResponseStatus.OK.code())
                .body(is(containsString(SUCCESS_MSG)));
    }

    @Test
    void searchSymptomByName() {
        when(symptomService.searchSymptomByName(anyString(), anyInt(), anyInt()))
                .thenReturn(Uni.createFrom().item(new SymptomResponse(HttpResponseStatus.OK.reasonPhrase(),
                        SUCCESS_MSG, null)));
        given()
                .queryParam("name", "Fever")
                .when().get("/symptom/search")
                .then()
                .statusCode(HttpResponseStatus.OK.code())
                .body(is(containsString(SUCCESS_MSG)));
    }

    @Test
    void saveSymptom() {
        when(symptomService.saveSymptom(any()))
                .thenReturn(Uni.createFrom().item(new SymptomResponse(HttpResponseStatus.OK.reasonPhrase(),
                        SUCCESS_MSG, null)));
        given()
                .contentType(MediaType.APPLICATION_JSON)
                .when().post("/symptom/save")
                .then()
                .statusCode(HttpResponseStatus.OK.code())
                .body(is(containsString(SUCCESS_MSG)));
    }

    @Test
    void updateSymptom() {
        when(symptomService.updateSymptom(anyLong(), any()))
                .thenReturn(Uni.createFrom().item(new SymptomResponse(HttpResponseStatus.OK.reasonPhrase(),
                        SUCCESS_MSG, null)));
        given()
                .contentType(MediaType.APPLICATION_JSON)
                .pathParams("id", "10000")
                .when().put("/symptom/update/{id}")
                .then()
                .statusCode(HttpResponseStatus.OK.code())
                .body(is(containsString(SUCCESS_MSG)));
    }

    @Test
    void deleteSymptom() {
        when(symptomService.deleteSymptom(anyLong()))
                .thenReturn(Uni.createFrom().item(new BaseResponse(HttpResponseStatus.OK.reasonPhrase(),
                        SUCCESS_MSG)));
        given()
                .pathParams("id", "10000")
                .when().delete("/symptom/delete/{id}")
                .then()
                .statusCode(HttpResponseStatus.OK.code())
                .body(is(containsString(SUCCESS_MSG)));
    }
}