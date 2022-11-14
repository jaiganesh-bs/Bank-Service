package com.bank.controller;

import com.bank.controller.request.CreateAccountRequest;
import com.bank.exceptions.AccountNotFoundException;
import com.bank.model.Account;
import com.bank.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.Principal;

import static org.mockito.Mockito.*;

public class AccountControllerTest {

    private AccountService accountService;
    private AccountController accountController;

    @BeforeEach
    public void setUp() {
        accountService = mock(AccountService.class);
        accountController = new AccountController(accountService);
    }

    @Test
    void shouldBeAbleToCreateAccountWithValidNameAndPassword() {
        String name = "Jaiganesh";
        String password = "Password@234";
        CreateAccountRequest createAccountRequest = new CreateAccountRequest(name, password);
        when(accountService.create(createAccountRequest.getName(), createAccountRequest.getPassword())).thenReturn(new Account());

        accountController.create(createAccountRequest);

        verify(accountService).create(createAccountRequest.getName(), createAccountRequest.getPassword());
    }

    @Test
    void shouldBeAbleToGetAccountSummary() throws AccountNotFoundException {
        String id = "accountUser";
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn(id);
        when(accountService.getAccount(id)).thenReturn(new Account());

        accountController.summary(principal);

        verify(accountService).getAccount(id);
    }


}
