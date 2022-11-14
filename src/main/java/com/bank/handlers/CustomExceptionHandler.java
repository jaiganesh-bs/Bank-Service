package com.bank.handlers;

import com.bank.controller.response.ErrorResponse;
import com.bank.exceptions.InvalidAmountException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Collections;

@ControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(InvalidAmountException.class)
    public ResponseEntity handleInvalidAmountException(InvalidAmountException invalidAmountException){
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }
}
