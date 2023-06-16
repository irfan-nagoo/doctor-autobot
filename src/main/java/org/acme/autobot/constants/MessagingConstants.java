package org.acme.autobot.constants;

/**
 * @author irfan.nagoo
 */
public interface MessagingConstants {

    String TOTAL_RECORD_MSG = "Total [%d] Records Found";
    String PROCESS_SUCCESS_MSG = "Request Processed Successfully";
    String RECORD_DELETED_MSG = "Record Deleted Successfully";
    String PROCESSING_ERROR_MSG = "Processing Error Occurred";
    String CONSTRAINT_VIOLATION_ERROR_MSG = "Constraint Violation Error Occurred";
    String NOT_FOUND_ERROR_MSG = "Records Not Found";
    String SYMPTOM_FOUND_ERROR_MSG = "Matching Symptom Not Found";
    String STATUS_INVALID_ERROR_MSG = "The given status is Invalid";
    String ID_INVALID_ERROR_MSG = "The Id should be null or greater than 0";
    String SORT_FIELD_ERROR_MSG = "The number of sort by fields and sort directions should be equal";
}
