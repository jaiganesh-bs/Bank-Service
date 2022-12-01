package com.bank.service;

import com.bank.controller.response.TransactionHistoryResponse;
import com.bank.controller.response.TransactionResponse;
import com.bank.exceptions.AccountNotFoundException;
import com.bank.exceptions.InvalidAmountException;
import com.bank.model.Account;
import com.bank.model.Transaction;
import com.bank.model.constants.TransactionType;
import com.bank.repo.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {
    @InjectMocks
    private TransactionService transactionService;
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private AccountService accountService;

    @Mock
    private Account account;

    @Test
    void shouldBeAbleToAddTheTransactionToTheAccountWhenCreditIsDone() throws AccountNotFoundException, InvalidAmountException {
        String email = "abc@example.com";
        BigDecimal amount = new BigDecimal(100);
        Date today = new Date();
        TransactionService spiedTransactionService = spy(transactionService);
        when(spiedTransactionService.getCurrentDate()).thenReturn(today);
        when(accountService.credit(email, amount)).thenReturn(account);
        Transaction credit = new Transaction(account, today, TransactionType.CREDIT, amount);

        spiedTransactionService.credit(email, amount);

        verify(transactionRepository).save(credit);
    }

    @Test
    void shouldThrowAmountNotBeLessThanOneExceptionWhenCreditAmountIsLessThanOne() {
        String email = "abc@example.com";
        BigDecimal amount = new BigDecimal(-1);

        assertThrows(InvalidAmountException.class, () -> transactionService.credit(email, amount));
    }

    @Test
    void shouldBeAbleToAddTheTransactionToTheAccountWhenDebitIsDone() throws InvalidAmountException, AccountNotFoundException {
        String email = "abc@example.com";
        BigDecimal amount = new BigDecimal(100);
        Date today = new Date();
        TransactionService spiedTransactionService = spy(transactionService);
        when(spiedTransactionService.getCurrentDate()).thenReturn(today);
        when(accountService.debit(email, amount)).thenReturn(account);
        Transaction debit = new Transaction(account, today, TransactionType.DEBIT, amount);

        spiedTransactionService.debit(email, amount);

        verify(transactionRepository).save(debit);
    }

    @Test
    void shouldThrowAmountNotBeLessThanOneExceptionWhenDebitAmountIsLessThanOne() {
        String email = "abc@example.com";
        BigDecimal amount = new BigDecimal(-1);

        assertThrows(InvalidAmountException.class, () -> transactionService.debit(email, amount));
    }

    @Test
    void shouldAbleToGetAccountTransactionHistoryAndAvailableBalanceWhenAccountIdIsGiven() throws AccountNotFoundException {
        String id = "userAccount";
        String email = "Jaiganesh";
        String password = "Password@234";
        BigDecimal balance = new BigDecimal(0);
        Account account = new Account(id, email, password, balance);
        when(accountService.getAccount(email)).thenReturn(account);
        BigDecimal amount = new BigDecimal(100);
        Transaction credit = new Transaction(account, new Date(), TransactionType.CREDIT, amount);
        Transaction debit = new Transaction(account, new Date(), TransactionType.DEBIT, amount);
        List<Transaction> transactions = new ArrayList<>(Arrays.asList(credit, debit));
        when(transactionService.getHistory(email)).thenReturn(transactions);
        ArrayList<TransactionResponse> transactionResponses = new ArrayList<>();
        TransactionResponse transactionResponse = new TransactionResponse();
        for (Transaction transaction : transactions) {
            transactionResponses.add(transactionResponse.getTransactionResponse(transaction));
        }
        TransactionHistoryResponse expectedTransactionHistoryResponse = new TransactionHistoryResponse(id, email, balance, transactionResponses);

        TransactionHistoryResponse actualTransactionHistoryResponse = transactionService.getAccountStatement(email);

        verify(transactionRepository).findByAccountEmail(email);
        assertThat(actualTransactionHistoryResponse, is(equalTo(expectedTransactionHistoryResponse)));
    }
}
