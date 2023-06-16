package org.acme.autobot.response;

import lombok.Getter;
import lombok.Setter;
import org.acme.autobot.domain.DiagnosisObject;

/**
 * @author irfan.nagoo
 */

@Getter
@Setter
public class DiagnosisResponse extends BaseResponse {

    private final DiagnosisObject diagnosis;

    public DiagnosisResponse(String status, String message, DiagnosisObject diagnosis) {
        super(status, message);
        this.diagnosis = diagnosis;
    }
}
