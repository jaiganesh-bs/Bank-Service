package com.bank.controller;

import com.bank.controller.request.TransactionRequest;
import com.bank.exceptions.AccountNotFoundException;
import com.bank.exceptions.InvalidAmountException;
import com.bank.model.Transaction;
import com.bank.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.security.Principal;

import static org.mockito.Mockito.*;

public class TransactionControllerTest {

    private TransactionService transactionService;
    private TransactionController transactionController;

    @BeforeEach
    void setUp() {
        transactionService = mock(TransactionService.class);
        transactionController = new TransactionController(transactionService);

    }

    @Test
    void shouldBeAbleToCreditAmountInTheAccount() throws AccountNotFoundException, InvalidAmountException {
        String accountId = "userAccount";
        BigDecimal amount = new BigDecimal(100);
        TransactionRequest transactionRequest = new TransactionRequest(amount);
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn(accountId);
        when(transactionService.credit(principal.getName(),amount)).thenReturn(mock(Transaction.class));

        transactionController.credit(principal,transactionRequest);

        verify(transactionService).credit(accountId,transactionRequest.getAmount());
    }

    @Test
    void shouldBeAbleToDebitAmountInTheAccount() throws InvalidAmountException {
        String accountId = "userAccount";
        BigDecimal amount = new BigDecimal(100);
        TransactionRequest transactionRequest = new TransactionRequest(amount);
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn(accountId);
        when(transactionService.debit(principal.getName(),amount)).thenReturn(mock(Transaction.class));

        transactionController.debit(principal,transactionRequest);

        verify(transactionService).debit(accountId,transactionRequest.getAmount());
    }
}
