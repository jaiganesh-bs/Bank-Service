package com.bank.service;

import com.bank.exceptions.AccountNotFoundException;
import com.bank.exceptions.InvalidAmountException;
import com.bank.model.Account;
import com.bank.model.Transaction;
import com.bank.repo.TransactionRepository;
import com.bank.repo.TransactionTypeRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class TransactionService {

    private TransactionRepository transactionRepository;

    private AccountService accountService;

    private TransactionTypeRepository transactionTypeRepository;

    public void credit(String accountId, BigDecimal amount) throws AccountNotFoundException, InvalidAmountException {
        if(isValidAmount(amount)) throw new InvalidAmountException();
        Account account = accountService.credit(accountId, amount);
        Transaction credit = new Transaction(account, getToday(), transactionTypeRepository.findByName("CREDIT"), amount, account.getAvail_bal());
        transactionRepository.save(credit);
    }

    private boolean isValidAmount(BigDecimal amount) {
        return amount.signum() != 1;
    }

    private Date getToday() {
        return new Date();
    }
}
