package com.service.video.exceptions;

import com.service.video.dtos.CustomMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.io.FileNotFoundException;
import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler extends RuntimeException{

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<CustomMessage> exceptionHandler(ResourceNotFoundException ex){
        CustomMessage customMessage = new CustomMessage();
        customMessage.setMessage("Error occurred : " + ex.getMessage());
        customMessage.setSuccess(false);
        customMessage.setStatus(HttpStatus.NOT_FOUND);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(customMessage);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationError(MethodArgumentNotValidException ex){
        Map<String,String> errorMap = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach(error ->{
            String fieldName = ((FieldError)error).getField();
            String errorMessage = error.getDefaultMessage();
            errorMap.put(fieldName, errorMessage);
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMap);
    }
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAuthorizationDeniedException(AccessDeniedException ex){
        CustomMessage customMessage = new CustomMessage();
        customMessage.setMessage(ex.getMessage());
        customMessage.setSuccess(false);
        customMessage.setStatus(HttpStatus.UNAUTHORIZED);
        return ResponseEntity.ok(customMessage);
    }
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<CustomMessage> exceptionHandler(NoResourceFoundException ex){
        CustomMessage customMessage = new CustomMessage();
        customMessage.setMessage("Error occurred : " + ex.getMessage());
        customMessage.setSuccess(false);
        customMessage.setStatus(HttpStatus.NOT_FOUND);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(customMessage);
    }
    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<?> handleAuthorizationDeniedException(FileNotFoundException ex){
        CustomMessage customMessage = new CustomMessage();
        customMessage.setMessage(ex.getMessage() + " Image or Banner not found. Pls upload the banner." );
        customMessage.setSuccess(false);
        customMessage.setStatus(HttpStatus.NOT_FOUND);
        return ResponseEntity.ok(customMessage);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleAuthorizationDeniedException(IllegalArgumentException ex){
        CustomMessage customMessage = new CustomMessage();
        customMessage.setMessage(ex.getMessage());
        customMessage.setSuccess(false);
        customMessage.setStatus(HttpStatus.FAILED_DEPENDENCY);
        return ResponseEntity.ok(customMessage);
    }
}
