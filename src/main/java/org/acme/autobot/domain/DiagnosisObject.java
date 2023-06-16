package org.acme.autobot.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.quarkus.arc.All;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.acme.autobot.vo.DiagnosisVO;

import java.util.List;

/**
 * @author irfan.nagoo
 */

@Getter
@Setter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class DiagnosisObject {

    private List<DiagnosisVO> symptoms;
    private List<DiagnosisVO> minorDiseases;
    private List<DiagnosisVO> majorDiseases;

}
