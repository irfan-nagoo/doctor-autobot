package org.acme.autobot.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author irfan.nagoo
 */

@Getter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ErrorResponse extends BaseResponse {

    private final String errorId;
    private final List<ValidationError> errors = new ArrayList<>();

    public ErrorResponse(String status, String message, String errorId) {
        super(status, message);
        this.errorId = errorId;
    }

    @RequiredArgsConstructor
    @Getter
    public static final class ValidationError {
        private final String field;
        private final String message;
    }

}
