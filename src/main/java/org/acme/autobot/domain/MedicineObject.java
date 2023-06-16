package org.acme.autobot.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.acme.autobot.constants.StatusType;

import java.time.LocalDateTime;

import static org.acme.autobot.constants.MessagingConstants.ID_INVALID_ERROR_MSG;

/**
 * @author irfan.nagoo
 */

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MedicineObject {

    @Min(value = 1, message = ID_INVALID_ERROR_MSG)
    private Long id;
    @NotEmpty
    private String cui;
    @NotEmpty
    private String name;
    @NotEmpty
    private String source;
    private String semType1;
    private String semType2;
    private String semType3;
    private StatusType status;
    private Long version;
    private LocalDateTime createDate;
    @NotEmpty
    private String createdBy;
    private LocalDateTime updateDate;
    @NotEmpty
    private String updatedBy;
}
