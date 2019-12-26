package com.space.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ShipRestExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ShipErrorResponse> handleException(ShipNotFoundException e) {
        ShipErrorResponse error = new ShipErrorResponse(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ShipErrorResponse> handleException(Exception e) {
        ShipErrorResponse error = new ShipErrorResponse(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
