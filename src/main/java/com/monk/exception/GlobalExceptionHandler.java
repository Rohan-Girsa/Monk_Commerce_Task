package com.monk.exception;


import com.monk.model.StatusDescription;
import com.monk.response.ResponseWrapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CustomCouponException.class)
    public ResponseEntity<ResponseWrapper> CustomCouponException(CustomCouponException ex) {
        StatusDescription statusDescription = StatusDescription.builder()
                .statusCode(404)
                .statusDescription(ex.getMessage()).build();
        ResponseWrapper response = ResponseWrapper.builder().statusDescription(statusDescription).build();
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<ResponseWrapper> InvalidInputException(InvalidInputException ex) {
        StatusDescription statusDescription = StatusDescription.builder()
                .statusCode(400)
                .statusDescription(ex.getMessage()).build();
        ResponseWrapper response = ResponseWrapper.builder().statusDescription(statusDescription).build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseWrapper> handleGenericException(Exception ex) {
        StatusDescription statusDescription = StatusDescription.builder().statusCode(500)
                .statusDescription("Internal Server Error: "+ex.getMessage()).build();
        ResponseWrapper response = ResponseWrapper.builder().statusDescription(statusDescription).build();
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
