package com.bank.service;

import com.bank.exceptions.AccountNotFoundException;
import com.bank.repo.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class TransactionServiceTest {

    private TransactionService transactionService;
    private TransactionRepository transactionRepository;
    private AccountService accountService;

    @BeforeEach
    void setUp() {
        transactionRepository = mock(TransactionRepository.class);
        accountService = mock(AccountService.class);
        transactionService = new TransactionService(transactionRepository, accountService);
    }

    @Test
    void shouldBeAbleToGetTheAccountAfterCreditedTheAmount() throws AccountNotFoundException {
        String id = "userId";
        BigDecimal amount = new BigDecimal(100);

        transactionService.credit(id,amount);

        verify(accountService).credit(id,amount);
    }
}
