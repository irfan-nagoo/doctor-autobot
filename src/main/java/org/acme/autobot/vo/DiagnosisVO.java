package org.acme.autobot.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author irfan.nagoo
 */

@Getter
@Setter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class DiagnosisVO {

    private String cui;
    private String name;
    private List<DiagnosisVO> medicines;
}
