package com.example.springKata.transformer_ns.adapter.in.web;

import com.example.springKata.transformer_ns.domain.exception.InvalidNumberException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidNumberException.class)
    public ResponseEntity<ErrorResponse> handleInvalidNumber(InvalidNumberException ex) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(ex.getMessage(), 400));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponse("Le paramètre doit être un entier valide, reçu : " + ex.getValue(), 400));
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleMissingNumber(NoResourceFoundException ex) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponse("Le paramètre number est obligatoire. Usage : /transform/{number}", 400));
    }
}
