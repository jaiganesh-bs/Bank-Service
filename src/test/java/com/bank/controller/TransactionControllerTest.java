package com.bank.controller;

import com.bank.controller.request.TransactionRequest;
import com.bank.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class TransactionControllerTest {

    private TransactionService transactionService;
    private TransactionController transactionController;

    @BeforeEach
    void setUp() {
        transactionService = mock(TransactionService.class);
        transactionController = new TransactionController(transactionService);

    }

    @Test
    void shouldBeAbleToCreditAmountInTheAccount() {
        BigDecimal amount = new BigDecimal(100);
        TransactionRequest transactionRequest = new TransactionRequest(amount);

        transactionController.credit(transactionRequest);

        verify(transactionService).credit(transactionRequest.getAmount());
    }
}
