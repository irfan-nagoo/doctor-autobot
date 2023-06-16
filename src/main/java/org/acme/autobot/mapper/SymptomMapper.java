package org.acme.autobot.mapper;

import org.acme.autobot.constants.StatusType;
import org.acme.autobot.domain.SymptomObject;
import org.acme.autobot.entity.Symptom;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

/**
 * @author irfan.nagoo
 */

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA_CDI)
public interface SymptomMapper {

    @Mapping(target = "status", qualifiedByName = "StatusTypeToStatus")
    @Mapping(target = "version", defaultValue = "1l")
    @Mapping(target = "createDate", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updateDate", expression = "java(java.time.LocalDateTime.now())")
    Symptom symptomObjectToSymptom(SymptomObject symptomObject);

    @Mapping(target = "status", qualifiedByName = "StatusToStatusType")
    SymptomObject symptomToSymptomObject(Symptom symptom);

    @Named("StatusToStatusType")
    default StatusType mapToStatusType(String status) {
        return StatusType.valueOf(status);
    }

    @Named("StatusTypeToStatus")
    default String mapToStatus(StatusType statusType) {
        return statusType != null ? statusType.toString() : StatusType.ACTIVE.toString();
    }
}
