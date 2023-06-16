package org.acme.autobot.request;

import io.smallrye.common.constraint.NotNull;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.acme.autobot.domain.SymptomObject;

/**
 * @author irfan.nagoo
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SymptomRequest {

    @Valid
    @NotNull
    private SymptomObject symptom;
}
