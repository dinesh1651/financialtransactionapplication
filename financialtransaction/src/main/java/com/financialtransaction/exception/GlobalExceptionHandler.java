package com.financialtransaction.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

    @RestControllerAdvice
    public class GlobalExceptionHandler {

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<String> validationException(
                MethodArgumentNotValidException ex){

            return ResponseEntity.badRequest()
                    .body("Validation Failed");
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<String> exception(Exception ex){

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ex.getMessage());
        }
}
