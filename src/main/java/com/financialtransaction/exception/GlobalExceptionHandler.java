package com.financialtransaction.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

@RestControllerAdvice
    public class GlobalExceptionHandler {

        @ExceptionHandler(HandlerMethodValidationException.class)
        public ResponseEntity<String> validationException(
                HandlerMethodValidationException ex) {

//            String message = ex.getParameterValidationResults()
//                    .stream()
//                    .flatMap(result -> result.getResolvableErrors().stream())
//                    .map(error -> error.getDefaultMessage())
//                    .filter(messageText -> messageText != null)
//                    .findFirst()
//                    .orElse("Validation failed");

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Validation failed: Amount must be greater than 0");
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<String> exception(Exception ex){

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ex.getMessage());
        }

        @ExceptionHandler(NoTransactionAvailableException.class)
        public ResponseEntity<String> handleNoTransactionException(
                NoTransactionAvailableException ex) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ex.getMessage());
        }
}
