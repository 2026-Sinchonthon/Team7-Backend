package sinchonthon.demo.global.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sinchonthon.demo.global.response.ApiResponse;
import sinchonthon.demo.global.response.GeneralErrorCode;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusinessException(BusinessException exception) {
        GeneralErrorCode errorCode = exception.getErrorCode();
        return ResponseEntity.status(errorCode.getStatus()).body(ApiResponse.failure(errorCode));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationException(MethodArgumentNotValidException exception) {
        FieldError fieldError = exception.getBindingResult().getFieldError();
        String message = fieldError == null ? GeneralErrorCode.INVALID_REQUEST.getMessage() : fieldError.getDefaultMessage();
        ApiResponse<Void> response = new ApiResponse<>(false, GeneralErrorCode.INVALID_REQUEST.getCode(), message, null);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleUnexpectedException(Exception exception) {
        return ResponseEntity.internalServerError().body(ApiResponse.failure(GeneralErrorCode.INTERNAL_SERVER_ERROR));
    }
}
