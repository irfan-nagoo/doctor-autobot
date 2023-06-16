package org.acme.autobot.response;

import lombok.Getter;
import org.acme.autobot.domain.DiseaseObject;

import java.util.List;

/**
 * @author irfan.nagoo
 */

@Getter
public class DiseaseResponse extends BaseResponse {

    private final List<DiseaseObject> diseases;

    public DiseaseResponse(String status, String message, List<DiseaseObject> diseases) {
        super(status, message);
        this.diseases = diseases;
    }
}
