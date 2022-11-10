package com.bank.controller;

import com.bank.controller.request.TransactionRequest;
import com.bank.service.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@AllArgsConstructor
public class TransactionController {

    private TransactionService transactionService;

    public void credit(TransactionRequest transactionRequest) {
        transactionService.credit(transactionRequest.getAmount());
    }
}
