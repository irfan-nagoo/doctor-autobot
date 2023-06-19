package org.acme.autobot.exception;

import io.netty.handler.codec.http.HttpResponseStatus;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;
import org.acme.autobot.response.ErrorResponse;

import java.util.UUID;

import static org.acme.autobot.constants.MessagingConstants.CONSTRAINT_VIOLATION_ERROR_MSG;
import static org.acme.autobot.constants.MessagingConstants.PROCESSING_ERROR_MSG;

/**
 * @author irfan.nagoo
 */

@Provider
@Slf4j
public class AutobotExceptionMapper implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable e) {
        String errorId = UUID.randomUUID().toString();
        if (e instanceof RecordNotFoundException) {
            return handleRecordNotFoundException((RecordNotFoundException) e, errorId);
        } else if (e instanceof IllegalArgumentException) {
            return handleIllegalArgumentException((IllegalArgumentException) e, errorId);
        } else if (e instanceof ConstraintViolationException) {
            return handleConstraintViolationException((ConstraintViolationException) e, errorId);
        } else {
            return handleThrowable(e, errorId);
        }
    }

    private Response handleRecordNotFoundException(RecordNotFoundException rnf, String errorId) {
        log.error("Record not found with errorId[{}]: ", errorId, rnf);
        return Response.status(Response.Status.NOT_FOUND)
                .entity(new ErrorResponse(HttpResponseStatus.NOT_FOUND.reasonPhrase(), rnf.getMessage(), errorId))
                .build();
    }

    private Response handleConstraintViolationException(ConstraintViolationException cve, String errorId) {
        log.error("Constraint violation error with errorId[{}]: ", errorId, cve);
        ErrorResponse response = new ErrorResponse(HttpResponseStatus.BAD_REQUEST.reasonPhrase(), CONSTRAINT_VIOLATION_ERROR_MSG, errorId);
        cve.getConstraintViolations()
                .forEach(x -> response.getErrors()
                        .add(new ErrorResponse.ValidationError(x.getPropertyPath().toString(), x.getMessage())));
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(response)
                .build();
    }

    private Response handleIllegalArgumentException(IllegalArgumentException nfe, String errorId) {
        log.error("Bad request parameter with errorId[{}]: ", errorId, nfe);
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(new ErrorResponse(HttpResponseStatus.BAD_REQUEST.reasonPhrase(), nfe.getMessage(), errorId))
                .build();
    }

    private Response handleThrowable(Throwable e, String errorId) {
        log.error("Processing error occurred with errorId[{}]: ", errorId, e);
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse(HttpResponseStatus.INTERNAL_SERVER_ERROR.reasonPhrase(), PROCESSING_ERROR_MSG, errorId))
                .build();
    }
}
