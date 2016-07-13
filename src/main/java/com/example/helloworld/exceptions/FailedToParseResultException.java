package com.example.helloworld.exceptions;

/**
 * Created by ddudariev on 13-Jul-16.
 */
public class FailedToParseResultException extends Exception {
    
    public FailedToParseResultException() {
    }
    
    public FailedToParseResultException(String message) {
        super(message);
    }
}
