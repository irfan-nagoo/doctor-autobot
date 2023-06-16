package org.acme.autobot.request;

import io.smallrye.common.constraint.NotNull;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.acme.autobot.domain.DiseaseObject;

/**
 * @author irfan.nagoo
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class DiseaseRequest {

    @Valid
    @NotNull
    private DiseaseObject disease;
}
