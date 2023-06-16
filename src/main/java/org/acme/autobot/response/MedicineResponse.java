package org.acme.autobot.response;

import lombok.Getter;
import org.acme.autobot.domain.MedicineObject;

import java.util.List;

/**
 * @author irfan.nagoo
 */

@Getter
public class MedicineResponse extends BaseResponse {

    private final List<MedicineObject> medicines;

    public MedicineResponse(String status, String message, List<MedicineObject> medicines) {
        super(status, message);
        this.medicines = medicines;
    }
}
