package com.bank.controller;

import com.bank.controller.request.TransactionRequest;
import com.bank.exceptions.AccountNotFoundException;
import com.bank.exceptions.InvalidAmountException;
import com.bank.service.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@AllArgsConstructor
public class TransactionController {

    private TransactionService transactionService;

    public void credit(Principal principal,TransactionRequest transactionRequest) throws AccountNotFoundException, InvalidAmountException {
        transactionService.credit(principal.getName(),transactionRequest.getAmount());
    }
}
