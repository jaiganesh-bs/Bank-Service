package com.bank.service;

import com.bank.exceptions.AccountNotFoundException;
import com.bank.model.Account;
import com.bank.repo.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.*;

public class AccountServiceTest {

    private AccountRepository accountRepository;
    private AccountService accountService;

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @BeforeEach
    public void setUp() {
        accountRepository = mock(AccountRepository.class);
        bCryptPasswordEncoder = new BCryptPasswordEncoder();
        accountService = new AccountService(accountRepository);
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

        assertThat(ten,is(equalTo(account.getAvail_bal())));
    }
}
