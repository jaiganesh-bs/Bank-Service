package com.bank.controller;

import com.bank.controller.request.TransactionRequest;
import com.bank.controller.response.TransactionHistoryResponse;
import com.bank.exceptions.AccountNotFoundException;
import com.bank.exceptions.InvalidAmountException;
import com.bank.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.security.Principal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransactionControllerTest {
    @Mock
    private TransactionService transactionService;
    @InjectMocks
    private TransactionController transactionController;
    @Mock
    private Principal principal;


    @Test
    void shouldBeAbleToCreditAmountInTheAccount() throws AccountNotFoundException, InvalidAmountException {
        BigDecimal amount = new BigDecimal(100);
        TransactionRequest transactionRequest = new TransactionRequest(amount);

        transactionController.credit(principal, transactionRequest);

        verify(transactionService).credit(principal.getName(), amount);
    }

    @Test
    void shouldBeAbleToDebitAmountInTheAccount() throws InvalidAmountException, AccountNotFoundException {
        BigDecimal amount = new BigDecimal(100);
        TransactionRequest transactionRequest = new TransactionRequest(amount);

        transactionController.debit(principal, transactionRequest);

        verify(transactionService).debit(principal.getName(), transactionRequest.getAmount());
    }

    @Test
    void shouldBeAbleToGetAccountTransactionHistory() throws AccountNotFoundException {
        TransactionHistoryResponse actualTransactionHistoryResponse = new TransactionHistoryResponse();
        when(transactionService.getAccountStatement(principal.getName())).thenReturn(actualTransactionHistoryResponse);

        TransactionHistoryResponse expectedTransactionHistoryResponse = transactionController.accountStatement(principal);

        verify(transactionService).getAccountStatement(principal.getName());
        assertThat(actualTransactionHistoryResponse, is(expectedTransactionHistoryResponse));
    }
}
