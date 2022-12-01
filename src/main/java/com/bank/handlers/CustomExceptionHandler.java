package com.bank.handlers;

import com.bank.controller.response.ErrorResponse;
import com.bank.exceptions.InvalidAmountException;
import com.bank.exceptions.UserAlreadyExistException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Collections;

@ControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(InvalidAmountException.class)
    public ResponseEntity<ErrorResponse> handleInvalidAmountException(InvalidAmountException invalidAmountException) {
        ErrorResponse errorResponse = new ErrorResponse("amount can't be negative!", Collections.singletonList(invalidAmountException.getMessage()));
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExistException(UserAlreadyExistException userAlreadyExistException) {
        ErrorResponse errorResponse = new ErrorResponse("user already exists!", Collections.singletonList(userAlreadyExistException.getMessage()));
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
