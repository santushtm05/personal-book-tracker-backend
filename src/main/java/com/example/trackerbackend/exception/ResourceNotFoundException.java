package com.example.trackerbackend.exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends BaseException {

    public ResourceNotFoundException(String resource) {
        super(resource + " not found!", HttpStatus.NOT_FOUND);
    }
}