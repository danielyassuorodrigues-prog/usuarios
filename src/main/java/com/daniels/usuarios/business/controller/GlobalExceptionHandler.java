package com.daniels.usuarios.business.controller;


import com.daniels.usuarios.business.dto.ErrorResponseDTO;
import com.daniels.usuarios.infrastructure.exceptions.ConflictException;
import com.daniels.usuarios.infrastructure.exceptions.IllegalArgumentException;
import com.daniels.usuarios.infrastructure.exceptions.ResourceNotFoundException;
import com.daniels.usuarios.infrastructure.exceptions.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handlerResourceNotFoundException(ResourceNotFoundException ex, HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(buildError(HttpStatus.NOT_FOUND.value(), ex.getMessage(), request.getRequestURI(), "Not Found"));
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponseDTO> handlerConflictException(ConflictException conflictException, HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(buildError(HttpStatus.CONFLICT.value(), conflictException.getMessage(), request.getRequestURI(), "Not Found"));
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<String> handlerUnathorizedException(UnauthorizedException unauthorizedException){
        return new ResponseEntity<>(unauthorizedException.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handlerIllegalArgumentException(IllegalArgumentException illegalArgumentException){
        return new ResponseEntity<>(illegalArgumentException.getMessage(), HttpStatus.BAD_REQUEST);
    }

    private ErrorResponseDTO buildError(int status, String mensagem, String path, String error) {
       return  ErrorResponseDTO.builder()
                .timeStamp(LocalDateTime.now())
                .message(mensagem)
                .erro(error)
                .status(status)
                .path(path)
                .build();
    }



}
