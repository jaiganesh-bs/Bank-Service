package com.bank.controller;

import com.bank.controller.request.CreateAccountRequest;
import com.bank.controller.response.SummaryResponse;
import com.bank.exceptions.AccountNotFoundException;
import com.bank.exceptions.UserAlreadyExistException;
import com.bank.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("account")
@AllArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody CreateAccountRequest createAccountRequest) throws UserAlreadyExistException {
        accountService.create(createAccountRequest);
    }

    @GetMapping("summary")
    public SummaryResponse summary(Principal principal) throws AccountNotFoundException {
        return accountService.getAccountSummary(principal.getName());
    }
}
