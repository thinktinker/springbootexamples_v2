package com.martin.jpa.exception;

public class ResourceNotFoundException extends Throwable{
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
