package com.space.controller;

import org.springframework.http.HttpStatus;

public class ShipErrorResponse {

    private int status;

    public ShipErrorResponse() {
    }

    public ShipErrorResponse(HttpStatus notFound) {
    }
}
