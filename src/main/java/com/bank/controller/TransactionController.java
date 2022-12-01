package com.bank.controller;

import com.bank.controller.request.TransactionRequest;
import com.bank.controller.response.TransactionHistoryResponse;
import com.bank.exceptions.AccountNotFoundException;
import com.bank.exceptions.InvalidAmountException;
import com.bank.service.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@AllArgsConstructor
@RequestMapping("transaction")
public class TransactionController {

    private TransactionService transactionService;

    @PostMapping(value = "credit")
    @ResponseStatus(HttpStatus.CREATED)
    public void credit(Principal principal,@RequestBody TransactionRequest transactionRequest) throws AccountNotFoundException, InvalidAmountException {
        transactionService.credit(principal.getName(), transactionRequest.getAmount());
    }

    @PostMapping(value = "debit")
    @ResponseStatus(HttpStatus.CREATED)
    public void debit(Principal principal, @RequestBody TransactionRequest transactionRequest) throws InvalidAmountException, AccountNotFoundException {
        transactionService.debit(principal.getName(), transactionRequest.getAmount());
    }

    @GetMapping
    public TransactionHistoryResponse accountStatement(Principal principal) throws AccountNotFoundException {
        return transactionService.getAccountStatement(principal.getName());
    }
}
