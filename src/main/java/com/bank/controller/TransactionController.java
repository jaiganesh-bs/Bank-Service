package com.bank.controller;

import com.bank.controller.request.TransactionRequest;
import com.bank.exceptions.AccountNotFoundException;
import com.bank.exceptions.InvalidAmountException;
import com.bank.model.Transaction;
import com.bank.service.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@AllArgsConstructor
@RequestMapping("transaction")
public class TransactionController {

    private TransactionService transactionService;

    @PostMapping(value = "credit")
    public ResponseEntity credit(Principal principal, @RequestBody TransactionRequest transactionRequest) throws AccountNotFoundException, InvalidAmountException {
        Transaction credit = transactionService.credit(principal.getName(), transactionRequest.getAmount());
        return new ResponseEntity(credit.getId(),HttpStatus.CREATED);
    }

    public void debit(Principal principal, TransactionRequest transactionRequest) {
        transactionService.debit(principal.getName(), transactionRequest.getAmount());
    }
}
