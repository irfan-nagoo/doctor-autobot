package org.acme.autobot.resource;

import io.smallrye.mutiny.Uni;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import lombok.RequiredArgsConstructor;
import org.acme.autobot.request.DiagnosisRequest;
import org.acme.autobot.response.DiagnosisResponse;
import org.acme.autobot.service.DiagnosisService;

@Path("/diagnosis")
@RequiredArgsConstructor
public class DiagnosisResource {

    private final DiagnosisService diagnosisService;

    /**
     * This API accepts the set of Symptoms as input, analyzes the Symptoms using
     * Artificial Intelligence (ML) component and produces a diagnosis report including
     * probable diseases and the medicine used for to treat each disease. This API retrieve
     * data from the database and hence some results depend on the availability of relevant data
     * in the database.
     *
     * @param diagnosisRequest Symptoms
     * @return Diagnosis report
     */
    @POST
    @Path("/evaluate")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<DiagnosisResponse> preliminaryDiagnosis(@Valid @NotNull DiagnosisRequest diagnosisRequest) {
        return diagnosisService.preliminaryDiagnosis(diagnosisRequest);
    }

}
