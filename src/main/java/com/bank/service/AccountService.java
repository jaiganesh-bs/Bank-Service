package com.bank.service;

import com.bank.controller.request.CreateAccountRequest;
import com.bank.controller.response.SummaryResponse;
import com.bank.exceptions.AccountNotFoundException;
import com.bank.exceptions.UserAlreadyExistException;
import com.bank.model.Account;
import com.bank.model.UserAccount;
import com.bank.repo.AccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@AllArgsConstructor
@Service
public class AccountService implements UserDetailsService {
    private AccountRepository accountRepository;

    public void create(CreateAccountRequest createAccountRequest) throws UserAlreadyExistException {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        Optional<Account> userAccount = accountRepository.findByEmail(createAccountRequest.getEmail());
        if(userAccount.isPresent()) throw new UserAlreadyExistException();
        Account account = new Account(createAccountRequest.getEmail(), bCryptPasswordEncoder.encode(createAccountRequest.getPassword()));
        accountRepository.save(account);
    }

    public SummaryResponse getAccountSummary(String email) throws AccountNotFoundException {
        Account account = getAccount(email);
        SummaryResponse summaryResponse = new SummaryResponse();
        return summaryResponse.getSummaryResponse(account);
    }

    protected Account getAccount(String email) throws AccountNotFoundException {
        return accountRepository.findByEmail(email).orElseThrow(AccountNotFoundException::new);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            Account account = getAccount(email);
            return new UserAccount(account);
        } catch (AccountNotFoundException e) {
            throw new UsernameNotFoundException(e.getMessage());
        }
    }

    public Account credit(String email, BigDecimal amount) throws AccountNotFoundException {
        Account account = getAccount(email);
        account.credit(amount);
        return account;
    }

    public Account debit(String email, BigDecimal amount) throws AccountNotFoundException {
        Account account = getAccount(email);
        account.debit(amount);
        return account;
    }

}
