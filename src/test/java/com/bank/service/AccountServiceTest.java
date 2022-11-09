package com.bank.service;

import com.bank.model.Account;
import com.bank.repo.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
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
        String id = "example@123";
        String name = "Jaiganesh";
        String password = "Password@234";
        Account account = new Account(id, name, password);
        when(accountRepository.save(any())).thenReturn(account);

        Account actualAccount = accountService.create(name, password);

        assertThat(account,is(equalTo(actualAccount)));
    }
}
