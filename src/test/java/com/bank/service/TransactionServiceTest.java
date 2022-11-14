package com.bank.service;

import com.bank.exceptions.AccountNotFoundException;
import com.bank.exceptions.InvalidAmountException;
import com.bank.model.Account;
import com.bank.model.Transaction;
import com.bank.model.TransactionType;
import com.bank.repo.TransactionRepository;
import com.bank.repo.TransactionTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class TransactionServiceTest {

    private TransactionService transactionService;
    private TransactionRepository transactionRepository;
    private AccountService accountService;
    private TransactionTypeRepository transactionTypeRepository;

    @BeforeEach
    void setUp() {
        transactionRepository = mock(TransactionRepository.class);
        transactionTypeRepository = mock(TransactionTypeRepository.class);
        accountService = mock(AccountService.class);
        transactionService = new TransactionService(transactionRepository, accountService, transactionTypeRepository);
    }

    @Test
    void shouldBeAbleToGetTheAccountAfterCreditedTheAmount() throws AccountNotFoundException, InvalidAmountException {
        String id = "userId";
        BigDecimal amount = new BigDecimal(100);
        when(transactionTypeRepository.findByName("CREDIT")).thenReturn(new TransactionType("CREDIT"));
        when(accountService.credit(id,amount)).thenReturn(mock(Account.class));

        transactionService.credit(id, amount);

        verify(accountService).credit(id, amount);
    }

    @Test
    void shouldBeAbleToAddTheTransactionToTheAccountWhenCreditIsDone() throws AccountNotFoundException, InvalidAmountException {
        String accountId = "userId";
        BigDecimal amount = new BigDecimal(100);
        when(transactionTypeRepository.findByName("CREDIT")).thenReturn(new TransactionType("CREDIT"));
        Account account = mock(Account.class);
        when(accountService.credit(accountId, amount)).thenReturn(account);
        Date today = new Date();
        Transaction credit = new Transaction(account, today, transactionTypeRepository.findByName("CREDIT"), amount, account.getAvail_bal());

        transactionService.credit(accountId, amount);

        verify(transactionRepository).save(credit);
    }

    @Test
    void shouldThrowAmountNotBeLessThanOneExceptionWhenCreditAmountIsLessThanOne() {
        String id = "userId";
        BigDecimal amount = new BigDecimal(-1);

        assertThrows(InvalidAmountException.class, () -> transactionService.credit(id, amount));
    }

    @Test
    void shouldBeAbleToGetAccountAfterTheAmountIsDebited() throws InvalidAmountException, AccountNotFoundException {
        String id = "userId";
        BigDecimal amount = new BigDecimal(100);
        when(transactionTypeRepository.findByName("DEBIT")).thenReturn(new TransactionType("DEBIT"));
        when(accountService.debit(id,amount)).thenReturn(mock(Account.class));

        transactionService.debit(id, amount);

        verify(accountService).debit(id, amount);
    }

    @Test
    void shouldBeAbleToAddTheTransactionToTheAccountWhenDebitIsDone() throws InvalidAmountException, AccountNotFoundException {
        String accountId = "userId";
        BigDecimal amount = new BigDecimal(100);
        when(transactionTypeRepository.findByName("DEBIT")).thenReturn(new TransactionType("DEBIT"));
        Account account = mock(Account.class);
        when(accountService.debit(accountId, amount)).thenReturn(account);
        Date today = new Date();
        Transaction debit = new Transaction(account, today, transactionTypeRepository.findByName("CREDIT"), amount, account.getAvail_bal());

        transactionService.debit(accountId, amount);

        verify(transactionRepository).save(debit);
    }

    @Test
    void shouldThrowAmountNotBeLessThanOneExceptionWhenDebitAmountIsLessThanOne() {
        String id = "userId";
        BigDecimal amount = new BigDecimal(-1);

        assertThrows(InvalidAmountException.class, () -> transactionService.debit(id, amount));
    }
}
