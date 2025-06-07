package com.martin.jpa.exception;

public class UnspecifiedException extends RuntimeException{
    public UnspecifiedException() {
        super("An unspecified error has occurred. Please try again later");
    }
}
