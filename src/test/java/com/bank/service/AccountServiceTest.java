package com.bank.service;

import com.bank.controller.request.CreateAccountRequest;
import com.bank.controller.response.SummaryResponse;
import com.bank.exceptions.AccountNotFoundException;
import com.bank.exceptions.UserAlreadyExistException;
import com.bank.model.Account;
import com.bank.repo.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {
    @Mock
    private AccountRepository accountRepository;
    @InjectMocks
    private AccountService accountService;

    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();


    @Test
    void shouldBeAbleToCreateAccountWhenValidNameAndPasswordIsGiven() throws UserAlreadyExistException {
        String email = "ac@example.com";
        String password = "Password@234";
        Account account = new Account(email, bCryptPasswordEncoder.encode(password));
        CreateAccountRequest createAccountRequest = new CreateAccountRequest(email, password);

        accountService.create(createAccountRequest);

        verify(accountRepository).save(account);
    }

    @Test
    void shouldThrowUserAlreadyExistExceptionWhenUserTriesToUseSameEmailToCreateAnAccount() {
        String email = "ac@example.com";
        String password = "Password@234";
        Account account = new Account(email, bCryptPasswordEncoder.encode(password));
        CreateAccountRequest createAccountRequest = new CreateAccountRequest(email, password);
        when(accountRepository.findByEmail(email)).thenReturn(Optional.of(account));

        assertThrows(UserAlreadyExistException.class,()->accountService.create(createAccountRequest));

    }

    @Test
    void shouldBeAbleToGetAccountSummaryWhenValidAccountIdIsGiven() throws AccountNotFoundException {
        String email = "Jaiganesh";
        String password = "Password@234";
        Account account = new Account(email, password);
        when(accountRepository.findByEmail(email)).thenReturn(Optional.of(account));
        SummaryResponse summaryResponse = new SummaryResponse();
        SummaryResponse actualSummaryResponse = summaryResponse.getSummaryResponse(account);

        SummaryResponse expectedAccountSummary = accountService.getAccountSummary(email);

        assertThat(actualSummaryResponse, is(equalTo(expectedAccountSummary)));
    }

    @Test
    void shouldThrowAccountNotFoundExceptionWhenGivenIdIsNotValid() {
        String email = "Jaiganesh";

        assertThrows(AccountNotFoundException.class, () -> accountService.getAccountSummary(email));

    }

    @Test
    void shouldReturnTenAsAvailableBalanceWhenTenRupeesIsCreditedToAccount() throws AccountNotFoundException {
        BigDecimal ten = new BigDecimal(10);
        String email = "abc@example.com";
        String password = "Password@234";
        Account userAccount = new Account(email, password);
        when(accountRepository.findByEmail(email)).thenReturn(Optional.of(userAccount));

        Account account = accountService.credit(email, ten);

        assertThat(ten, is(equalTo(account.getAvail_bal())));
    }

    @Test
    void shouldReturnTenAsAvailableBalanceWhenTenRupeesIsDebitedFromAccount() throws AccountNotFoundException {
        BigDecimal ten = new BigDecimal(10);
        BigDecimal twenty = new BigDecimal(20);
        String email = "abc@example.com";
        String password = "Password@234";
        Account userAccount = new Account(email, password);
        userAccount.setAvail_bal(twenty);
        when(accountRepository.findByEmail(email)).thenReturn(Optional.of(userAccount));

        Account account = accountService.debit(email, ten);

        assertThat(ten, is(equalTo(account.getAvail_bal())));
        assertEquals(ten, account.getAvail_bal());
    }


}
