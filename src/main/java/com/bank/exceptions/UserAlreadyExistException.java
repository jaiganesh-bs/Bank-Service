package com.bank.exceptions;

public class UserAlreadyExistException extends Exception{
    public UserAlreadyExistException() {
        super("user already exists!");
    }
}
