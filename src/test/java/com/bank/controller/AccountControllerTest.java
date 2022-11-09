package com.bank.controller;

import com.bank.controller.request.CreateAccountRequest;
import com.bank.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class AccountControllerTest {

    private AccountService accountService;

    @BeforeEach
    public void setUp(){
        accountService = mock(AccountService.class);
    }
    @Test
    void shouldBeAbleToCreateAccountWithValidNameAndPassword() {
        AccountController accountController = new AccountController(accountService);
        String name = "Jaiganesh";
        String password = "Password@234";

        CreateAccountRequest createAccountRequest = new CreateAccountRequest(name, password);
        accountController.create(createAccountRequest);
        verify(accountService).create(createAccountRequest.getName(),createAccountRequest.getPassword());
    }
}
