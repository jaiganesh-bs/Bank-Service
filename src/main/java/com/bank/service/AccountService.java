package com.bank.service;

import com.bank.model.Account;
import com.bank.repo.AccountRepository;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;

    public Account create(String name, String password) {
        Account account = new Account(name, password);
        Account userAccount = accountRepository.save(account);
        return userAccount;
    }
}
