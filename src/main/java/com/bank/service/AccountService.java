package com.bank.service;

import com.bank.exceptions.AccountNotFoundException;
import com.bank.model.Account;
import com.bank.model.UserAccount;
import com.bank.repo.AccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class AccountService implements UserDetailsService {
    private final AccountRepository accountRepository;

    public Account create(String name, String password) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        Account account = new Account(name, bCryptPasswordEncoder.encode(password));
        Account userAccount = accountRepository.save(account);
        return userAccount;
    }

    public Account getAccount(String id) throws AccountNotFoundException {
        Optional<Account> optionalAccount = accountRepository.findById(id);
        if (optionalAccount.isPresent()) {
            return optionalAccount.get();
        }
        throw new AccountNotFoundException();
    }

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        try {
            Account account = getAccount(id);
            return new UserAccount(account);
        } catch (AccountNotFoundException e) {
            throw new UsernameNotFoundException(e.getMessage());
        }

    }
}
