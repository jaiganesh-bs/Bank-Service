package com.bank.service;

import com.bank.controller.response.TransactionHistoryResponse;
import com.bank.controller.response.TransactionResponse;
import com.bank.exceptions.AccountNotFoundException;
import com.bank.model.Account;
import com.bank.model.Transaction;
import com.bank.model.UserAccount;
import com.bank.repo.AccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class AccountService implements UserDetailsService {
    private AccountRepository accountRepository;

    private TransactionService transactionService;

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

    public Account credit(String id, BigDecimal amount) throws AccountNotFoundException {
        Account account = getAccount(id);
        account.credit(amount);
        return account;
    }

    public Account debit(String id, BigDecimal amount) throws AccountNotFoundException {
        Account account = getAccount(id);
        account.debit(amount);
        return account;
    }

    public TransactionHistoryResponse getTransactionHistory(String id) throws AccountNotFoundException {
        List<Transaction> transactions = transactionService.getHistory(id);
        Account account = getAccount(id);
        List<TransactionResponse> transactionResponse = new ArrayList<>();
        for (Transaction transaction : transactions) {
            transactionResponse.add(new TransactionResponse(transaction.getId(),transaction.getDate(), transaction.getTransactionType().getName(), transaction.getAmount(), transaction.getBalance()));
        }
        TransactionHistoryResponse transactionHistoryResponse = new TransactionHistoryResponse(account.getId(), account.getName(), transactionResponse, account.getAvail_bal());


        return transactionHistoryResponse;
    }
}
