package org.acme.autobot.service;

import io.smallrye.mutiny.Uni;
import org.acme.autobot.request.DiagnosisRequest;
import org.acme.autobot.response.DiagnosisResponse;

/**
 * @author irfan.nagoo
 */
public interface DiagnosisService {

    Uni<DiagnosisResponse> preliminaryDiagnosis(DiagnosisRequest diagnosisRequest);
}
