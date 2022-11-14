package com.bank.controller;

import com.bank.controller.request.TransactionRequest;
import com.bank.controller.response.TransactionHistoryResponse;
import com.bank.exceptions.AccountNotFoundException;
import com.bank.exceptions.InvalidAmountException;
import com.bank.model.Transaction;
import com.bank.service.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    @PostMapping(value = "debit")
    public ResponseEntity debit(Principal principal,@RequestBody TransactionRequest transactionRequest) throws InvalidAmountException, AccountNotFoundException {
        Transaction debit = transactionService.debit(principal.getName(), transactionRequest.getAmount());
        return new ResponseEntity(debit.getId(),HttpStatus.CREATED);
    }
    @GetMapping
    public ResponseEntity accountStatement(Principal principal) throws AccountNotFoundException {
        TransactionHistoryResponse transactionHistory = transactionService.getAccountStatement(principal.getName());
        return new ResponseEntity(transactionHistory, HttpStatus.OK);
    }
}
