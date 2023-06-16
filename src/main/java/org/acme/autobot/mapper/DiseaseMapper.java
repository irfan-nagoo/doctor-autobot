package org.acme.autobot.mapper;

import org.acme.autobot.constants.StatusType;
import org.acme.autobot.domain.DiseaseObject;
import org.acme.autobot.entity.Disease;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

/**
 * @author irfan.nagoo
 */

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA_CDI,
        uses = {SymptomMapper.class, MedicineMapper.class})
public interface DiseaseMapper {

    @Mapping(target = "status", qualifiedByName = "DiseaseStatusTypeToStatus")
    @Mapping(target = "version", defaultValue = "1l")
    @Mapping(target = "createDate", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updateDate", expression = "java(java.time.LocalDateTime.now())")
    Disease diseaseObjectToDisease(DiseaseObject diseaseObject);

    @Mapping(target = "status", qualifiedByName = "DiseaseStatusToStatusType")
    DiseaseObject diseaseToDiseaseObject(Disease disease);

    @Named("DiseaseStatusToStatusType")
    default StatusType mapToStatusType(String status) {
        return StatusType.valueOf(status);
    }

    @Named("DiseaseStatusTypeToStatus")
    default String mapToStatus(StatusType statusType) {
        return statusType != null ? statusType.toString() : StatusType.ACTIVE.toString();
    }
}
