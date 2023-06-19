package org.acme.autobot.resource;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.restassured.mapper.ObjectMapperType;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.core.MediaType;
import org.acme.autobot.request.DiagnosisRequest;
import org.acme.autobot.response.DiagnosisResponse;
import org.acme.autobot.service.impl.DiagnosisServiceImpl;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@QuarkusTest
class DiagnosisResourceTest {

    private static final String SUCCESS_MSG = "Success";

    @InjectMock
    DiagnosisServiceImpl diagnosisService;

    @Test
    void preliminaryDiagnosis() {
        when(diagnosisService.preliminaryDiagnosis(any(DiagnosisRequest.class)))
                .thenReturn(Uni.createFrom().item(new DiagnosisResponse(HttpResponseStatus.OK.reasonPhrase(),
                        SUCCESS_MSG, null)));
        var request = new DiagnosisRequest();
        request.setHaveChill(true);
        request.setHaveCough(true);
        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(request, ObjectMapperType.JACKSON_2)
                .when().post("/diagnosis/evaluate")
                .then()
                .statusCode(HttpResponseStatus.OK.code())
                .body(is(containsString(SUCCESS_MSG)));
    }
}