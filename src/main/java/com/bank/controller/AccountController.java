package com.bank.controller;

import com.bank.controller.request.CreateAccountRequest;
import com.bank.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@Controller
@AllArgsConstructor
public class AccountController {
    private final AccountService accountService;

    public void create(CreateAccountRequest createAccountRequest) {
        accountService.create(createAccountRequest.getName(), createAccountRequest.getPassword());
    }
}
