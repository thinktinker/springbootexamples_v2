package com.martin.jpa.exception;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;
import java.util.HashMap;

@Order(Ordered.HIGHEST_PRECEDENCE) // when exceptions occur, the GlobalExceptionHandler takes highest precedence
@ControllerAdvice  // addresses exceptions across the entire app (i.e. globally)
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    // GlobalExceptionHandler extends ResponseEntityExceptionHandler
    // and inherits the built-in methods from ResponseEntityExceptionHandler

    // 1. When user sends over data that is not readable, handleHttpMessageNotReadable will be thrown
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        // customise our own message using MessageNotReadableExeption
        MessageNotReadableException messageNotReadableException = new MessageNotReadableException();

        // store the responses as a Hashmap, to return as part of the response
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", messageNotReadableException.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    //2. When user request for a resource (e.g. Customer or Feedback not found),
    // ResourceNotFoundException will be thrown, managed by httpEntityNotFound
    @ExceptionHandler(ResourceNotFoundException.class)
    protected ResponseEntity<Object> httpEntityNotFound(ResourceNotFoundException ex){

        // store the responses as a HashMap, to return as part of the reponse
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", ex.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    // 3. When user sends a requestbody that is empty OR incomplete,
    // handleMethodArugmentNotValid is invoked (tracked by @Valid annotation)
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        // create a hashmap to store the field(s) and its related error
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((err)->{
            String field = ((FieldError) err).getField();
            String errMessage = err.getDefaultMessage();
            errors.put(field, errMessage);
        });

        // store the errors as an Object in errorResponse
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", errors);

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
