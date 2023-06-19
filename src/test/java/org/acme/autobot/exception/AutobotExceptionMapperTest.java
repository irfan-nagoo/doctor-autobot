package org.acme.autobot.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import jakarta.ws.rs.core.Response;
import org.acme.autobot.response.ErrorResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AutobotExceptionMapperTest {

    AutobotExceptionMapper autobotExceptionMapper;

    @BeforeEach
    void setup() {
        autobotExceptionMapper = new AutobotExceptionMapper();
    }

    @Test
    void toResponse_RecordNotFoundException() {
        var exception = new RecordNotFoundException("Record Not Found");
        var response = autobotExceptionMapper.toResponse(exception);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        assertEquals("Record Not Found", ((ErrorResponse) response.getEntity()).getMessage());
    }

    @Test
    void toResponse_ConstraintViolationException() {
        ConstraintViolation<?> constraintViolation = mock(ConstraintViolation.class);
        var path = mock(Path.class);
        when(constraintViolation.getPropertyPath()).thenReturn(path);
        when(constraintViolation.getMessage()).thenReturn("Id is empty");
        var constraintViolationException = new ConstraintViolationException("Some error", Set.of(constraintViolation));
        var response = autobotExceptionMapper.toResponse(constraintViolationException);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertEquals("Constraint Violation Error Occurred", ((ErrorResponse) response.getEntity()).getMessage());
        assertFalse(((ErrorResponse) response.getEntity()).getErrors().isEmpty());
        assertEquals("Id is empty", ((ErrorResponse) response.getEntity()).getErrors().get(0).getMessage());
    }

    @Test
    void toResponse_IllegalArgumentException() {
        var exception = new IllegalArgumentException("Bad Request Parameter");
        var response = autobotExceptionMapper.toResponse(exception);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertEquals("Bad Request Parameter", ((ErrorResponse) response.getEntity()).getMessage());
    }

    @Test
    void toResponse_Exception() {
        var exception = new Exception("Some error");
        var response = autobotExceptionMapper.toResponse(exception);
        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        assertEquals("Processing Error Occurred", ((ErrorResponse) response.getEntity()).getMessage());
    }

}