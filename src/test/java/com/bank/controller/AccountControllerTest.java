package com.bank.controller;

import com.bank.controller.request.CreateAccountRequest;
import com.bank.controller.response.SummaryResponse;
import com.bank.exceptions.AccountNotFoundException;
import com.bank.exceptions.UserAlreadyExistException;
import com.bank.service.AccountService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.Principal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountControllerTest {
    @Mock
    private AccountService accountService;
    @InjectMocks
    private AccountController accountController;
    @Mock
    private Principal principal;


    @Test
    void shouldBeAbleToCreateAccountWithValidNameAndPassword() throws UserAlreadyExistException {
        String email = "abc@example.com";
        String password = "password";
        CreateAccountRequest createAccountRequest = new CreateAccountRequest(email, password);

        accountController.create(createAccountRequest);

        verify(accountService).create(createAccountRequest);
    }

    @Test
    void shouldBeAbleToGetAccountSummary() throws AccountNotFoundException {
        SummaryResponse expectedSummaryResponse = new SummaryResponse();
        when(accountService.getAccountSummary(principal.getName())).thenReturn(expectedSummaryResponse);

        SummaryResponse actualSummaryResponse = accountController.summary(principal);

        verify(accountService).getAccountSummary(principal.getName());
        assertThat(actualSummaryResponse, is(expectedSummaryResponse));
    }


}
