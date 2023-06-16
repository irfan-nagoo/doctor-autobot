package org.acme.autobot.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.acme.autobot.domain.MedicineObject;
import org.acme.autobot.domain.SymptomObject;

import java.util.List;

/**
 * @author irfan.nagoo
 */

@Getter
public class SymptomResponse extends BaseResponse {

    private final List<SymptomObject> symptoms;

    public SymptomResponse(String status, String message, List<SymptomObject> symptoms) {
        super(status, message);
        this.symptoms = symptoms;
    }

}
