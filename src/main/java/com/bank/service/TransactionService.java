package com.bank.service;

import com.bank.exceptions.AccountNotFoundException;
import com.bank.model.Account;
import com.bank.repo.TransactionRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class TransactionService {

    private TransactionRepository transactionRepository;

    private AccountService accountService;

    public void credit(String accountId, BigDecimal amount) throws AccountNotFoundException {
        Account account = accountService.credit(accountId, amount);
    }
}
