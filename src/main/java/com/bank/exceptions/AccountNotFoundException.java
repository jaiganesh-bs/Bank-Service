package com.bank.exceptions;

public class AccountNotFoundException extends Exception{
    public AccountNotFoundException() {
        super("account not found!");
    }
}
