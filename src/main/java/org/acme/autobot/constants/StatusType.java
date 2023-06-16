package org.acme.autobot.constants;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import static org.acme.autobot.constants.MessagingConstants.STATUS_INVALID_ERROR_MSG;

/**
 * @author irfan.nagoo
 */

@Getter
public enum StatusType {

    ACTIVE("Active"),
    DELETED("Deleted");

    @JsonValue
    private final String value;

    StatusType(String value) {
        this.value = value;
    }

    StatusType getStatusType(String value) {
        for (StatusType type : StatusType.values()) {
            if (type.getValue().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException(STATUS_INVALID_ERROR_MSG);
    }

}
