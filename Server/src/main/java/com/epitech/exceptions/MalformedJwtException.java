package com.epitech.exceptions;


public class MalformedJwtException extends RuntimeException {
    public MalformedJwtException(String message) {
        super(message);
    }
}
