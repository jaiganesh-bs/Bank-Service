package com.bank.controller;

import com.bank.controller.request.CreateAccountRequest;
import com.bank.controller.response.SummaryResponse;
import com.bank.exceptions.AccountNotFoundException;
import com.bank.model.Account;
import com.bank.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("account")
@AllArgsConstructor
public class AccountController {
    private final AccountService accountService;
    @PostMapping
    public ResponseEntity create(@RequestBody CreateAccountRequest createAccountRequest) {
        Account account = accountService.create(createAccountRequest.getName(), createAccountRequest.getPassword());
        return new ResponseEntity<>(account.getId(), HttpStatus.CREATED);

    }
    @GetMapping("/summary")
    public ResponseEntity summary(Principal principal) throws AccountNotFoundException {
        Account account = accountService.getAccount(principal.getName());
        SummaryResponse summaryResponse = new SummaryResponse(account.getId(), account.getName(), account.getAvail_bal());
        return new ResponseEntity<>(summaryResponse,HttpStatus.OK);
    }
}
