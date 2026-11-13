
package purchase.handler;
import purchase.domain.InvalidPurchaseId;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import purchase.domain.DuplicatePurchase;
import purchase.domain.PurchaseNotFound;

import java.util.UUID;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
@RestControllerAdvice
public class PurchaseExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(PurchaseExceptionHandler.class);
    private String correlationId(HttpServletRequest request) {
        String id = request.getHeader("X-Correlation-Id");
        if (id == null || id.isBlank()) {
            return UUID.randomUUID().toString();
        }
        return id;
    }

    private ResponseEntity<ApiErrorResponse> error(HttpStatus status,String code,String message,HttpServletRequest request) {
        return ResponseEntity.status(status).body(new ApiErrorResponse(code, message, correlationId(request))
        );
    }
    @ExceptionHandler(InvalidPurchaseId.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidPurchaseId(
            InvalidPurchaseId ex,
            HttpServletRequest request
    ) {
        return error(
                HttpStatus.BAD_REQUEST,
                "VALIDATION",
                "id must be a UUID",
                request
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(fieldError ->
                        fieldError.getField() + " " +
                                fieldError.getDefaultMessage()
                )
                .orElse("request validation failed");

        return error(
                HttpStatus.BAD_REQUEST,
                "VALIDATION",
                message,
                request
        );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiErrorResponse> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex,
            HttpServletRequest request
    ) {
        if ("id".equals(ex.getName())) {
            return error(
                    HttpStatus.BAD_REQUEST,
                    "VALIDATION",
                    "id must be a UUID",
                    request
            );
        }

        return error(
                HttpStatus.BAD_REQUEST,
                "VALIDATION",
                "invalid request parameter",
                request
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidJson(
            HttpMessageNotReadableException ex,
            HttpServletRequest request
    ) {
        return error(
                HttpStatus.BAD_REQUEST,
                "VALIDATION",
                "body must be JSON",
                request
        );
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNoResourceFound(
            NoResourceFoundException ex,
            HttpServletRequest request
    ) {
        return error(
                HttpStatus.NOT_FOUND,
                "NOT_FOUND",
                "resource not found",
                request
        );
    }
    @ExceptionHandler(PurchaseNotFound.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(
            PurchaseNotFound ex,
            HttpServletRequest request
    ) {
        return error(
                HttpStatus.NOT_FOUND,
                "NOT_FOUND",
                "purchase not found",
                request
        );
    }

    @ExceptionHandler(DuplicatePurchase.class)
    public ResponseEntity<ApiErrorResponse> handleDuplicate(
            DuplicatePurchase ex,
            HttpServletRequest request
    ) {
        return error(
                HttpStatus.CONFLICT,
                "DUPLICATE",
                "purchase with this business key already exists",
                request
        );
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ApiErrorResponse> handleUnsupportedMediaType(
            HttpMediaTypeNotSupportedException ex,
            HttpServletRequest request
    ) {
        return error(
                HttpStatus.UNSUPPORTED_MEDIA_TYPE,
                "UNSUPPORTED",
                "unsupported content type",
                request
        );
    }

    @ExceptionHandler({
            ConstraintViolationException.class,
            IllegalArgumentException.class
    })
    public ResponseEntity<ApiErrorResponse> handleBadRequest(
            Exception ex,
            HttpServletRequest request
    ) {
        return error(
                HttpStatus.BAD_REQUEST,
                "VALIDATION",
                "invalid request",
                request
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleUnexpected(
            Exception ex,
            HttpServletRequest request
    ) {
        String id = correlationId(request);

        log.error(
                "Unexpected error. Correlation ID: {}",
                id,
                ex
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiErrorResponse(
                        "INTERNAL",
                        "unexpected error",
                        id
                ));
    }
}
