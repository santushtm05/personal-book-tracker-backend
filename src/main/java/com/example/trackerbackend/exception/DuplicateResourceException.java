package com.example.trackerbackend.exception;

import org.springframework.http.HttpStatus;

public class DuplicateResourceException extends BaseException {

    public DuplicateResourceException(String resource) {
        super(resource + " already exists", HttpStatus.CONFLICT);
    }
}