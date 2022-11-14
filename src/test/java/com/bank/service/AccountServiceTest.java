package com.bank.service;

import com.bank.controller.response.TransactionHistoryResponse;
import com.bank.controller.response.TransactionResponse;
import com.bank.exceptions.AccountNotFoundException;
import com.bank.model.Account;
import com.bank.model.Transaction;
import com.bank.model.TransactionType;
import com.bank.repo.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.*;

public class AccountServiceTest {

    private AccountRepository accountRepository;
    private AccountService accountService;

    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private TransactionService transactionService;

    @BeforeEach
    public void setUp() {
        accountRepository = mock(AccountRepository.class);
        transactionService = mock(TransactionService.class);
        bCryptPasswordEncoder = new BCryptPasswordEncoder();
        accountService = new AccountService(accountRepository, transactionService);
    }

    @Test
    void shouldBeAbleToCreateAccountWhenValidNameAndPasswordIsGiven() {
        String name = "Jaiganesh";
        String password = "Password@234";
        Account account = new Account(name, bCryptPasswordEncoder.encode(password));
        when(accountRepository.save(account)).thenReturn(account);

        Account actualAccount = accountService.create(name, password);

        assertThat(account, is(equalTo(actualAccount)));
    }

    @Test
    void shouldBeAbleToGetAccountSummaryWhenValidAccountIdIsGiven() throws AccountNotFoundException {
        String id = "userAccount";
        String name = "Jaiganesh";
        String password = "Password@234";
        Account account = new Account(name, password);
        when(accountRepository.findById(id)).thenReturn(Optional.of(account));

        Account userAccount = accountService.getAccount(id);

        assertThat(account, is(equalTo(userAccount)));
    }

    @Test
    void shouldThrowAccountNotFoundExceptionWhenGivenIdIsNotValid() {
        String id = "userAccount";

        assertThrows(AccountNotFoundException.class, () -> accountService.getAccount(id));

    }

    @Test
    void shouldReturnTenAsAvailableBalanceWhenTenRupeesIsCreditedToAccount() throws AccountNotFoundException {
        BigDecimal ten = new BigDecimal(10);
        String id = "userAccount";
        String name = "Jaiganesh";
        String password = "Password@234";
        Account userAccount = new Account(name, password);
        when(accountRepository.findById(id)).thenReturn(Optional.of(userAccount));

        Account account = accountService.credit(id, ten);

        assertThat(ten, is(equalTo(account.getAvail_bal())));
    }

    @Test
    void shouldReturnTenAsAvailableBalanceWhenTenRupeesIsDebitedFromAccount() throws AccountNotFoundException {
        BigDecimal ten = new BigDecimal(10);
        String id = "userAccount";
        String name = "Jaiganesh";
        String password = "Password@234";
        Account userAccount = new Account(id, name, password, new BigDecimal(20));
        when(accountRepository.findById(id)).thenReturn(Optional.of(userAccount));

        Account account = accountService.debit(id, ten);

        assertThat(ten, is(equalTo(account.getAvail_bal())));
    }

    @Test
    void shouldAbleToGetAccountTransactionHistoryWhenAccountIdIsGiven() throws AccountNotFoundException {
        String id = "userAccount";
        String name = "Jaiganesh";
        String password = "Password@234";
        Account account = new Account(name, password);
        when(accountRepository.findById(id)).thenReturn(Optional.of(account));

        accountService.getTransactionHistory(id);

        verify(transactionService).getHistory(id);
    }

    @Test
    void shouldAbleToGetAccountTransactionHistoryAndAvailableBalanceWhenAccountIdIsGiven() throws AccountNotFoundException {
        String id = "userAccount";
        String name = "Jaiganesh";
        String password = "Password@234";
        BigDecimal balance = new BigDecimal(0);
        Account account = new Account(id, name, password, balance);
        when(accountRepository.findById(id)).thenReturn(Optional.of(account));
        BigDecimal amount = new BigDecimal(100);
        Transaction credit = new Transaction(account, new Date(), new TransactionType("CREDIT"), amount, amount);
        Transaction debit = new Transaction(account, new Date(), new TransactionType("DEBIT"), amount, balance);
        List<Transaction> transactions = new ArrayList<>(Arrays.asList(credit, debit));
        when(transactionService.getHistory(id)).thenReturn(transactions);
        ArrayList<TransactionResponse> transactionResponse = new ArrayList<TransactionResponse>();
        for (Transaction transaction : transactions) {
            transactionResponse.add(new TransactionResponse(transaction.getId(),transaction.getDate(), transaction.getTransactionType().getName(), transaction.getAmount(), transaction.getBalance()));
        }
        TransactionHistoryResponse transactionHistoryResponse = new TransactionHistoryResponse(id, name, transactionResponse, balance);

        TransactionHistoryResponse actualTransactionHistoryResponse = accountService.getTransactionHistory(id);

        assertThat(transactionHistoryResponse, is(equalTo(actualTransactionHistoryResponse)));
    }
}
