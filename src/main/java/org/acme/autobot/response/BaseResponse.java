package org.acme.autobot.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author irfan.nagoo
 */

@RequiredArgsConstructor
@Getter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class BaseResponse {

    private final String status;
    private final String message;
}
