package com.daniels.usuarios.infrastructure.exceptions;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
    //excessão personalizada
    public UnauthorizedException(String message, Throwable throwable){
        super(message, throwable);
    }

}
