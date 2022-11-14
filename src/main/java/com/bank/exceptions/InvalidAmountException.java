package com.bank.exceptions;

public class InvalidAmountException extends Exception{
    public InvalidAmountException() {
        super("Amount can't be less than one!");
    }
}
