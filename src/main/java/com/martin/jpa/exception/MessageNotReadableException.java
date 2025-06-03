package com.martin.jpa.exception;

public class MessageNotReadableException extends RuntimeException{

    public MessageNotReadableException() {
        super("Invalid data. Please check again.");
    }
}
