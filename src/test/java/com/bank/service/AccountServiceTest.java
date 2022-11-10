package com.bank.service;

import com.bank.exceptions.AccountNotFoundException;
import com.bank.model.Account;
import com.bank.repo.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.*;
public class AccountServiceTest {

    private AccountRepository accountRepository;
    private AccountService accountService;

    @BeforeEach
    public void setUp(){
        accountRepository = mock(AccountRepository.class);
        accountService = new AccountService(accountRepository);
    }

    @Test
    void shouldBeAbleToCreateAccountWhenValidNameAndPasswordIsGiven() {
        String name = "Jaiganesh";
        String password = "Password@234";
        Account account = new Account(name, password);
        when(accountRepository.save(account)).thenReturn(account);

        Account actualAccount = accountService.create(name, password);

        assertThat(account,is(equalTo(actualAccount)));
    }

    @Test
    void shouldBeAbleToGetAccountSummaryWhenValidAccountIdIsGiven() throws AccountNotFoundException {
        String id = "userAccount";
        String name = "Jaiganesh";
        String password = "Password@234";
        Account account = new Account(name, password);
        when(accountRepository.findById(id)).thenReturn(Optional.of(account));

        Account userAccount = accountService.getAccount(id);

        assertThat(account,is(equalTo(userAccount)));
    }

    @Test
    void shouldThrowAccountNotFoundExceptionWhenGivenIdIsNotValid() {
        String id = "userAccount";

        assertThrows(AccountNotFoundException.class,()-> accountService.getAccount(id));

    }
}
