package com.socialmeadia.socialmedia.Exception;

import com.socialmeadia.socialmedia.Util.ResponseHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.validation.FieldError;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseHandler<?>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        ResponseHandler<Map<String, String>> response = new ResponseHandler<>();
        response.setStatus(HttpStatus.BAD_REQUEST);
        response.setSuccess(false);

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        response.setMessage("Validation failed");
        response.setData(errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseHandler<?>> handleGenericException(Exception ex) {
        ResponseHandler<?> response = new ResponseHandler<>();
        response.setStatus(HttpStatus.BAD_REQUEST);
        response.setMessage(ex.getMessage());
        response.setSuccess(false);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
